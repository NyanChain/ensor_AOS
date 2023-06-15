package com.nyanchain.ensor.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nyanchain.ensor.data.model.response.MysaveResponse
import com.nyanchain.ensor.data.model.response.SaveResponse
import com.nyanchain.ensor.data.model.response.VeganResponse
import com.nyanchain.ensor.data.network.APIs
import com.nyanchain.ensor.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class SaveViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var retService: APIs
    private val _tasks = MutableLiveData<List<MysaveResponse>>()
    val tasks: LiveData<List<MysaveResponse>> = _tasks

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)

        viewModelScope.launch {
            try {
                val response = retService.getSave()
                if (response.isSuccessful) {
                    _tasks.value = response.body()
                    Log.d("SaveViewModel API Success", "fetchTasks: ${response.body()}")
                } else {
                    Log.d("SaveViewModel API Fail1", "fetchTasks: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("SaveViewModel API Fail2", "fetchTasks: ${e.message}")
            }
        }
    }
}
