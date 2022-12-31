package com.example.linky;

import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.ScanMode;
import com.example.linky.backend.cache.UserDataCache;
import com.example.linky.backend.services.UserDataService;
import com.example.linky.databinding.ActivityQrScannerBinding;
import com.example.linky.ui.dialogs.LoadingScreen;
import com.google.zxing.BarcodeFormat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class QrScannerActivity extends AppCompatActivity {
    private ActivityQrScannerBinding binding;
    private CodeScanner codeScanner;
    private UserDataService userDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userDataService = UserDataService.getInstance();

        codeScanner = new CodeScanner(this, binding.QRSCodeScanner);

        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(List.of(BarcodeFormat.QR_CODE));
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(false);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);

        boolean singleRun = false;

        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            if (result.getText().length() != 28)
                Toast.makeText(
                        QrScannerActivity.this,
                        "QR is invalid",
                        Toast.LENGTH_SHORT
                ).show();
            else {
                LoadingScreen ls = new LoadingScreen(
                        QrScannerActivity.this,
                        R.layout.loading_screen
                );
                ls.start();

                userDataService.addConnection(result.getText(), (Object... objects) -> {
                    if (!UserDataCache.getConnections().contains(result.getText()))
                        UserDataCache.getConnections().addFirst(result.getText());
                    ls.stop();
                    finish();
                    Toast.makeText(
                            QrScannerActivity.this,
                            "Connection added successfully",
                            Toast.LENGTH_SHORT
                    ).show();
                    codeScanner.setScanMode(ScanMode.CONTINUOUS);
                }, (Object... objects) -> {
                    ls.stop();
                    Toast.makeText(
                            QrScannerActivity.this,
                            "Failed to add connection",
                            Toast.LENGTH_SHORT
                    ).show();
                });
            }
        }));
        codeScanner.setErrorCallback(result -> runOnUiThread(() -> {
            Log.e("QR_ACTIVITY", result.getMessage());
            result.printStackTrace();
            Toast.makeText(
                    QrScannerActivity.this,
                    "Failed to initialize camera",
                    Toast.LENGTH_SHORT).show();
        }));
        binding.QRSCodeScanner.setOnClickListener(view -> codeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}