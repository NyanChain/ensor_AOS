package com.nyanchain.ensor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.nyanchain.ensor.R
import com.nyanchain.ensor.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {
    lateinit var binding: ActivityFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val keyHash = Utility.getKeyHash(this)

        KakaoSdk.init(this, this.getString(R.string.kakao_app_key))


        binding.btnStartKakaoLogin.setOnClickListener {
            kakaoLogin()
        }
//        binding.btnStartKakaoLogout.setOnClickListener {
//            kakaoLogout()
//        }
//
//        binding.btnStartKakaoUnlink.setOnClickListener {
//            kakaoUnlink()
//        }
    }

    private fun kakaoLogin() {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                //TextMsg(this, "카카오계정으로 로그인 실패 : ${error}")
                setLogin(false)
            } else if (token != null) {
                //TODO: 최종적으로 카카오로그 인 및 유저정보 가져온 결과
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        TextMsg(
                            this, "카카오계정으로 로그인 성공 \n\n " +
                                    "token: ${token.accessToken} \n\n " +
                                    "me: ${user}"
                        )
                        setLogin(true)
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("nickname", "다희")
                        intent.putExtra("email", "ekgmljeong@naver.com")
                        startActivity(intent)
                        finish()
                    } else if (user != null) {
                        setLogin(true)
                        val intent = Intent(this, MainActivity::class.java)
//                         닉네임과 이메일 정보 가져오기
                        val nickname = user.kakaoAccount?.profile?.nickname.toString()
                        val email = user.kakaoAccount?.email.toString()
//                        intent.putExtra("nickname", nickname)
////                        intent.putExtra("email", email)
//                        intent.putExtra("nickname", "다희")
//                        intent.putExtra("email", "ekgmljeong@naver.com")
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    TextMsg(this, "카카오톡으로 로그인 실패 : ${error}")

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    TextMsg(this, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    setLogin(true)
                    val intent = Intent(this, MainActivity::class.java)
//                    val nickname = user.kakaoAccount?.profile?.nickname
//                    val email = user.kakaoAccount?.email
                    intent.putExtra("nickname", "다희")
                    intent.putExtra("email", "ekgmljeong@naver.com")
                    startActivity(intent)
                    //finish()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

//    private fun kakaoLogout(){
//        // 로그아웃
//        UserApiClient.instance.logout { error ->
//            if (error != null) {
//                TextMsg(this, "로그아웃 실패. SDK에서 토큰 삭제됨: ${error}")
//            }
//            else {
//                TextMsg(this, "로그아웃 성공. SDK에서 토큰 삭제됨")
//                setLogin(false)
//            }
//        }
//    }
//
//    private fun kakaoUnlink(){
//        // 연결 끊기
//        UserApiClient.instance.unlink { error ->
//            if (error != null) {
//                TextMsg(this, "연결 끊기 실패: ${error}")
//            }
//            else {
//                TextMsg(this, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
//                setLogin(false)
//            }
//        }
//    }

    private fun TextMsg(act: Activity, msg : String){
        binding.tvHashKey.text = msg
    }

    private fun setLogin(bool: Boolean){
        binding.btnStartKakaoLogin.visibility = if(bool) View.GONE else View.VISIBLE
//        binding.btnStartKakaoLogout.visibility = if(bool) View.VISIBLE else View.GONE
//        binding.btnStartKakaoUnlink.visibility = if(bool) View.VISIBLE else View.GONE
    }
}
