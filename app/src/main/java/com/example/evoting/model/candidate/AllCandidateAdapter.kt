package com.example.evoting.model.candidate

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.evoting.R
import com.example.evoting.SessionManager
import com.example.evoting.model.voting.AddVotingRequest
import com.example.evoting.model.login.DataLogin
import com.example.evoting.retrofit.ApiService
import com.example.evoting.ui.HomeActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AllCandidateAdapter (private val allCandidate: ArrayList<DataItemAllCandidate>):
        RecyclerView.Adapter<AllCandidateAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCandidate : ImageView = itemView.findViewById(R.id.iv_list_candidate)
        private val tvCandidateName : TextView = itemView.findViewById(R.id.tv_candidate_name)
        private val tvCandidateDetail : TextView = itemView.findViewById(R.id.tv_candidate_detail)
        private val btnAddVote : Button = itemView.findViewById(R.id.btn_add_vote)

        val sessionManager = SessionManager(itemView.context)
        val dataLogin: DataLogin? = sessionManager.getDataLogin()

        @RequiresApi(Build.VERSION_CODES.O)
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

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val current = LocalDateTime.now().format(formatter)

            btnAddVote.setOnClickListener {
                val addVotingRequest = AddVotingRequest(current, dataLogin?.id!!, candidate.id!!)
//                Log.d("Add Voting", "$addVotingRequest")
                //ADD Vote
                ApiService.endpoint.addVoting("Bearer ${dataLogin?.token}", addVotingRequest)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                        printLog(t.toString())
                            Log.d("Add Voting", "Gagal")
                            Toast.makeText(
                                itemView.context,
                                "Gagal menambahkan voting",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            val result = response.body()
                            Log.d("Add Voting", "$response")
                            if(response.isSuccessful){
                                Toast.makeText(
                                    itemView.context,
                                    "Voting Berhasil",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(itemView.context, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                itemView.context.startActivity(intent)

                                dataLogin.isvoted = 1

                                sessionManager.setDataLogin(dataLogin)
                            }

                        }
                    })
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_all_candidate, viewGroup, false)

        return ViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allCandidate[position])
    }

    override fun getItemCount(): Int = allCandidate.size
}