package com.nyanchain.ensor.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nyanchain.ensor.R
import com.nyanchain.ensor.databinding.ActivityMainBinding
import com.nyanchain.ensor.databinding.ActivityResultBinding
import com.nyanchain.ensor.ui.fragment.*

class ResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = intent.getStringExtra("fragment").toString()
        Log.d("fragment", fragment.toString())
        if (fragment != null) {
            if (fragment == "success") {
                // Success 처리
                val successFragment = SuccessFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, successFragment)
                    .commit()
            } else if (fragment == "fail") {
                // Fail 처리
                val failFragment = FailFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, failFragment)
                    .commit()
            }
        }

        initBottomNavigation()
    }


    private fun initBottomNavigation() {

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.main_frm, QrFragment())
//            .commitAllowingStateLoss()


        binding.mainBnv.selectedItemId = R.id.qrFragment
        //Todo: 초기화면에서 ..... 탭 클릭하지 않아도.. 바로 화면 뜨게 !!
        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.qrFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, QrFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.mypageFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MyPageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}