package com.example.duckcards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuestionAdapter(
    private val onLongClick: (QuestionModelClass) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.VH>() {

    private val data = ArrayList<QuestionModelClass>()

    fun submit(items: List<QuestionModelClass>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_question_card, parent, false)
        return VH(v, onLongClick)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(data[position])
    }

    class VH(itemView: View, val onLongClick: (QuestionModelClass) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.questionName)
        private val answer = itemView.findViewById<TextView>(R.id.questionAnswer)
        private var current: QuestionModelClass? = null

        init {
            itemView.setOnLongClickListener { current?.let(onLongClick); true }
        }

        fun bind(item: QuestionModelClass) {
            current = item
            name.text = item.questionName
            answer.text = item.questionAnswer
        }
    }
}
