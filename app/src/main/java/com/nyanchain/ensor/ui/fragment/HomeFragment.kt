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
import com.nyanchain.ensor.databinding.FragmentHomeBinding
import com.nyanchain.ensor.ui.adapter.EnvironmentRecyclerViewAdapter
import com.nyanchain.ensor.ui.adapter.VeganRecyclerViewAdapter
import com.nyanchain.ensor.ui.viewmodel.EnvironmentViewModel
import com.nyanchain.ensor.ui.viewmodel.VeganViewModel


class HomeFragment : BaseFragment<FragmentHomeBinding>()  {

    private lateinit var retService: APIs
    private var isClicked = false
    private lateinit var veganViewModel: VeganViewModel
    private lateinit var veganRecyclerViewAdapter: VeganRecyclerViewAdapter
    private lateinit var environmentViewModel: EnvironmentViewModel
    private lateinit var environmentRecyclerViewAdapter: EnvironmentRecyclerViewAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEnvironmentRecyclerView()

        binding.btnInformation.setOnClickListener {
            if(!isClicked) {
                binding.btnInformation.setImageResource(R.drawable.btn_vegan)
                isClicked = true
                initVeganRecyclerView()

            } else {
                binding.btnInformation.setImageResource(R.drawable.btn_environment)
                isClicked = false
                initEnvironmentRecyclerView()

            }
        }




    }

    private fun initVeganRecyclerView() {
        // ViewModel 초기화
        veganViewModel = ViewModelProvider(this).get(VeganViewModel::class.java)
        veganRecyclerViewAdapter = VeganRecyclerViewAdapter { task ->
            //click event 처리

        }

        // recyclerview 구성
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = veganRecyclerViewAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // ViewModel과 RecyclerView 어댑터 연결
        veganViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            veganRecyclerViewAdapter.updateTasks(tasks)
        })
    }

    private fun initEnvironmentRecyclerView() {
        // ViewModel 초기화
        environmentViewModel = ViewModelProvider(this).get(EnvironmentViewModel::class.java)
        environmentRecyclerViewAdapter = EnvironmentRecyclerViewAdapter { task ->
            //click event 처리

        }

        // recyclerview 구성
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = environmentRecyclerViewAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // ViewModel과 RecyclerView 어댑터 연결
        environmentViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            environmentRecyclerViewAdapter.updateTasks(tasks)
        })
    }

}