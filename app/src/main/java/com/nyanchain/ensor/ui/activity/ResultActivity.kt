package com.nyanchain.ensor.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nyanchain.ensor.R
import com.nyanchain.ensor.databinding.ActivityMainBinding
import com.nyanchain.ensor.databinding.ActivityResultBinding
import com.nyanchain.ensor.ui.fragment.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
    }

    class MainActivity : AppCompatActivity() {
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

            binding.mainBnv.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.homeFragment -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, HomeFragment())
                            .commitAllowingStateLoss()
                        true
                    }
                    R.id.qrFragment -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, QrFragment())
                            .commitAllowingStateLoss()
                        true
                    }
                    R.id.mypageFragment -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, MyPageFragment())
                            .commitAllowingStateLoss()
                        true
                    }
                    else -> false
                }
            }
        }
    }
}