package com.example.evoting.model.candidate

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.evoting.R
import com.example.evoting.ui.candidate.CandidateActivity
import com.example.evoting.ui.candidate.DetailCandidateActivity

class ListCandidateAdapter  (private val listCandidate: ArrayList<DataItemAllCandidate>):
        RecyclerView.Adapter<ListCandidateAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCandidate : ImageView = itemView.findViewById(R.id.iv_list_candidate)
        private val tvCandidateName : TextView = itemView.findViewById(R.id.tv_candidate_name)
        private val tvCandidateDetail : TextView = itemView.findViewById(R.id.tv_candidate_detail)

        fun bind(candidate: DataItemAllCandidate) {
//            ivCandidate.setImageResource(R.drawable.ic_person)
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

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailCandidateActivity::class.java)
                intent.putExtra(DetailCandidateActivity.EXTRA_DETAIL, candidate)
                itemView.context.startActivity(intent)
                (itemView.context as CandidateActivity).finish()
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_list_candidate, viewGroup, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCandidate[position])
    }

    override fun getItemCount(): Int = listCandidate.size
}