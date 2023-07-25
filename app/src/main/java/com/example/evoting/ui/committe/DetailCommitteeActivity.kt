package com.example.evoting.ui.committe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.evoting.R
import com.example.evoting.SessionManager
import com.example.evoting.model.committee.AddCommitteeRequest
import com.example.evoting.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCommitteeActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_committee)

        val etName: EditText = findViewById(R.id.et_comm_name)
        val etUsername: EditText = findViewById(R.id.et_comm_username)
        val etPassword: EditText = findViewById(R.id.et_comm_password)
        val etKtp: EditText = findViewById(R.id.et_comm_ktp)
        val etAge: EditText = findViewById(R.id.et_comm_age)
        val etAddress: EditText = findViewById(R.id.et_comm_address)

        val btnAddCommitteeRequest: Button = findViewById(R.id.btn_add_committee)
        btnAddCommitteeRequest.setOnClickListener {
            val name = etName.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val ktp = etKtp.text.toString()
            val age = etAge.text.toString()
            val address = etAddress.text.toString()

            if(name.isEmpty() || username.isEmpty() || password.isEmpty()
                || ktp.isEmpty() || age.isEmpty() || address.isEmpty() )
            {
                Toast.makeText(this, "Semua data wajib diisi!", Toast.LENGTH_SHORT).show()
                Log.d("Detail Committee", "Data masih ada yg kosong")
            }else{
                val addCommittee = AddCommitteeRequest(name, username, password, ktp, age, address)

                GlobalScope.launch(Dispatchers.IO) {
                    val sessionManager = SessionManager(this@DetailCommitteeActivity)

                    ApiService.endpoint.addCommittee("Bearer ${sessionManager.getDataLogin()?.token}", addCommittee)
                        .enqueue(object : Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(
                                    this@DetailCommitteeActivity,
                                    "Gagal menambahkan data Panitia",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if(response.isSuccessful){
                                    Toast.makeText(
                                        this@DetailCommitteeActivity,
                                        "Berhasil menambahkan data panitia",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@DetailCommitteeActivity, CommitteActivity::class.java)
                                    //                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        })
            }


            }
        }
    }
}