package com.nyanchain.ensor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nyanchain.ensor.base.BaseFragment
import com.nyanchain.ensor.data.network.APIs
import com.nyanchain.ensor.databinding.FragmentReviewBinding
import com.nyanchain.ensor.ui.adapter.ReviewRecyclerViewAdapter
import com.nyanchain.ensor.ui.viewmodel.ReviewViewModel

class ReviewFragment : BaseFragment<FragmentReviewBinding>()  {

    private lateinit var retService: APIs
    private var isClicked = false
    private lateinit var reviewViewModel: ReviewViewModel
    private lateinit var reviewRecyclerViewAdapter: ReviewRecyclerViewAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReviewBinding{
        return FragmentReviewBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()


    }

    private fun initRecyclerView() {
        // ViewModel 초기화
        reviewViewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        reviewRecyclerViewAdapter = ReviewRecyclerViewAdapter { task ->
            //click event 처리

        }

        // recyclerview 구성
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = reviewRecyclerViewAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // ViewModel과 RecyclerView 어댑터 연결
        reviewViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            reviewRecyclerViewAdapter.updateTasks(tasks)
        })
    }


}