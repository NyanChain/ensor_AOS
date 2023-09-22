package com.nyanchain.ensor.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nyanchain.ensor.Constants.REQUEST_CODE
import com.nyanchain.ensor.ui.fragment.HomeFragment
import com.nyanchain.ensor.ui.fragment.MyPageFragment
import com.nyanchain.ensor.ui.fragment.QrFragment
import com.nyanchain.ensor.R
import com.nyanchain.ensor.databinding.ActivityMainBinding
import com.nyanchain.ensor.ui.fragment.SuccessFragment
import com.nyanchain.ensor.ui.fragment.FailFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()
    }

    private fun initBottomNavigation() {

        binding.mainBnv.selectedItemId = R.id.qrFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, QrFragment())
            .commitAllowingStateLoss()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("onActivityResult", "requestCode: $requestCode, resultCode: $resultCode, data: $data")

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val fragment = data?.getStringExtra("fragment")
            when (fragment) {
                "success" -> {
                    val successFragment = SuccessFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, successFragment)
                        .commit()
                }
                "fail" -> {
                    val failFragment = FailFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, failFragment)
                        .commit()
                }
            }
        }
    }
}
