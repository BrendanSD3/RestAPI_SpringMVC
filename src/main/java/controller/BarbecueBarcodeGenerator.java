/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author brend
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

public class BarbecueBarcodeGenerator {

    private static final Font BARCODE_TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);

    public static BufferedImage generateUPCABarcodeImage(String barcodeText) throws Exception {
        Barcode barcode = BarcodeFactory.createUPCA(barcodeText); //checksum is automatically added
        barcode.setFont(BARCODE_TEXT_FONT);
        barcode.setResolution(400);

        return BarcodeImageHandler.getImage(barcode);
    }

    public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
        Barcode barcode = BarcodeFactory.createEAN13(barcodeText); //checksum is automatically added
        barcode.setFont(BARCODE_TEXT_FONT);

        return BarcodeImageHandler.getImage(barcode);
    }

    public static BufferedImage generateCode128BarcodeImage(String barcodeText) throws Exception {
        Barcode barcode = BarcodeFactory.createCode128(barcodeText);
        barcode.setFont(BARCODE_TEXT_FONT);

        return BarcodeImageHandler.getImage(barcode);
    }

    public static BufferedImage generatePDF417BarcodeImage(String barcodeText) throws Exception {
        Barcode barcode = BarcodeFactory.createPDF417(barcodeText);
        barcode.setFont(BARCODE_TEXT_FONT);

        return BarcodeImageHandler.getImage(barcode);
    }

}