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
import org.json.JSONObject

class QrCodeActivity : AppCompatActivity() {

    private lateinit var retService: APIs
    private var resultText: String? = null
    private var validity: String? = null
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
//            val(productId, productName) = resultText?.split(",")?.map { it.trim() } ?: listOf("", "")
//            Log.d("ScanQRCodeActivity", "QR Code Result productName: $productName, productId: $productId")
            GlobalApplication.prefs.setString("productName", resultText.toString().substring(5))
            GlobalApplication.prefs.setString("productId", resultText.toString().substring(0,5))
            GlobalApplication.prefs.setString("qrCodeData", resultText.toString())

            // QR 결과값 서버 post
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val response = retService.postQr(APIs.QrRequest(resultText.toString()))
                        if (response.isSuccessful) {
                            Log.d("ScanQRCodeActivity 통신 성공", "QR Code Result: ${response.body()}")
    
                            val jsonObject = JSONObject(response.body().toString())
                            val resultsArray = jsonObject.getJSONArray("results")
                            Log.d("ScanQRCodeActivity resultArray", "$resultsArray")


                            validity = jsonObject.get("tfresult").toString()
                            if (validity == "success") {
                                val successText = "success"
                                val firstResultObject = resultsArray.getJSONObject(0)
                                val censorText =
//                                val data = response.body()?.getAsJsonElement("data")
                                    GlobalApplication.prefs.setString("censorText", firstResultObject.getString("censorText"))
                                GlobalApplication.prefs.setString("censorCom", firstResultObject.getString("censorCom"))
                                GlobalApplication.prefs.setString("imgUrl", firstResultObject.getString("imgUrl"))

                                val resultActivity = Intent(this@QrCodeActivity, ResultActivity::class.java)
                                resultActivity.putExtra("fragment", successText)
                                startActivity(resultActivity)
                                finish()
                            } else {
                                val failText = "fail"
                                val resultActivity = Intent(this@QrCodeActivity, ResultActivity::class.java)
                                resultActivity.putExtra("fragment", failText)
                                startActivity(resultActivity)
                                finish()
                            }

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
