package com.nyanchain.ensor.ui.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.nyanchain.ensor.ui.fragment.HomeFragment
import com.nyanchain.ensor.ui.fragment.MyPageFragment
import com.nyanchain.ensor.ui.fragment.QrFragment
import com.nyanchain.ensor.R
import com.nyanchain.ensor.databinding.ActivityMainBinding
import com.nyanchain.ensor.ui.fragment.*
import com.nyanchain.ensor.ui.fragment.SuccessFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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


        binding.mainBnv.selectedItemId = R.id.homeFragment
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