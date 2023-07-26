package com.example.evoting.ui.voter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.evoting.R
import com.example.evoting.SessionManager
import com.example.evoting.model.committee.AllCommitteAdapter
import com.example.evoting.model.voting.AllVoterAdapter
import com.example.evoting.model.voting.AllVoterResponse
import com.example.evoting.model.voting.DataItemVoter
import com.example.evoting.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoterActivity : Activity() {
    private var allVoter: ArrayList<DataItemVoter> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voter)

        val btnAddVoter: ImageButton = findViewById(R.id.btn_add_voter)
        btnAddVoter.setOnClickListener{
            val intent = Intent(this, AddVoterActivity::class.java)
            startActivity(intent)
            finish()
        }

        val progressBar: ProgressBar = findViewById(R.id.progress_bar)

        //Get List All Voter
        GlobalScope.launch(Dispatchers.IO) {
            val sessionManager = SessionManager(this@VoterActivity)

            ApiService.endpoint.getAllVoter("Bearer ${sessionManager.getDataLogin()?.token}")
                .enqueue(object : Callback<AllVoterResponse> {
                    override fun onFailure(call: Call<AllVoterResponse>, t: Throwable) {
//                        printLog(t.toString())
                        Toast.makeText(
                            this@VoterActivity,
                            "Gagal mendapatkan data semua pemilih",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar.visibility = View.GONE
                    }
                    override fun onResponse(
                        call: Call<AllVoterResponse>,
                        response: Response<AllVoterResponse>
                    ) {
//                        Log.d("Voter Activity", "onResponse: $response")
                        val result = response.body()?.data
                        result?.forEach {
                            if(it != null){
                                allVoter.add(it)
                            }
                        }
                        progressBar.visibility = View.GONE
                    }
                })

        }

        val rvVoterData : RecyclerView = findViewById(R.id.rv_all_voter)
        rvVoterData.layoutManager = LinearLayoutManager(this)
        rvVoterData.adapter = AllVoterAdapter(allVoter)

    }
}