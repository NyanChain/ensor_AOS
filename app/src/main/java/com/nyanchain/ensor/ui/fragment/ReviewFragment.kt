package com.nyanchain.ensor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nyanchain.ensor.base.BaseFragment
import com.nyanchain.ensor.databinding.FragmentReviewBinding

class ReviewFragment : BaseFragment<FragmentReviewBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReviewBinding {
        return FragmentReviewBinding.inflate(inflater, container, false)
    }

}