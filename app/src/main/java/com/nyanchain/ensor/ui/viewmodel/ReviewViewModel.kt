package com.nyanchain.ensor.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nyanchain.ensor.GlobalApplication
import com.nyanchain.ensor.data.model.response.ReviewResponse
import com.nyanchain.ensor.data.network.APIs
import com.nyanchain.ensor.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class ReviewViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var retService: APIs
    private val _tasks = MutableLiveData<List<ReviewResponse>>()
    val tasks: LiveData<List<ReviewResponse>> = _tasks
    val product = GlobalApplication.prefs.getString("qrCodeData","")

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)

        viewModelScope.launch {
            try {
                val response = retService.getReview(product)
                if (response.isSuccessful) {
                    _tasks.value = response.body()
                    Log.d("ReviewViewModel API Success", "fetchTasks: ${response.body()}")
                } else {
                    Log.d("ReviewViewModel API Fail1", "fetchTasks: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("ReviewViewModel API Fail2", "fetchTasks: ${e.message}")
            }
        }
    }
}
