package com.nyanchain.ensor.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyanchain.ensor.R
import com.nyanchain.ensor.data.model.response.ReviewResponse

class ReviewRecyclerViewAdapter(
    private val tasks: MutableList<ReviewResponse> = mutableListOf(),
    private val clickListener: (ReviewResponse) -> Unit
) : RecyclerView.Adapter<ReviewRecyclerViewAdapter.MyViewHolder>() {

    fun updateTasks(newTasks: List<ReviewResponse>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.save_item, parent, false)
        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ReviewRecyclerViewAdapter.MyViewHolder, position: Int) {
        if (tasks.isNotEmpty()) {
            val task = tasks[position]
            holder.bind(task, clickListener)
        }
    }



    override fun getItemCount(): Int {
        return tasks.size
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        // UI 요소에 대한 참조를 정의합니다.
        private val nickname: TextView = view.findViewById(R.id.tv_nickname)
        private val rate: TextView = view.findViewById(R.id.tv_rate)
        private val review: TextView = view.findViewById(R.id.tv_review)

        var isClicked = false
//        private val layout: ConstraintLayout = view.findViewById(R.id.constraintLayout)


        fun bind(task: ReviewResponse, clickListener: (ReviewResponse) -> Unit) {

            nickname.text = "닉네임 : " + task.nickname
            rate.text = "별점 : " + task.rate
            review.text = "리뷰 : " + task.review



            view.setOnClickListener {
                clickListener(task)
            }
        }
    }
}