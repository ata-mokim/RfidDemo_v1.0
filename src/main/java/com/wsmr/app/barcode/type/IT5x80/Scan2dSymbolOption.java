package com.wsmr.app.barcode.type.IT5x80;

public enum Scan2dSymbolOption {
    Codabar("Codabar"),
    Code39("Code 39"),
    I2of5("Interleaved 2 of 5"),
    Code93("Code 93"),
    R2of5("Straight 2 of 5 Industrial"),
    A2of5("Straight 2 of 5 IATA"),
    X2of5("Matrix 2 of 5"),
    Code11("Code 11"),
    Code128("Code 128"),
    Telepen("Telepen"),
    UPCA("UPC-A"),
    UPCE0("UPC-E0"),
    UPCE1("UPC-E1"),
    EAN13("EAN/JAP-13"),
    EAN8("EAN/JAP-8"),
    MSI("MSI"),
    PlesseyCode("Plessey Code"),
    RSS14("RSS-14"),
    RSSLimit("RSS Limited"),
    RSSExp("RSS Expanded"),
    PosiCode("PosiCode"),
    TriopticCode("Trioptic Code"),
    CodablockF("Codablock F"),
    Code16K("Code 16K"),
    Code49("Code 49"),
    PDF417("PDF 417"),
    MicroPDF("MicroPDF 417"),
    ComCode("EAN/UCC Composite Code"),
    TLC39("TCIF Linked Code 39"),
    Postnet("Postnet"),
    Planet("Planet Code"),
    BritishPost("British Post"),
    CanadianPost("Canadian Post"),
    KixPost("Kix (Netherlands) Post"),
    AustralianPost("Australian Post"),
    JapanesePost("Japanese Post"),
    ChinaPost("China Post"),
    KoreaPost("Korea Post"),
    QRCode("QR Code"),
    Matrix("Data Matrix"),
    MaxiCode("MaxiCode"),
    AztecCode("Aztec Code"),
    OCR("OCR");

    private String name;

    private Scan2dSymbolOption(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
