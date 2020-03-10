package com.wsmr.app.barcode.type.MDI3x00;

public enum ScanOpticonSymbolOption {
    UPCA("UPC-A"),
    UPCE("UPC-E"),
    EAN13("EAN-13"),
    EAN8("EAN-8"),
    Code39("Code 39"),
    Codabar("Codabar"),
    Industrial2of5("Industrial 2 of 5"),
    Interleaved2of5("Interleaved 2 of 5"),
    SCode("S-Code"),
    Code128("Code 128"),
    Code93("Code 93"),
    IATA("IATA"),
    MSI_Plessey("MSI/Plessey"),
    UK_Plessey("UK/Plessey"),
    Telepen("Telepen"),
    Code11("Code 11"),
    Matrix2of5("Matrix 2 of 5"),
    ChinesePost("Chinese Post"),
    KoreanPostalAuthority("Korean Postal Authority"),
    IntelligentMailBarCode("Intelligent Mail Bar Code"),
    POSTNET("POSTNET"),
    JapanesePost("Japanese Post"),
    GS1_Databar("GS1 DataBar"),
    PDF417("PDF417"),
    MicroPDF417("MicroPDF417"),
    CodablockF("Codablock F"),
    QRCode("QR code"),
    MicroQR("Micro QR"),
    DataMatrix("Data Matrix"),
    Aztec("Aztec"),
    ChineseSensibleCode("Chinese-Sensible Code"),
    Maxicode("Maxicode");

    private String name;

    private ScanOpticonSymbolOption(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
