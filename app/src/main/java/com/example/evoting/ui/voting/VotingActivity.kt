package com.example.evoting.ui.voting

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.db.williamchart.view.BarChartView
import com.example.evoting.R
import com.example.evoting.SessionManager
import com.example.evoting.model.QuickCountResponse
import com.example.evoting.model.candidate.AllCandidateAdapter
import com.example.evoting.model.candidate.AllCandidateResponse
import com.example.evoting.model.candidate.DataItemAllCandidate
import com.example.evoting.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VotingActivity : Activity() {

    private var resultQuickCount: MutableList<Pair<String, Float>> = mutableListOf()

    private val animationDuration = 1000L

    private var allCandidate: ArrayList<DataItemAllCandidate> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        val barChart: BarChartView = findViewById(R.id.barChart)
        barChart.animation.duration = animationDuration

        var rvAllCandidate: RecyclerView = findViewById(R.id.rv_all_candidate)
        val allCandidateAdapter = AllCandidateAdapter(allCandidate)
        rvAllCandidate.adapter = allCandidateAdapter
        rvAllCandidate.layoutManager = LinearLayoutManager(this)


        GlobalScope.launch(Dispatchers.IO) {
            val sessionManager = SessionManager(this@VotingActivity)
            val dataLogin = sessionManager.getDataLogin()

            ApiService.endpoint.getAllCandidate("Bearer ${dataLogin?.token}")
                .enqueue(object : Callback<AllCandidateResponse> {
                    override fun onFailure(call: Call<AllCandidateResponse>, t: Throwable) {
//                        printLog(t.toString())
                        Toast.makeText(
                            this@VotingActivity,
                            "Gagal mendapatkan data Kandidat",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    override fun onResponse(
                        call: Call<AllCandidateResponse>,
                        response: Response<AllCandidateResponse>
                    ) {
                        val result = response.body()?.data
                        result?.forEach {
                            resultQuickCount.add(
                                Pair("${it?.name?.substring(0, 15)} (${it?.voteCount})", it?.voteCount?.toFloat()!!)
                            )
                            allCandidate.add(it)
                        }
                        barChart.animate(resultQuickCount)
//                        if (result != null) {
//                            tvTotalVoter.text = result.voterOnly.toString()
//                            tvTotalCandidate.text = result.allCandidate.toString()
//                            tvVoteDone.text = result.totalDoneVote.toString()
//                            tvVoteNotYet.text = result.totalNotVote.toString()
//                        }
                        printLog("ALL Candidate $allCandidate")

                    }
                })

        }


    }

    private fun printLog(message: String){
        Log.d("Voting Activity", message)
    }
}