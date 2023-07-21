package com.example.evoting.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.evoting.R
import com.example.evoting.SessionManager
import com.example.evoting.model.DataLogin
import com.example.evoting.model.QuickCountResponse
import com.example.evoting.retrofit.ApiService
import com.example.evoting.ui.candidate.CandidateActivity
import com.example.evoting.ui.committe.CommitteActivity
import com.example.evoting.ui.voting.VotingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : Activity() {
    companion object{
        const val EXTRA_LOGIN_DATA = "extra_login_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val dataLogin = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_LOGIN_DATA, DataLogin::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_LOGIN_DATA)
        }

        var tvName: TextView = findViewById(R.id.tv_name)
        tvName.text = "Halo, ${dataLogin?.name}"

        var tvTotalVoter: TextView = findViewById(R.id.tv_total_voter)
        var tvTotalCandidate: TextView = findViewById(R.id.tv_total_candidate)
        var tvVoteDone: TextView = findViewById(R.id.tv_vote_done)
        var tvVoteNotYet: TextView = findViewById(R.id.tv_vote_not_yet)

        GlobalScope.launch(Dispatchers.IO) {
            ApiService.endpoint.getRekapData("Bearer ${dataLogin?.token}")
                .enqueue(object : Callback<QuickCountResponse> {
                    override fun onFailure(call: Call<QuickCountResponse>, t: Throwable) {
//                        printLog(t.toString())
                        Toast.makeText(
                            this@HomeActivity,
                            "Gagal mendapatkan data Quick Count",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    override fun onResponse(
                        call: Call<QuickCountResponse>,
                        response: Response<QuickCountResponse>
                    ) {
                        val result = response.body()?.data
                        if (result != null) {
                            tvTotalVoter.text = result.voterOnly.toString()
                            tvTotalCandidate.text = result.allCandidate.toString()
                            tvVoteDone.text = result.totalDoneVote.toString()
                            tvVoteNotYet.text = result.totalNotVote.toString()
                        }
                    }
                })
        }

        val cvVoting: CardView = findViewById(R.id.cv_voting)
        cvVoting.setOnClickListener{
            val intent = Intent(this, VotingActivity::class.java)
            startActivity(intent)
        }

        val cvAddCandidate: CardView = findViewById(R.id.cv_add_candidate)
        if(dataLogin?.userLevel!! > 0){
            cvAddCandidate.visibility = View.VISIBLE
        }else{
            cvAddCandidate.visibility = View.GONE
        }
        cvAddCandidate.setOnClickListener{
            val intent = Intent(this, CandidateActivity::class.java)
            startActivity(intent)
        }

        val cvAddCommittee: CardView = findViewById(R.id.cv_add_committee)
        if(dataLogin.userLevel == 10){
            cvAddCommittee.visibility = View.VISIBLE
        }else{
            cvAddCommittee.visibility = View.GONE
        }
        cvAddCommittee.setOnClickListener{
            val intent = Intent(this, CommitteActivity::class.java)
            startActivity(intent)
        }

        //EXIT
        val ivExit: ImageView = findViewById(R.id.iv_exit)
        ivExit.setOnClickListener{
            GlobalScope.launch {
                ApiService.endpoint.postLogout(token = "Bearer ${dataLogin.token}")
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(
                                this@HomeActivity,
                                "Logout Gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if(response.code() == 200){
                                Toast.makeText(
                                    this@HomeActivity,
                                    "Berhasil Logout",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val sessionManager = SessionManager(this@HomeActivity)
                                sessionManager.logout()

                                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                        }
                    })
            }
        }

    }

    override fun onStart() {
        super.onStart()
    }

    private fun printLog(message: String){
        Log.d("Home Activity", message)
    }
}