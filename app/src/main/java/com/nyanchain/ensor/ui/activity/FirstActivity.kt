package com.nyanchain.ensor.ui.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.nyanchain.ensor.R
import com.nyanchain.ensor.data.network.APIs
import com.nyanchain.ensor.databinding.ActivityFirstBinding
import com.nyanchain.ensor.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirstActivity : AppCompatActivity() {
    lateinit var binding: ActivityFirstBinding
    private lateinit var retService: APIs
    private var authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val keyHash = Utility.getKeyHash(this)

        KakaoSdk.init(this, this.getString(R.string.kakao_app_key))

        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)

        binding.btnStartKakaoLogin.setOnClickListener {
            kakaoLogin()
        }

        binding.btnStartKakaoLogout.setOnClickListener {
            kakaoLogout()
        }

        binding.btnStartKakaoUnlink.setOnClickListener {
            kakaoUnlink()
        }
    }


    private fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                TextMsg(this, "카카오계정으로 로그인 실패 : ${error}")
                setLogin(false)
            } else if (token != null) {
                lifecycleScope.launch {
                    try {
                        val user = getUserInfo()
                        if (user != null) {
                            TextMsg(
                                this@FirstActivity, "카카오계정으로 로그인 성공 \n\n " +
                                        "token: ${token.accessToken} \n\n " +
                                        "me: ${user}"
                            )
                            authToken = token.accessToken
                            setLogin(true)
                            signUp()
                        }
                    } catch (e: Exception) {
                        Log.d("로그인 실패", e.toString())
                    }
                }
            }
        }
    }

        // Suspend function to get user info
    private suspend fun getUserInfo() = withContext(Dispatchers.IO) {
        var userInfo: User? = null
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.d(ContentValues.TAG, "사용자 정보 요청 실패", error)
            } else {
                userInfo = user
            }
        }
        userInfo
        }

    private fun kakaoLogout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                TextMsg(this, "로그아웃 실패. SDK에서 토큰 삭제됨: ${error}")
            } else {
                TextMsg(this, "로그아웃 성공. SDK에서 토큰 삭제됨")
                setLogin(false)
            }
        }
    }

    private fun kakaoUnlink() {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                TextMsg(this, "연결 끊기 실패: ${error}")
            } else {
                TextMsg(this, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                setLogin(false)
            }
        }
    }

    private fun TextMsg(act: Activity, msg : String) {
        binding.tvHashKey.text = msg
    }

    private fun setLogin(bool: Boolean) {
        binding.btnStartKakaoLogin.visibility = if (bool) View.GONE else View.VISIBLE
        binding.btnStartKakaoLogout.visibility = if (bool) View.VISIBLE else View.GONE
        binding.btnStartKakaoUnlink.visibility = if (bool) View.VISIBLE else View.GONE
    }

    suspend fun signUp() {
        try {
            val response = retService.signUp(APIs.RequestSignup(accessToken = authToken!!))
            if (response.isSuccessful) {
                val receivedLoginItem = response.body()
                val intent = Intent(this@FirstActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                Log.d("로그인 통신 요청 실패", response.toString())
                Log.d("로그인 통신 요청 실패", response.body().toString())
            }
        } catch (e: Exception) {
            Log.d("로그인 통신 실패", e.toString())
        }
    }
}
