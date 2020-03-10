package com.wsmr.app.barcode.type.SE4710;

public enum Scan1dSymbolOption {
    UpcEan("UPC/EAN"),
    Code128("Code 128"),
    Code39("Code 39"),
    Code93("Code 93"),
    Code11("Code 11"),
    I2of5("Interleaved 2 of 5"),
    D2of5("Discrete 2 of 5"),
    Ch2of5("Chinese 2 of 5"),
    Codabar("Codabar"),
    Msi("MSI"),
    Aztec("Aztec"),
    Data_Matrix("Data Matrix"),
    Maxicode("Maxicode"),
    MicroPDF417("MicroPDF417"),
    MicroQR("MicroQR"),
    PDF417("PDF417"),
    QRCode("QRCode"),
    Matrix2of5("Matrix 2 of 5"),
    Korea_3of5("Korea 3 of 5"),
    US_Postnet("US Postnet"),
    UK_Postal("UK Postal"),
    US_Planet("US Planet"),
    Japan_Postal("Japan Postal"),
    Australia_Post("Australia Post"),
    Netherlands_KIX_Code("Netherlands KIX Code"),
    USPS_4CB_OneCode_Inteligent_Mail("USPS 4CB One Code Inteligent Mail"),
    UPU_FICS_Postral("UPU FICS Postral"),
    Composite("Composite"),
    /*Composite_CC_C("Composite CC-C"),
    Composite_CC_AB("Composite CC-AB"),
    Composite_TLC_39("Composite TLC-39"),*/
    ISSN_EAN("ISSN EAN"),
    GS1_128("GS1 128"),
    ISBT_128("ISBT 128"),
    RSS_14("RSS 14(GS1 DataBar)"),
    /*RSS_Limited("RSS Limited(GS1 DataBar Limited)"),
    RSS_Expanded("RSS Expanded(GS1 DataBar Expanded)"),*/
    HanXin("Han Xin(Chinese-Sensible Code)");

    private String name;

    private Scan1dSymbolOption(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
