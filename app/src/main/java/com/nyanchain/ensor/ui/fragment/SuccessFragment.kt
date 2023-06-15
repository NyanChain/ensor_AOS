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

class SuccessFragment : BaseFragment<FragmentSuccessBinding>()  {

    private lateinit var retService: APIs
    private var productName =GlobalApplication.prefs.getString("productName", "=")
    private var productId =GlobalApplication.prefs.getString("productId", "")
    private var censorText = GlobalApplication.prefs.getString("censorText", "")
    private var censorCom = GlobalApplication.prefs.getString("censorCom", "")
    private var imgUrl =  GlobalApplication.prefs.getString("imgUrl", "")
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

        binding.productName.text = productName
        binding.authenticationCertification.text = censorCom
        binding.authenticationContext.text = censorText


        if(imgUrl != ""){
            Glide.with(view)
                .load(imgUrl)
                .into(binding.authenticationImage)
        }

        binding.btnSave.setOnClickListener{
            // QR 결과값 서버 post
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val response = retService.postSave(APIs.SaveRequest(productId, productName))
                        if (response.isSuccessful) {
                            Log.d("SuccessFragment 통신 성공", "Result: $response")
                            Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()

                        } else {
                            Log.d("SuccessFragment 통신 요청 실패", "Result: $response")
                        }
                    } catch (e: Exception) {
                        Log.d("SuccessFragment 통신 실패", "Result: $e")
                    }
                }
            }
        }


    }
}