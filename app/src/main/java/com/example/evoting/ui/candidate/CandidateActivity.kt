package com.example.evoting.ui.candidate

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.evoting.R
import com.example.evoting.SessionManager
import com.example.evoting.model.candidate.*
import com.example.evoting.retrofit.ApiService
import com.example.evoting.ui.voting.VotingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CandidateActivity : Activity() {

    private var listCandidate: ArrayList<DataItemAllCandidate> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)

        val btnAddCandidate: ImageButton = findViewById(R.id.btn_add_candidate)
        btnAddCandidate.setOnClickListener{
            val intent = Intent(this, DetailCandidateActivity::class.java)
            startActivity(intent)
            finish()
        }

        val progressBar: ProgressBar = findViewById(R.id.progress_bar)

        //Get List All Candidate
        GlobalScope.launch(Dispatchers.IO) {
            val sessionManager = SessionManager(this@CandidateActivity)
            val dataLogin = sessionManager.getDataLogin()

            ApiService.endpoint.getAllCandidate("Bearer ${dataLogin?.token}")
                .enqueue(object : Callback<AllCandidateResponse> {
                    override fun onFailure(call: Call<AllCandidateResponse>, t: Throwable) {
//                        printLog(t.toString())
                        Toast.makeText(
                            this@CandidateActivity,
                            "Gagal mendapatkan data semua Kandidat",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar.visibility = View.GONE
                    }
                    override fun onResponse(
                        call: Call<AllCandidateResponse>,
                        response: Response<AllCandidateResponse>
                    ) {
                        val result = response.body()?.data
                        result?.forEach {
                            if(it != null){
                                listCandidate.add(it)
                            }
                        }
                        printLog("List Candidate $listCandidate")
                        progressBar.visibility = View.GONE

                    }
                })

        }

        val rvCandidateData : RecyclerView = findViewById(R.id.rv_list_candidate)
        rvCandidateData.layoutManager = LinearLayoutManager(this)
        rvCandidateData.adapter = ListCandidateAdapter(listCandidate)
    }

    private fun printLog(message: String){
        Log.d("Candidate Activity", message)
    }

}