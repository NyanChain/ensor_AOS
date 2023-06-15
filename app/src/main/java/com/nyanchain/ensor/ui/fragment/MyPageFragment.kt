package com.nyanchain.ensor.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.nyanchain.ensor.base.BaseFragment
import com.nyanchain.ensor.data.network.APIs
import com.nyanchain.ensor.databinding.FragmentMyPageBinding
import com.nyanchain.ensor.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyPageFragment : BaseFragment<FragmentMyPageBinding>() {

    private lateinit var retService: APIs

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyPageBinding {
        return FragmentMyPageBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)

        // Fetch data
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    retService.myPage()
                }
                if (response.isSuccessful) {
                    val myPageResponse = response.body()
                    if (myPageResponse != null) {
                        // Update UI with myPageResponse data
                        binding.tvEmail.text = myPageResponse.email
                        binding.tvNickname.text = myPageResponse.nickname
                        Log.d("MyPageFragment 통신 성공", "Response: ${myPageResponse.toString()}")
                    }
                } else {
                    Log.d("MyPageFragment 통신 요청 실패", "Response not successful: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("MyPageFragment 통신 실패", "Exception: ${e.message.toString()}")
            }
        }
    }
}