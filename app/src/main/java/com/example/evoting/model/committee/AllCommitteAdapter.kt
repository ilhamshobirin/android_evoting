package com.example.evoting.model.committee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.evoting.R
import com.example.evoting.model.candidate.AllCandidateAdapter

class AllCommitteAdapter(private val allcommittee: ArrayList<DataItemAllCommittee>):
    RecyclerView.Adapter<AllCommitteAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvIndex: TextView = itemView.findViewById(R.id.index)
        private val tvNamaPanitia: TextView = itemView.findViewById(R.id.comm_name)
        private val tvUsernamePanitia: TextView = itemView.findViewById(R.id.comm_username)
        private val tvKtpPanitia: TextView = itemView.findViewById(R.id.comm_ktp)

        fun bind(committee: DataItemAllCommittee, position: Int){
            tvIndex.text = "$position."
            tvNamaPanitia.text = "Nama: ${committee.name}"
            tvUsernamePanitia.text = "Username: ${committee.userName}"
            tvKtpPanitia.text = "Ktp: ${committee.ktp}"
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_all_committee, viewGroup, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allcommittee[position], position+1)
    }

    override fun getItemCount(): Int = allcommittee.size

}