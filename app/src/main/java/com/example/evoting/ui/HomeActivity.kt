package com.example.evoting.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.evoting.R
import com.example.evoting.SessionManager
import com.example.evoting.model.login.DataLogin
import com.example.evoting.model.QuickCountResponse
import com.example.evoting.retrofit.ApiService
import com.example.evoting.ui.candidate.CandidateActivity
import com.example.evoting.ui.committe.CommitteActivity
import com.example.evoting.ui.voter.VoterActivity
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

    lateinit var tvName: TextView

    lateinit var tvTotalVoter: TextView
    lateinit var tvTotalCandidate: TextView
    lateinit var tvVoteDone: TextView
    lateinit var tvVoteNotYet: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val dataLogin = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_LOGIN_DATA, DataLogin::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_LOGIN_DATA)
        }

        printLog(dataLogin.toString())

        tvName = findViewById(R.id.tv_name)
        tvName.text = "Halo, ${dataLogin?.name}"

        tvTotalVoter = findViewById(R.id.tv_total_voter)
        tvTotalCandidate = findViewById(R.id.tv_total_candidate)
        tvVoteDone = findViewById(R.id.tv_vote_done)
        tvVoteNotYet = findViewById(R.id.tv_vote_not_yet)

        val cvVoting: CardView = findViewById(R.id.cv_voting)
        cvVoting.setOnClickListener{
            val intent = Intent(this, VotingActivity::class.java)
            startActivity(intent)
        }

        val cvAddVoter: CardView = findViewById(R.id.cv_add_voter)
        val cvAddCandidate: CardView = findViewById(R.id.cv_add_candidate)
        if(dataLogin?.userLevel!! > 0){
            cvAddVoter.visibility = View.VISIBLE
            cvAddCandidate.visibility = View.VISIBLE
        }else{
            cvAddVoter.visibility = View.GONE
            cvAddCandidate.visibility = View.GONE
        }

        cvAddVoter.setOnClickListener {
            val intent = Intent(this, VoterActivity::class.java)
            startActivity(intent)
        }
        cvAddCandidate.setOnClickListener{
            val intent = Intent(this, CandidateActivity::class.java)
            startActivity(intent)
        }

        val cvAddCommittee: CardView = findViewById(R.id.cv_add_committee)
        val cvDeleteVoting: CardView = findViewById(R.id.cv_delete_voting)
        if(dataLogin.userLevel == 10){
            cvAddCommittee.visibility = View.VISIBLE
            cvDeleteVoting.visibility = View.VISIBLE
        }else{
            cvAddCommittee.visibility = View.GONE
            cvDeleteVoting.visibility = View.GONE
        }
        cvAddCommittee.setOnClickListener{
            val intent = Intent(this, CommitteActivity::class.java)
            startActivity(intent)
        }
        cvDeleteVoting.setOnClickListener {
            GlobalScope.launch {
                ApiService.endpoint.deleteVoting(token = "Bearer ${dataLogin.token}")
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(
                                this@HomeActivity,
                                "Reset count gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.code() == 200) {
                                Toast.makeText(
                                    this@HomeActivity,
                                    "Reset count berhasil",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
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

        val sessionManager = SessionManager(this)

        GlobalScope.launch(Dispatchers.IO) {
            ApiService.endpoint.getRekapData("Bearer ${sessionManager.getDataLogin()?.token}")
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

    }

    private fun printLog(message: String){
        Log.d("Home Activity", message)
    }
}