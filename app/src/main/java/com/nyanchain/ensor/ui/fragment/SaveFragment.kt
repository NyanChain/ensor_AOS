package com.nyanchain.ensor.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nyanchain.ensor.R
import com.nyanchain.ensor.base.BaseFragment
import com.nyanchain.ensor.data.network.APIs
import com.nyanchain.ensor.databinding.FragmentSaveBinding
import com.nyanchain.ensor.ui.adapter.SaveRecyclerViewAdapter
import com.nyanchain.ensor.ui.viewmodel.SaveViewModel

class SaveFragment : BaseFragment<FragmentSaveBinding>()  {

    private lateinit var retService: APIs
    private var isClicked = false
    private lateinit var saveViewModel: SaveViewModel
    private lateinit var saveRecyclerViewAdapter: SaveRecyclerViewAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSaveBinding {
        return FragmentSaveBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()


    }

    private fun initRecyclerView() {
        // ViewModel 초기화
        saveViewModel = ViewModelProvider(this).get(SaveViewModel::class.java)
        saveRecyclerViewAdapter = SaveRecyclerViewAdapter { task ->
            //click event 처리

        }

        // recyclerview 구성
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = saveRecyclerViewAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // ViewModel과 RecyclerView 어댑터 연결
        saveViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            saveRecyclerViewAdapter.updateTasks(tasks)
        })
    }


}