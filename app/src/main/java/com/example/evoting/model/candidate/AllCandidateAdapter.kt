package com.example.evoting.model.candidate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.evoting.R

class AllCandidateAdapter (private val allCandidate: ArrayList<DataItemAllCandidate>):
        RecyclerView.Adapter<AllCandidateAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCandidate: ImageView = itemView.findViewById(R.id.iv_candidate)
        private val tvDetailCandidate: TextView = itemView.findViewById(R.id.tv_detail_candidate)
        private val btnAddVote: Button = itemView.findViewById(R.id.btn_add_vote)

        fun bind(candidate: DataItemAllCandidate) {
            tvDetailCandidate.text = candidate.name

        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_all_candidate, viewGroup, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allCandidate[position])
    }

    override fun getItemCount(): Int = allCandidate.size
}