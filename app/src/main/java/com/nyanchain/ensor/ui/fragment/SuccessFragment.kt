package com.nyanchain.ensor.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.nyanchain.ensor.GlobalApplication
import com.nyanchain.ensor.R
import com.nyanchain.ensor.base.BaseFragment
import com.nyanchain.ensor.data.network.APIs
import com.nyanchain.ensor.databinding.FragmentSuccessBinding
import com.nyanchain.ensor.retrofit.RetrofitClient
import com.nyanchain.ensor.ui.activity.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SuccessFragment : BaseFragment<FragmentSuccessBinding>()  {

    private lateinit var retService: APIs
    private var productName =GlobalApplication.prefs.getString("productName", "=")
    private var productId =GlobalApplication.prefs.getString("productId", "")
    private var qrCodeData =GlobalApplication.prefs.getString("qrCodeData", "")
    private var censorText = GlobalApplication.prefs.getString("censorText", "")
    private var censorCom = GlobalApplication.prefs.getString("censorCom", "")
    private var imgUrl =  GlobalApplication.prefs.getString("imgUrl", "")

    private var star: Int = 0
    private var message: String = ""
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSuccessBinding {
        return FragmentSuccessBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = retService.getRate(qrCodeData) //Todo; 수정해야됨..
                    if (response.isSuccessful) {
                        val body = response.body()
                        GlobalApplication.prefs.setString("productName", "${body?.product}")
                        star = body?.rate!!
                        message = body?.message.toString()
                        setStar(star)
                        Log.d("SuccessFragment - getRate 통신 성공", "Result: $response")

                    } else {
                        Log.d("SuccessFragment - getRate 통신 요청 실패", "Result: $response")
                    }
                } catch (e: Exception) {
                    Log.d("SuccessFragment - getRate 통신 실패", "Result: $e")
                }
            }
        }

        binding.tvRate.text = star.toString()
        binding.tvMessage.text = message

        binding.productName.text = productName
        binding.authenticationCertification.text = censorCom
        binding.authenticationContext.text = censorText


        if(imgUrl != ""){
            Glide.with(view)
                .load(imgUrl)
                .into(binding.authenticationImage)
        }


        binding.btnReview.setOnClickListener {
            val reviewFragment = ReviewFragment() // 이동할 Fragment 객체를 생성
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.successLayout, reviewFragment)
            transaction.addToBackStack(null) // 이전 Fragment 스택에 추가 (뒤로 가기 버튼 사용 가능)
            transaction.commit()
        }

        binding.btnSave.setOnClickListener{
            // QR 결과값 서버 post
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val response = retService.postSave(APIs.SaveRequest(qrCodeData))
                        if (response.isSuccessful) {
                            val jsonResponse = JSONObject(response.body().toString())
                            val code = jsonResponse.getInt("code")
                            val message = jsonResponse.getString("message")
                            Log.d("SuccessFragment - postSave 통신 성공", "Result: $response")
                            activity?.runOnUiThread {
                                Toast.makeText(activity?.applicationContext, "저장 성공했습니다.", Toast.LENGTH_SHORT).show()
                            }
                            //Todo: Toast message 제대로 뜨게 확니하기 !!~!~!~!~

                        } else {
                            Log.d("SuccessFragment - postSave통신 요청 실패", "Result: $response")
                        }
                    } catch (e: Exception) {
                        Log.d("SuccessFragment - postSave 통신 실패", "Result: $e")
                    }
                }
            }
        }

    }
    private fun setStar(star: Int) {
        if (star == 5) {
            binding.ivStart1.setImageResource(R.drawable.star_filled)
            binding.ivStart2.setImageResource(R.drawable.star_filled)
            binding.ivStart3.setImageResource(R.drawable.star_filled)
            binding.ivStart4.setImageResource(R.drawable.star_filled)
            binding.ivStart5.setImageResource(R.drawable.star_filled)
        }
        else if (star == 4) {
            binding.ivStart1.setImageResource(R.drawable.star_filled)
            binding.ivStart2.setImageResource(R.drawable.star_filled)
            binding.ivStart3.setImageResource(R.drawable.star_filled)
            binding.ivStart4.setImageResource(R.drawable.star_filled)
            binding.ivStart5.setImageResource(R.drawable.star_unfilled)
        }
        else if (star == 3) {
            binding.ivStart1.setImageResource(R.drawable.star_filled)
            binding.ivStart2.setImageResource(R.drawable.star_filled)
            binding.ivStart3.setImageResource(R.drawable.star_filled)
            binding.ivStart4.setImageResource(R.drawable.star_unfilled)
            binding.ivStart5.setImageResource(R.drawable.star_unfilled)
        }
        else if (star == 2) {
            binding.ivStart1.setImageResource(R.drawable.star_filled)
            binding.ivStart2.setImageResource(R.drawable.star_filled)
            binding.ivStart3.setImageResource(R.drawable.star_unfilled)
            binding.ivStart4.setImageResource(R.drawable.star_unfilled)
            binding.ivStart5.setImageResource(R.drawable.star_unfilled)
        }
        else if (star == 1) {
            binding.ivStart1.setImageResource(R.drawable.star_filled)
            binding.ivStart2.setImageResource(R.drawable.star_unfilled)
            binding.ivStart3.setImageResource(R.drawable.star_unfilled)
            binding.ivStart4.setImageResource(R.drawable.star_unfilled)
            binding.ivStart5.setImageResource(R.drawable.star_unfilled)
        }
        else if (star == 0) {
            binding.ivStart1.setImageResource(R.drawable.star_unfilled)
            binding.ivStart2.setImageResource(R.drawable.star_unfilled)
            binding.ivStart3.setImageResource(R.drawable.star_unfilled)
            binding.ivStart4.setImageResource(R.drawable.star_unfilled)
            binding.ivStart5.setImageResource(R.drawable.star_unfilled)
        }
    }
}