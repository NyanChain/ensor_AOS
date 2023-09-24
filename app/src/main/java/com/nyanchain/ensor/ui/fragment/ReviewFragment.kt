package com.nyanchain.ensor.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nyanchain.ensor.GlobalApplication
import com.nyanchain.ensor.R
import com.nyanchain.ensor.base.BaseFragment
import com.nyanchain.ensor.data.network.APIs
import com.nyanchain.ensor.databinding.FragmentReviewBinding
import com.nyanchain.ensor.retrofit.RetrofitClient
import com.nyanchain.ensor.ui.adapter.ReviewRecyclerViewAdapter
import com.nyanchain.ensor.ui.viewmodel.ReviewViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ReviewFragment : BaseFragment<FragmentReviewBinding>()  {

    private lateinit var retService: APIs
    private var review: String = ""
    private lateinit var reviewViewModel: ReviewViewModel
    private lateinit var reviewRecyclerViewAdapter: ReviewRecyclerViewAdapter
    private var rating : Int =0
    private val product = GlobalApplication.prefs.getString("qrCodeData","")

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReviewBinding{
        return FragmentReviewBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)

        binding.star1.setOnClickListener {
            rating = 1
            binding.star1.setImageResource(R.drawable.ic_star_on)
            binding.star2.setImageResource(R.drawable.ic_star_off)
            binding.star3.setImageResource(R.drawable.ic_star_off)
            binding.star4.setImageResource(R.drawable.ic_star_off)
            binding.star5.setImageResource(R.drawable.ic_star_off)
        }
        binding.star2.setOnClickListener {
            rating = 2
            binding.star1.setImageResource(R.drawable.ic_star_on)
            binding.star2.setImageResource(R.drawable.ic_star_on)
            binding.star3.setImageResource(R.drawable.ic_star_off)
            binding.star4.setImageResource(R.drawable.ic_star_off)
            binding.star5.setImageResource(R.drawable.ic_star_off)
        }
        binding.star3.setOnClickListener {
            rating = 3
            binding.star1.setImageResource(R.drawable.ic_star_on)
            binding.star2.setImageResource(R.drawable.ic_star_on)
            binding.star3.setImageResource(R.drawable.ic_star_on)
            binding.star4.setImageResource(R.drawable.ic_star_off)
            binding.star5.setImageResource(R.drawable.ic_star_off)
        }
        binding.star4.setOnClickListener {
            rating = 4
            binding.star1.setImageResource(R.drawable.ic_star_on)
            binding.star2.setImageResource(R.drawable.ic_star_on)
            binding.star3.setImageResource(R.drawable.ic_star_on)
            binding.star4.setImageResource(R.drawable.ic_star_on)
            binding.star5.setImageResource(R.drawable.ic_star_off)
        }
        binding.star5.setOnClickListener {
            rating = 5
            binding.star1.setImageResource(R.drawable.ic_star_on)
            binding.star2.setImageResource(R.drawable.ic_star_on)
            binding.star3.setImageResource(R.drawable.ic_star_on)
            binding.star4.setImageResource(R.drawable.ic_star_on)
            binding.star5.setImageResource(R.drawable.ic_star_on)
        }


        initRecyclerView()

        binding.btnSubmit.setOnClickListener {
            review = binding.edtReview.text.toString()
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    try { //Todo: 수정해야됨!!!!
                        val response = retService.postWrite(APIs.WriteRequest(product, rating, review))
                        if (response.isSuccessful) {
                            val jsonResponse = JSONObject(response.body().toString())
                            val code = jsonResponse.getInt("code")
                            val message = jsonResponse.getString("message")
                            activity?.runOnUiThread {
                                Toast.makeText(activity?.applicationContext, "$message", Toast.LENGTH_SHORT).show()
                            }
                            Log.d("ReviewFragment 통신 성공", "Result: $response")

                        } else {
                            Log.d("ReviewFragment 통신 요청 실패", "Result: $response")
                        }
                    } catch (e: Exception) {
                        Log.d("ReviewFragment 통신 실패", "Result: $e")
                    }
                }
            }

        }

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