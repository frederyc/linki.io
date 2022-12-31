package com.example.linky.backend.services;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

public class QRCodeService {
    private static QRCodeService instance = null;
    private final MultiFormatWriter writer;
    private final BarcodeEncoder encoder;

    private QRCodeService() {
        writer = new MultiFormatWriter();
        encoder = new BarcodeEncoder();
    }

    public static QRCodeService getInstance() {
        if (instance == null)
            instance = new QRCodeService();
        return instance;
    }

    /**
     * Generates a QR with a specific text
     */
    public byte[] generateQR(String text) throws WriterException {
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512);
        Bitmap bitmap = Bitmap.createBitmap(
                encoder.createBitmap(matrix),
                50,
                50,
                412,
                412
        );
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}
