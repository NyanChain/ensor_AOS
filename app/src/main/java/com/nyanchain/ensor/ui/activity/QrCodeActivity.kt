package com.nyanchain.ensor.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.nyanchain.ensor.GlobalApplication
import com.nyanchain.ensor.R
import com.nyanchain.ensor.data.network.APIs
import com.nyanchain.ensor.retrofit.RetrofitClient
import com.nyanchain.ensor.ui.fragment.FailFragment
import com.nyanchain.ensor.ui.fragment.SuccessFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QrCodeActivity : AppCompatActivity() {

    private lateinit var retService: APIs
    private var resultText: String? = null
    private var validity: Boolean? = null
    private var productId: String? = null
    private var productName: String? = null

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }

    private lateinit var scannerView: CompoundBarcodeView

    private val barcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            // QR 코드 스캔 결과 처리
            resultText = result.text
            Log.d("ScanQRCodeActivity", "QR Code Result: $resultText")
            val(productId, productName) = resultText?.split(",")?.map { it.trim() } ?: listOf("", "")

            Log.d("ScanQRCodeActivity", "QR Code Result: $resultText")
            Log.d("ScanQRCodeActivity", "QR Code Result productName: $productName, productId: $productId")
            GlobalApplication.prefs.setString("productName", productName)
            GlobalApplication.prefs.setString("productId", productId)

            // QR 결과값 서버 post
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val response = retService.postQr(APIs.QrRequest(productId))
                        if (response.isSuccessful) {
                            Log.d("ScanQRCodeActivity 통신 성공", "QR Code Result: $response")
                            // Todo: 아래 코드는 response 형식 QrResponse로 변경하면 살리기
//                            validity = response.body()?.tfresult
//                            if (validity == true) {
//                                val successText = "success"
//                                GlobalApplication.prefs.setString("censorText", response.body()?.censorText.toString())
//                                GlobalApplication.prefs.setString("censorCom", response.body()?.censorCom.toString())
//                                GlobalApplication.prefs.setString("imgUrl", response.body()?.imgUrl.toString())

                                val mainActivity = Intent(this@QrCodeActivity, MainActivity::class.java)
                                //mainActivity.putExtra("fragment", successText)
                                startActivity(mainActivity)
                                finish()
//                            } else {
//                                val failText = "fail"
//                                val mainActivity = Intent(this@QrCodeActivity, MainActivity::class.java)
//                                mainActivity.putExtra("fragment", failText)
//                                startActivity(mainActivity)
//                                finish()
//                            }
//
                        } else {
                            Log.d("ScanQRCodeActivity 통신 요청 실패", "QR Code Result: $response")
                        }
                    } catch (e: Exception) {
                        Log.d("ScanQRCodeActivity 통신 실패", "QR Code Result: $e")
                    }
                }
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

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)


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
