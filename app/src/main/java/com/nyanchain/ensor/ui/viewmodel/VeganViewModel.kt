package com.nyanchain.ensor.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nyanchain.ensor.data.model.response.VeganResponse
import com.nyanchain.ensor.data.network.APIs
import com.nyanchain.ensor.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class VeganViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var retService: APIs
    private val _tasks = MutableLiveData<List<VeganResponse>>()
    val tasks: LiveData<List<VeganResponse>> = _tasks

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)

        viewModelScope.launch {
            try {
                val response = retService.getVegan()
                if (response.isSuccessful) {
                    _tasks.value = response.body()
                    Log.d("VeganViewModel API Success", "fetchTasks: ${response.body()}")
                } else {
                    Log.d("VeganViewModel API Fail1", "fetchTasks: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("VeganViewModel API Fail2", "fetchTasks: ${e.message}")
            }
        }
    }
}
