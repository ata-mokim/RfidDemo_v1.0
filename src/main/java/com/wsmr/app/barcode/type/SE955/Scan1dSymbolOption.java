package com.wsmr.app.barcode.type.SE955;

public enum Scan1dSymbolOption {
    UpcEan("UPC/EAN"),
    Code39("Code 39"),
    Code93("Code 93"),
    Code11("Code 11"),
    I2of5("Interleaved 2 of 5"),
    D2of5("Discrete 2 of 5"),
    Codabar("Codabar"),
    QRCode("QR Code"),
    Msi("MSI");

    private String name;

    private Scan1dSymbolOption(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
