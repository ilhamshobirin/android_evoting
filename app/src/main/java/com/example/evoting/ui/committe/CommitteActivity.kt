package com.example.evoting.ui.committe

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
import com.example.evoting.model.committee.AllCommitteeResponse
import com.example.evoting.model.committee.DataItemAllCommittee
import com.example.evoting.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommitteActivity : Activity() {
    private var allCommittee: ArrayList<DataItemAllCommittee> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_committe)

        val btnAddCommittee: ImageButton = findViewById(R.id.btn_add_committee)
        btnAddCommittee.setOnClickListener{
            val intent = Intent(this, DetailCommitteeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val progressBar: ProgressBar = findViewById(R.id.progress_bar)

        //Get List All Committee
        GlobalScope.launch(Dispatchers.IO) {
            val sessionManager = SessionManager(this@CommitteActivity)

            ApiService.endpoint.getAllCommittee("Bearer ${sessionManager.getDataLogin()?.token}")
                .enqueue(object : Callback<AllCommitteeResponse> {
                    override fun onFailure(call: Call<AllCommitteeResponse>, t: Throwable) {
//                        printLog(t.toString())
                        Toast.makeText(
                            this@CommitteActivity,
                            "Gagal mendapatkan data semua Panitia",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar.visibility = View.GONE
                    }
                    override fun onResponse(
                        call: Call<AllCommitteeResponse>,
                        response: Response<AllCommitteeResponse>
                    ) {
                        val result = response.body()?.data
                        result?.forEach {
                            if(it != null){
                                allCommittee.add(it)
                            }
                        }
                        progressBar.visibility = View.GONE

                    }
                })

        }

        val rvCommitteeData : RecyclerView = findViewById(R.id.rv_all_committee)
        rvCommitteeData.layoutManager = LinearLayoutManager(this)
        rvCommitteeData.adapter = AllCommitteAdapter(allCommittee)

    }
}