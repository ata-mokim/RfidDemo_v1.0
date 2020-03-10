package com.wsmr.app.barcode.view;

import com.wsmr.app.barcode.R;
import com.wsmr.app.barcode.dialog.CommonDialog;
import com.wsmr.app.barcode.view.base.ReadWriteMemoryActivity;
import com.wsmr.lib.dev.ATRfidReader;
import com.wsmr.lib.dev.rfid.ATRfid900MAReader;
import com.wsmr.lib.dev.rfid.exception.ATRfidReaderException;
import com.wsmr.lib.dev.rfid.param.EpcMatchParam;
import com.wsmr.lib.dev.rfid.param.QValue;
import com.wsmr.lib.dev.rfid.param.RangeValue;
import com.wsmr.lib.dev.rfid.param.SelectionMask6b;
import com.wsmr.lib.dev.rfid.type.ActionState;
import com.wsmr.lib.dev.rfid.type.BankType;
import com.wsmr.lib.dev.rfid.type.GlobalBandType;
import com.wsmr.lib.dev.rfid.type.InventorySession;
import com.wsmr.lib.dev.rfid.type.InventoryTarget;
import com.wsmr.lib.dev.rfid.type.MaskMatchingType;
import com.wsmr.lib.dev.rfid.type.ResultCode;
import com.wsmr.lib.dev.rfid.type.TagType;
import com.wsmr.lib.diagnostics.ATLog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ini4j.Ini;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class WriteMemoryActivity extends ReadWriteMemoryActivity {

    private static final String TAG = WriteMemoryActivity.class.getSimpleName();

    // ------------------------------------------------------------------------
    // Member Variable
    // ------------------------------------------------------------------------
    protected Ini ini;
    protected String tempOperationTime;
    protected String tempGlobalBand;
    protected String tempInventoryTime;
    protected String tempIdleTime;
    protected String tempSession;
    protected String tempTarget;
    protected String tempStartQ;
    protected String tempMaxQ;
    protected String tempMinQ;
    protected String tempPowerGain;

    private static int InitFlag = 0;

    private RangeValue mPowerRange;
    private GlobalBandType mGlobalBand;
    private int mOperationTime;
    private int mInventoryTime;
    private int mIdleTime;
    private int mPowerLevel;

    private String[] mFreqChanNames;
    private boolean[] mFreqChanUses;
    private InventorySession mInventorySession;
    private InventoryTarget mInventoryTarget;
    private QValue mQValue;

    private TextView txtWriteValue_Hex;
    private TextView txtWriteValue_Ascii;
    private Button btnActionHex;
    private Button btnActionAscii;

    private String mWriteValue_Hex;
    private String mWriteValue_Ascii;


    // 신규로 추가한 소스 20200310 mino
    Spinner spinner;
    String[] arrayPc   = {"2800", "3000", "3800", "4000","4800","5000","5800" , "6000","6800", "7000",  "7800", "8000",  "8800",   "9000" };
    String[] bits      = {"80bit", "96bit", "112bit" , "128bit", "144bit", "160bit", "176bit", "192bit", "208bit", "224bit", "240bit", "256bit", "272bit", "288bit"  };
    private TextView textView_epc[] = new TextView[18];
    String epc[] = new String[18];
    String pc = "";
    private int spinnerIndex;
    private boolean asciiFlag=false ;

    RadioGroup radioGroup_Hex_Ascii;
    RadioButton radioButton_Hex;
    RadioButton radioButton_Ascii;
    String writeEpc = "";
    String data = "";
    TextView  txtSelection;
    TextView  txtMessage;

    public TextView getTxtWriteValue_Ascii() {
        return txtWriteValue_Ascii;
    }
    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    public WriteMemoryActivity() {
        super();

        mQValue = new QValue();

        mView = R.layout.activity_write_memory;
        mWriteValue_Hex = "";
        mWriteValue_Ascii = "";
    }

    // ------------------------------------------------------------------------
    // Activity Event Handler
    // ------------------------------------------------------------------------

    @Override
    public void onDestroy() {
        super.onDestroy();

        Set_IniFile();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.write_value_Hex:
                ATLog.i(TAG, "INFO. onClick(write_value)");
                CommonDialog.showStringDialog(this, R.string.write_data_Hex, mWriteValue_Hex, mWriteValueListener_Hex);
                break;
            case R.id.write_value_Ascii:
                ATLog.i(TAG, "INFO. onClick(write_value)");
                CommonDialog.showAsciiStringDialog(this, R.string.write_data_Ascii, mWriteValue_Ascii, mWriteValueListener_Ascii);
                break;
            case R.id.actionascii:
                ATLog.i(TAG, "INFO. onClick(write_button_ascii)");
                startAction();
                break;
        }
    }

    // ------------------------------------------------------------------------
    // Reader Control Methods
    // ------------------------------------------------------------------------

    // Start Action
    @Override
    protected void startAction() {

        //꺼짐방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        /* mino

            if (btnActionHex.isPressed()){
                data = getmWriteValue_Hex();
            }else if(btnActionAscii.isPressed()){
                String tdata = getmWriteValue_Ascii();
                data = StringUtil.convertStringToHex(tdata);
            }

            int offset = getOffset();
         */

        // new code mino start
        writeEpc = "";
        if ( setEpc (spinnerIndex) == false ) {

        }

        Log.e("#########  ",   " writeEpc :"   + String.format( "| %s ",  writeEpc));
        Log.e("#########  ",   "  pc :"   + String.format( "| %s ",   pc ));

        if (radioButton_Hex.isChecked()){
            data = pc + writeEpc ;
        }else if(radioButton_Ascii.isChecked()){
            data = pc +  getStringToHex(writeEpc);
        }
        int offset = 1;
        // end

        ResultCode res;
        BankType bank = getBank();

/*        String dataHex = getmWriteValue_Hex();
        String dataAscii = getmWriteValue_Ascii();*/
        String password = getPassword();
        EpcMatchParam epc = getEpc();
        TagType tagType = getTagType();

        txtSelection.setText("");
        txtMessage.setText("");

        enableWidgets(false);

        if(tagType == TagType.Tag6C) {


            if ((res = mReader.writeMemory6c(bank, offset, data, password, epc)) != ResultCode.NoError) {
                Log.e(TAG,  String.format("ERROR. startAction() - Failed to write memory 6B tag [%s]", res.toString() ));
                enableWidgets(true);
                return;
            }else {
                Log.e(TAG,  String.format("SUCCESS. startAction() - write memory 6B tag [%s]", res.toString() ));
            }



        } else if(tagType == TagType.Tag6B) {

            ATRfid900MAReader MAReader = (ATRfid900MAReader)mReader;
            offset *= 2;
            String mask = getSelection();

            SelectionMask6b mask6b = new SelectionMask6b(0, mask, MaskMatchingType.Match);
            if ((res = MAReader.writeMemory6b(offset, data, mask6b)) != ResultCode.NoError) {
                Log.e(TAG,  String.format("ERROR. startAction() - Failed to write memory 6B tag [%s]", res.toString() ));
                enableWidgets(true);
                return;
            }
        } else {
            Toast.makeText(this, R.string.not_supported, Toast.LENGTH_LONG).show();
            enableWidgets(true);
        }

        ATLog.i(TAG, "INFO. startAction()");

        //Set_IniFile();
    }


    @Override
    public void onReaderResult(ATRfidReader reader, ResultCode code, ActionState action, String epc, String s1,
                               float rssi, float phase) {

        resultMessage(code);
        setSelection(data);

        Log.e("###########  ",   ":"   + String.format( "(%s, %s, [%s], [%s], %.2f, %.2f ",  code, action, epc, s1, rssi, phase  ) );
        ATLog.i(TAG, "EVENT. onReaderResult(%s, %s, [%s], [%s], %.2f, %.2f", code, action, epc, data, rssi, phase);

        playSuccess();
    }

    // Clear Widgets
    @Override
    protected void clear() {
        super.clear();

        for (int i = 0 ; i < epc.length ; i++)
        {
            epc[i] = "";
            textView_epc[i].setText (epc[i]) ;
        }
    }


    public static String getStringToHex(String s) {
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            result += String.format("%02X", (int) s.charAt(i));
        }

        return result;
    }
    public boolean setEpc(int position)
    {
        for (int i = 0 ; i < 5 + position ; i++)
        {
            epc[i] = textView_epc[i].getText().toString().trim();

        }

        if ( radioButton_Ascii.isChecked() )
        {
            for (int i = 0; i < 5 + position; i++) {

                if (epc[i].trim().length() != 2) {
                    // 원하는 자리수가 채워지지 않을경우 팝업
                    showMessage(i, "Hex 는 4자리 Ascii 는 2자리로 채워주세요");
                    return false;
                }
                writeEpc = writeEpc + epc[i];
            }

        }else {

            for (int i = 0; i < 5 + position; i++) {

                if (epc[i].trim().length() != 4) {
                    // 원하는 자리수가 채워지지 않을경우 팝업
                    showMessage(i, "Hex 는 4자리 Ascii 는 2자리로 채워주세요");
                    return false;
                }

                writeEpc = writeEpc + epc[i];

            }
        }

        return true ;
    }

    private void showMessage(int position, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("안내");
        builder.setMessage(msg);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        textView_epc[position].requestFocus();

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void errorShowMessage(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Error");
        builder.setMessage(msg);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // ------------------------------------------------------------------------
    // Override Widgets Control Methods
    // ------------------------------------------------------------------------

    // Initialize Activity Widgets
    @Override
    protected void initWidgets() {

        //꺼짐방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.initWidgets();

        // Initialize Write Data EditText
        txtWriteValue_Hex = (TextView) findViewById(R.id.write_value_Hex);
        txtWriteValue_Hex.setOnClickListener(this);
        // Initialize Write Data EditText
        txtWriteValue_Ascii = (TextView) findViewById(R.id.write_value_Ascii);
        txtWriteValue_Ascii.setOnClickListener(this);

        // Initialize Action Button
        btnActionHex = (Button) findViewById(R.id.actionhex);
        btnActionHex.setOnClickListener(this);
        // Initialize Action Button
        btnActionAscii = (Button) findViewById(R.id.actionascii);
        btnActionAscii.setOnClickListener(this);


        setBank(BankType.EPC);
        setOffset(2);


        // spinner 설정 ( combobox )
        spinner     = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this , R.layout.spinner_text, bits);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(adapter);
        //Load_IniFile();

        // ViewBy Id
        this.setViewById();
        // epc text box 설정
        for (int i = 0 ; i < epc.length ; i++)
        {
            epc[i] = "";
            textView_epc[i].setText (epc[i]) ;
        }
        // epc text 에 focusChange 설정
        this.setListener();
        spinner.setSelection(1);

        radioButton_Hex = findViewById(R.id.radioButton_Hex);
        radioButton_Ascii = findViewById(R.id.radioButton_Ascii);

        radioButton_Hex.setChecked(true);
        pc = "3000";

        txtSelection = (TextView) findViewById(R.id.selection);
        txtMessage = (TextView) findViewById(R.id.message);

    }

    private void setListener()
    {

        // hex ascii 라디오 버튼 이벤트
        radioGroup_Hex_Ascii.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.radioButton_Ascii){
                    asciiFlag = true;
                }else{
                    asciiFlag = false;
                }

            }
        });


        // epc text 포커스 시에 배경색 바꿈 START
        textView_epc[0].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[0].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[0].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[1].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[1].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[1].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[2].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[2].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[2].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[3].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[3].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[3].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[4].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[4].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[4].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[5].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[5].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[5].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[6].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[6].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[6].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[7].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[7].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[7].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[8].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[8].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[8].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[9].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[9].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[9].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });


        textView_epc[10].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[10].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[10].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[11].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[11].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[11].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[12].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[12].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[12].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[13].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[13].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[13].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[14].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[14].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[14].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[15].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[15].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[15].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[16].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[16].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[16].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });

        textView_epc[17].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    textView_epc[17].setBackgroundResource(R.drawable.fource_text_style);
                }else {
                    textView_epc[17].setBackgroundResource(R.drawable.edit_text_style);
                }
            }
        });
        // 배경생 바꿈 END


        // spinner 의 선택에 따르 epc text 박스 visible 기능
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View View, int position , long id){

                pc = arrayPc[position] ;
                spinnerIndex = position;

                if (position ==0 )
                {
                    setTextBoxVisible(position, adapterView);
                } else if (position ==1 ) {
                    setTextBoxVisible(position, adapterView);
                } else if (position ==2 ) {
                    setTextBoxVisible(position, adapterView);
                }else if (position ==3 ) {
                    setTextBoxVisible(position, adapterView);
                }else if (position ==4 ) {
                    setTextBoxVisible(position, adapterView);
                }else if (position ==5 ) {
                    setTextBoxVisible(position, adapterView);
                }else if (position ==6 ) {
                    setTextBoxVisible(position, adapterView);
                }else if (position ==7 ) {
                    setTextBoxVisible(position, adapterView);
                }else if (position ==8 ) {
                    setTextBoxVisible(position, adapterView);
                }else if (position ==9 ) {
                    setTextBoxVisible(position, adapterView);
                }else if (position ==10 ) {
                    setTextBoxVisible(position, adapterView);
                }else if (position ==11) {
                    setTextBoxVisible(position, adapterView);
                }else if (position ==12 ) {
                    setTextBoxVisible(position, adapterView);
                }else if (position ==13 ) {
                    setTextBoxVisible(position, adapterView);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
                pc = "";
            }
        } );

        // epc text 박스에 text 개수에 따라 다음 칸으로 이동
        textView_epc[0].addTextChangedListener(new TextWatcher() {

            public String text = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[0].length()==i){  // edit1  값의 제한값을 6이라고 가정했을때
                    textView_epc[1].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[0].length()== i) {
                    text = textView_epc[0].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[0].length()> i){
                    textView_epc[0].setText(text);
                }*/
            }
        });


        textView_epc[1].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[1].length()==i){  // edit1  값의 제한값을 6이라고 가정했을때
                    textView_epc[2].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[1].length()== i) {
                    text = textView_epc[1].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[1].length()> i){
                    textView_epc[1].setText(text);
                }

                 */
            }
        });

        textView_epc[2].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[2].length()==i){  // edit2  값의 제한값을 6이라고 가정했을때
                    textView_epc[3].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[2].length()== i) {
                    text = textView_epc[2].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[2].length()> i){
                    textView_epc[2].setText(text);
                }

                 */
            }
        });

        textView_epc[3].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[3].length()==i){  // edit3  값의 제한값을 6이라고 가정했을때
                    textView_epc[4].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[3].length()== i) {
                    text = textView_epc[3].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[3].length()> i){
                    textView_epc[3].setText(text);
                }

                 */
            }
        });

        textView_epc[4].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[4].length()==i){  // edit4  값의 제한값을 6이라고 가정했을때
                    textView_epc[5].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[4].length()== i) {
                    text = textView_epc[4].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[4].length()> i){
                    textView_epc[4].setText(text);
                }

                 */
            }
        });

        textView_epc[5].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[5].length()==i){  // edit5  값의 제한값을 6이라고 가정했을때
                    textView_epc[6].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[5].length()== i) {
                    text = textView_epc[5].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[5].length()> i){
                    textView_epc[5].setText(text);
                }

                 */
            }
        });

        textView_epc[6].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[6].length()==i){  // edit6  값의 제한값을 6이라고 가정했을때
                    textView_epc[7].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[6].length()== i) {
                    text = textView_epc[6].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[6].length()> i){
                    textView_epc[6].setText(text);
                }

                 */
            }
        });

        textView_epc[7].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[7].length()==i){  // edit7  값의 제한값을 7이라고 가정했을때
                    textView_epc[8].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[7].length()== i) {
                    text = textView_epc[7].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[7].length()> i){
                    textView_epc[7].setText(text);
                }

                 */
            }
        });

        textView_epc[8].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[8].length()==i){  // edit8  값의 제한값을 8이라고 가정했을때
                    textView_epc[9].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[8].length()== i) {
                    text = textView_epc[8].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[8].length()> i){
                    textView_epc[8].setText(text);
                }

                 */
            }
        });

        textView_epc[9].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[9].length()==i){  // edit9  값의 제한값을 9이라고 가정했을때
                    textView_epc[10].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[9].length()== i) {
                    text = textView_epc[9].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;


                if(textView_epc[9].length()> i){
                    textView_epc[9].setText(text);
                }

                 */
            }
        });

        textView_epc[10].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[10].length()==i){  // edit10  값의 제한값을 10이라고 가정했을때
                    textView_epc[11].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[10].length()== i) {
                    text = textView_epc[10].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;


                if(textView_epc[10].length()> i){
                    textView_epc[10].setText(text);
                }

                 */
            }
        });

        textView_epc[11].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[11].length()==i){  // edit11  값의 제한값을 11이라고 가정했을때
                    textView_epc[12].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[11].length()== i) {
                    text = textView_epc[11].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;


                if(textView_epc[11].length()> i){
                    textView_epc[11].setText(text);
                }

                 */
            }
        });

        textView_epc[12].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[12].length()==i){  // edit12  값의 제한값을 12이라고 가정했을때
                    textView_epc[13].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[12].length()== i) {
                    text = textView_epc[12].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[12].length()> i){
                    textView_epc[12].setText(text);
                }


                 */
            }
        });

        textView_epc[13].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[13].length()==i){  // edit13  값의 제한값을 13이라고 가정했을때
                    textView_epc[14].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[13].length()== i) {
                    text = textView_epc[13].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[13].length()> i){
                    textView_epc[13].setText(text);
                }

                 */
            }
        });

        textView_epc[14].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[14].length()==i){  // edit14  값의 제한값을 14이라고 가정했을때
                    textView_epc[15].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[14].length()== i) {
                    text = textView_epc[14].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[14].length()> i){
                    textView_epc[14].setText(text);
                }

                 */
            }
        });

        textView_epc[15].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[15].length()==i){  // edit15  값의 제한값을 15이라고 가정했을때
                    textView_epc[16].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[15].length()== i) {
                    text = textView_epc[15].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[15].length()> i){
                    textView_epc[15].setText(text);
                }

                 */
            }
        });

        textView_epc[16].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[16].length()==i){  // edit16  값의 제한값을 16이라고 가정했을때
                    textView_epc[17].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[16].length()== i) {
                    text = textView_epc[16].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[16].length()> i){
                    textView_epc[16].setText(text);
                }

                 */
            }
        });

        textView_epc[17].addTextChangedListener(new TextWatcher() {

            public String text = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[17].length()==i){  // edit17  값의 제한값을 17이라고 가정했을때
                    //textView_epc[18].requestFocus(); // 두번째EditText 로 포커스가 넘어가게 됩니다
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[17].length()== i) {
                    text = textView_epc[17].getText().toString();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                /*
                int i = 4;
                if ( asciiFlag)
                    i = 2;

                if(textView_epc[17].length()> i){
                    textView_epc[17].setText(text);
                }

                 */
            }
        });



    }
    // Eanble Activity Widgets
    @Override
    protected void enableWidgets(boolean enabled) {
        super.enableWidgets(enabled);

        if (mReader.getAction() == ActionState.Stop) {
            txtWriteValue_Hex.setEnabled(enabled);
            txtWriteValue_Ascii.setEnabled(enabled);
            btnActionHex.setText(R.string.actionhex_write);
            btnActionAscii.setText(R.string.actionascii_write);
        } else {
            txtWriteValue_Hex.setEnabled(false);
            txtWriteValue_Ascii.setEnabled(false);
            btnActionHex.setText(R.string.actionhex_stop);
            btnActionAscii.setText(R.string.actionascii_stop);
        }
        btnActionHex.setEnabled(enabled);
        btnActionAscii.setEnabled(enabled);


    }

    private String getmWriteValue_Hex() {
        return mWriteValue_Hex;
    }

    private void setmWriteValue_Hex(String value) {
        mWriteValue_Hex = value;
        txtWriteValue_Hex.setText(mWriteValue_Hex);
    }

    private String getmWriteValue_Ascii() {
        return mWriteValue_Ascii;
    }

    private void setmWriteValue_Ascii(String value) {
        mWriteValue_Ascii = value;
        txtWriteValue_Ascii.setText(mWriteValue_Ascii);
    }

    // ------------------------------------------------------------------------
    // CommonDialog Result Listener
    // ------------------------------------------------------------------------

    private CommonDialog.IStringDialogListener mWriteValueListener_Hex = new CommonDialog.IStringDialogListener() {

        @Override
        public void onConfirm(String value, DialogInterface dialog) {
            setmWriteValue_Hex(value);
            ATLog.i(TAG, "INFO. mWriteValueListener.$CommonDialog.IStringDialogListener.onConfirm([%s])", value);
        }
    };

    private CommonDialog.IAsciiStringDialogListener mWriteValueListener_Ascii = new CommonDialog.IAsciiStringDialogListener() {

        @Override
        public void onConfirm(String value, DialogInterface dialog) {
            setmWriteValue_Ascii(value);
            ATLog.i(TAG, "INFO. mWriteValueListener.$CommonDialog.IStringDialogListener.onConfirm([%s])", value);
        }
    };


    // ------------------------------------------------------------------------
    // Ini File Initial Load
    // ------------------------------------------------------------------------

    private void Load_IniFile() {
        File file = new File("/sdcard/RFIDParm.ini");
        Properties prop = new Properties();

        if (file.exists()){

            try {
                prop.load(new FileInputStream(file));
            }catch (IOException e){
                e.printStackTrace();
            }

        }else {
            try {
                prop.setProperty("[RFIDParm]","");
                prop.setProperty("OperationTime","0");
                prop.setProperty("GlobalBand","Korea");
                prop.setProperty("InventoryTime","400");
                prop.setProperty("IdleTime","0");
                prop.setProperty("Session","Session 2");
                prop.setProperty("Target","AB");
                prop.setProperty("StartQ","4");
                prop.setProperty("MaxQ","15");
                prop.setProperty("MinQ","0");
                prop.setProperty("PowerGain","30.0");

                prop.store(new FileOutputStream(file),null);

                try {
                    prop.load(new FileInputStream(file));
                }catch (IOException e){
                    e.printStackTrace();
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        tempOperationTime = prop.getProperty("OperationTime");
        mOperationTime = Integer.parseInt(tempOperationTime);

        tempInventoryTime = prop.getProperty("InventoryTime");
        mInventoryTime = Integer.parseInt(tempInventoryTime);

        tempIdleTime = prop.getProperty("IdleTime");
        mIdleTime = Integer.parseInt(tempIdleTime);

        tempStartQ = prop.getProperty("StartQ");
        mQValue.setStratQ(Integer.parseInt(tempStartQ));

        tempMaxQ = prop.getProperty("MaxQ");
        mQValue.setMaxQ(Integer.parseInt(tempMaxQ));

        tempMinQ = prop.getProperty("MinQ");
        mQValue.setMinQ(Integer.parseInt(tempMinQ));

        tempPowerGain = prop.getProperty("PowerGain");
        double f = Double.parseDouble(tempPowerGain) * 10;
        mPowerLevel = (int)f;

        tempSession =prop.getProperty("Session");
        switch (tempSession) {
            case "Session 0":
                mInventorySession = InventorySession.S0;
                break;
            case "Session 1":
                mInventorySession = InventorySession.S1;
                break;
            case "Session 2":
                mInventorySession = InventorySession.S2;
                break;
            case "Session 3":
                mInventorySession = InventorySession.S3;
                break;
        }

        tempTarget = prop.getProperty("Target");
        switch (tempTarget) {
            case "AB":
                mInventoryTarget = InventoryTarget.AB;
                break;
            case "A":
                mInventoryTarget = InventoryTarget.A;
                break;
            case "B":
                mInventoryTarget = InventoryTarget.B;
                break;
        }

        tempGlobalBand = prop.getProperty("GlobalBand");
        mGlobalBand = GlobalBandType.Korea;

        new Thread(new Runnable() {

            @Override
            public void run() {

                // Set Power Level
                try {
                    mReader.setPower(mPowerLevel);
                } catch (ATRfidReaderException e) {
                    ATLog.e(TAG, e, "ERROR. saveOption() - Failed to set power level");

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // WaitDialog.hide();
                            enableWidgets(true);
                        }
                    });
                    return;
                }
                ATLog.i(TAG, "INFO. saveOption() - [Power Level : %d]", mPowerLevel);

                // Set Operation Time
                try {
                    mReader.setOperationTime(mOperationTime);
                } catch (ATRfidReaderException e) {
                    ATLog.e(TAG, e, "ERROR. saveOption() - Failed to set operation Time");

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //WaitDialog.hide();
                            enableWidgets(true);
                        }
                    });
                    return;
                }
                ATLog.i(TAG, "INFO. saveOption() - [Operation Time : %d]", mOperationTime);


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        setPowerLevel(mPowerLevel);

                        // Set Operation Time
                        setOperationTime(mOperationTime);

                        enableWidgets(true);

                    }
                });
            }
        }).start();
    }


    private void Set_IniFile(){
        File file = new File("/sdcard/RFIDParm.ini");
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream(file));
        }catch (IOException e){
            e.printStackTrace();
        }


        TextView txtOperationTime = (TextView) findViewById(R.id.operation_time);

        // Initialize Power Gain
        TextView txtPower = (TextView) findViewById(R.id.power_gain);


        String tOperationTime = (String)txtOperationTime.getText();
        String[] words = tOperationTime.split(" ");
        prop.setProperty("OperationTime",words[0]);

        String tPower = (String)txtPower.getText();
        String[] words3 = tPower.split(" ");
        prop.setProperty("PowerGain",words3[0]);

        try {
            prop.store(new FileOutputStream(file),null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 신규로 추가한 소스 20200310 mino
    // epc 세틍하는 text view 에 커서가 포커스 될경우
    // background 색상을 변경한다 .

    public void onFocusChange(View view, boolean hasFocus) {

        switch (view.getId()) {
            case R.id.textView_epc1:
                if (hasFocus) {
                    textView_epc[0].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[0].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;
            case R.id.textView_epc2:
                if (hasFocus) {
                    textView_epc[1].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[1].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;

            case R.id.textView_epc3:
                if (hasFocus) {
                    textView_epc[2].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[2].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;

            case R.id.textView_epc4:
                if (hasFocus) {
                    textView_epc[3].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[3].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;

            case R.id.textView_epc5:
                if (hasFocus) {
                    textView_epc[4].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[4].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;

            case R.id.textView_epc6:
                if (hasFocus) {
                    textView_epc[5].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[5].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;

            case R.id.textView_epc7:
                if (hasFocus) {
                    textView_epc[6].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[6].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;
            case R.id.textView_epc8:
                if (hasFocus) {
                    textView_epc[7].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[7].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;
            case R.id.textView_epc9:
                if (hasFocus) {
                    textView_epc[8].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[8].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;
            case R.id.textView_epc10:
                if (hasFocus) {
                    textView_epc[9].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[9].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;
            case R.id.textView_epc11:
                if (hasFocus) {
                    textView_epc[10].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[10].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;
            case R.id.textView_epc12:
                if (hasFocus) {
                    textView_epc[11].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[11].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;
            case R.id.textView_epc13:
                if (hasFocus) {
                    textView_epc[12].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[12].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;
            case R.id.textView_epc14:
                if (hasFocus) {
                    textView_epc[13].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[13].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;
            case R.id.textView_epc15:
                if (hasFocus) {
                    textView_epc[14].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[14].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;
            case R.id.textView_epc16:
                if (hasFocus) {
                    textView_epc[15].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[15].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;
            case R.id.textView_epc17:
                if (hasFocus) {
                    textView_epc[16].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[16].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;
            case R.id.textView_epc18:
                if (hasFocus) {
                    textView_epc[17].setBackgroundResource(R.drawable.fource_text_style);
                } else {
                    textView_epc[17].setBackgroundResource(R.drawable.edit_text_style);
                }
                break;

            }
        }

        @SuppressLint("WrongViewCast")
        private void setViewById()
        {

            textView_epc[0]        =  findViewById(R.id.textView_epc1);
            textView_epc[1]        =  findViewById(R.id.textView_epc2);
            textView_epc[2]        =  findViewById(R.id.textView_epc3);
            textView_epc[3]        =  findViewById(R.id.textView_epc4);
            textView_epc[4]        =  findViewById(R.id.textView_epc5);
            textView_epc[5]        =  findViewById(R.id.textView_epc6);
            textView_epc[6]        =  findViewById(R.id.textView_epc7);
            textView_epc[7]        =  findViewById(R.id.textView_epc8);
            textView_epc[8]        =  findViewById(R.id.textView_epc9);
            textView_epc[9]        =  findViewById(R.id.textView_epc10);
            textView_epc[10]       =  findViewById(R.id.textView_epc11);
            textView_epc[11]       =  findViewById(R.id.textView_epc12);
            textView_epc[12]       =  findViewById(R.id.textView_epc13);
            textView_epc[13]       =  findViewById(R.id.textView_epc14);
            textView_epc[14]       =  findViewById(R.id.textView_epc15);
            textView_epc[15]       =  findViewById(R.id.textView_epc16);
            textView_epc[16]       =  findViewById(R.id.textView_epc17);
            textView_epc[17]       =  findViewById(R.id.textView_epc18);

            radioGroup_Hex_Ascii = findViewById(R.id.radioGroup_Hex_Ascii);

        }

        public boolean setTextBoxVisible(int position, AdapterView<?> adapterView)
        {
            for (int i = 5 ; i < 5+position ; i++)
            {
                textView_epc[i].setVisibility(adapterView.VISIBLE);
            }

            for (int i = 5+position  ; i < epc.length ; i++)
            {
                textView_epc[i].setVisibility(adapterView.INVISIBLE);
            }

            return true ;
        }

    }
