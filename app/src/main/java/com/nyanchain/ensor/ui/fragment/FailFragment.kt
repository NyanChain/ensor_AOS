package com.nyanchain.ensor.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nyanchain.ensor.R
import com.nyanchain.ensor.base.BaseFragment
import com.nyanchain.ensor.databinding.FragmentFailBinding
import com.nyanchain.ensor.databinding.FragmentHomeBinding
import com.nyanchain.ensor.ui.activity.QrCodeActivity


class FailFragment : BaseFragment<FragmentFailBinding>()  {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFailBinding {
        return FragmentFailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authenticationImage.setOnClickListener {
            // QR 코드 스캔 화면으로 이동
            val intent = Intent(requireContext(), QrCodeActivity::class.java)
            startActivity(intent)
        }


    }

}