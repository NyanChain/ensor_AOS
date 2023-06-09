package com.nyanchain.ensor.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nyanchain.ensor.base.BaseFragment
import com.nyanchain.ensor.databinding.FragmentQrBinding
import com.nyanchain.ensor.ui.activity.QrCodeActivity


class QrFragment : BaseFragment<FragmentQrBinding>()  {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentQrBinding {
        return FragmentQrBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGoQr.setOnClickListener {
            // QR 코드 스캔 화면으로 이동
            val intent = Intent(requireContext(), QrCodeActivity::class.java)
            startActivity(intent)
        }

    }

}