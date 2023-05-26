package com.nyanchain.ensor.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.nyanchain.ensor.R
import com.nyanchain.ensor.ui.fragment.FailFragment
import com.nyanchain.ensor.ui.fragment.SuccessFragment

class QrCodeActivity : AppCompatActivity() {
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }

    private lateinit var scannerView: CompoundBarcodeView

    private val barcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            // QR 코드 스캔 결과 처리
            val resultText = result.text
            Log.d("ScanQRCodeActivity", "QR Code Result: $resultText")

            if (resultText == "true") {
                val successText = "success"
                val mainActivity = Intent(this@QrCodeActivity, MainActivity::class.java)
                mainActivity.putExtra("fragment", successText)
                startActivity(mainActivity)
                finish()
            } else {
                val failText = "fail"
                val mainActivity = Intent(this@QrCodeActivity, MainActivity::class.java)
                mainActivity.putExtra("fragment", failText)
                startActivity(mainActivity)
                finish()
            }

            // 필요한 처리를 수행한 후, 다시 스캔을 진행하려면 아래 코드를 호출합니다.
            scannerView.resume()
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
            // 필요한 처리를 수행할 수 있습니다.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)

        scannerView = findViewById(R.id.scannerView)

        if (!hasCameraPermission()) {
            requestCameraPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        scannerView.resume()
        scannerView.decodeContinuous(barcodeCallback)
    }

    override fun onPause() {
        super.onPause()
        scannerView.pause()
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 카메라 권한이 허용되었을 때 처리
                scannerView.resume()
                scannerView.decodeContinuous(barcodeCallback)
            } else {
                // 카메라 권한이 거부되었을 때 처리
            }
        }
    }
}
