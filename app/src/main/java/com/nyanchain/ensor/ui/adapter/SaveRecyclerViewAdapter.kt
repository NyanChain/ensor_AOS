package com.nyanchain.ensor.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nyanchain.ensor.R
import com.nyanchain.ensor.data.model.response.MysaveResponse
import com.nyanchain.ensor.data.model.response.VeganResponse

class SaveRecyclerViewAdapter(
    private val tasks: MutableList<MysaveResponse> = mutableListOf(),
    private val clickListener: (MysaveResponse) -> Unit
) : RecyclerView.Adapter<SaveRecyclerViewAdapter.MyViewHolder>() {

    fun updateTasks(newTasks: List<MysaveResponse>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.save_item, parent, false)
        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
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
        private val productName: TextView = view.findViewById(R.id.tv_product_name)
        private val com: TextView = view.findViewById(R.id.tv_censor_com)
        private val text: TextView = view.findViewById(R.id.tv_context)
        private val image: ImageView = view.findViewById(R.id.iv_censor_image)

        var isClicked = false
//        private val layout: ConstraintLayout = view.findViewById(R.id.constraintLayout)


        fun bind(task: MysaveResponse, clickListener: (MysaveResponse) -> Unit) {

            productName.text = "제품 이름 : " + task.productName
            com.text = "인증 기관 : " + task.censorCom
            text.text = "인증 내용 : " + task.censorText

            if (task.imgUrl.isNotEmpty()) {
                Glide.with(view)
                    .load(task.imgUrl)
                    .into(image)
            }


            view.setOnClickListener {
                clickListener(task)
            }
        }
    }
}