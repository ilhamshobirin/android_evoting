package com.example.evoting.model.voting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.evoting.R
import com.example.evoting.model.committee.AllCommitteAdapter

class AllVoterAdapter(private val allVoter: ArrayList<DataItemVoter>):
        RecyclerView.Adapter<AllVoterAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvIndex: TextView = itemView.findViewById(R.id.index)
        private val tvNamaVoter: TextView = itemView.findViewById(R.id.voter_name)
        private val tvUsernameVoter: TextView = itemView.findViewById(R.id.voter_username)
        private val tvKtpVoter: TextView = itemView.findViewById(R.id.voter_ktp)

        fun bind(voter: DataItemVoter, position: Int){
            tvIndex.text = "$position."
            tvNamaVoter.text = "Nama: ${voter.name}"
            tvUsernameVoter.text = "Username: ${voter.userName}"
            tvKtpVoter.text = "Ktp: ${voter.ktp}"
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_all_voter, viewGroup, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allVoter[position], position+1)
    }

    override fun getItemCount(): Int = allVoter.size

}