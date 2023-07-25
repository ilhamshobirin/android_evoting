package com.example.evoting.model.candidate

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.evoting.R
import com.example.evoting.SessionManager
import com.example.evoting.model.DataLogin
import com.example.evoting.ui.candidate.DetailCandidateActivity

class AllCandidateAdapter (private val allCandidate: ArrayList<DataItemAllCandidate>):
        RecyclerView.Adapter<AllCandidateAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCandidate : ImageView = itemView.findViewById(R.id.iv_list_candidate)
        private val tvCandidateName : TextView = itemView.findViewById(R.id.tv_candidate_name)
        private val tvCandidateDetail : TextView = itemView.findViewById(R.id.tv_candidate_detail)
        private val btnAddVote : Button = itemView.findViewById(R.id.btn_add_vote)

        val sessionManager = SessionManager(itemView.context)
        val dataLogin: DataLogin? = sessionManager.getDataLogin()

        fun bind(candidate: DataItemAllCandidate) {
            tvCandidateName.text = candidate.name
            tvCandidateDetail.text = candidate.detail

            Glide
                .with(itemView.context)
                .load(candidate.image)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
//                .placeholder(R.drawable.ic_camera)
                .into(ivCandidate)

            if(dataLogin?.userLevel == 10 || dataLogin?.isvoted == 1){
                btnAddVote.visibility = View.GONE
            }

            btnAddVote.setOnClickListener {
                //ADD Vote
            }
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