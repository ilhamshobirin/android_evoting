package com.example.evoting.ui.voter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.evoting.R
import com.example.evoting.SessionManager
import com.example.evoting.model.voting.AddVoterRequest
import com.example.evoting.retrofit.ApiService
import com.example.evoting.ui.committe.CommitteActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddVoterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_voter)

        val etName: EditText = findViewById(R.id.et_voter_name)
        val etUsername: EditText = findViewById(R.id.et_voter_username)
        val etPassword: EditText = findViewById(R.id.et_voter_password)
        val etKtp: EditText = findViewById(R.id.et_voter_ktp)
        val etAge: EditText = findViewById(R.id.et_voter_age)
        val etAddress: EditText = findViewById(R.id.et_voter_address)

        val btnAddVoterRequest: Button = findViewById(R.id.btn_add_voter)
        btnAddVoterRequest.setOnClickListener {
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
                val addVoter = AddVoterRequest(name, username, password, ktp, age, address)

                GlobalScope.launch(Dispatchers.IO) {
                    val sessionManager = SessionManager(this@AddVoterActivity)

                    ApiService.endpoint.addVoter("Bearer ${sessionManager.getDataLogin()?.token}", addVoter)
                        .enqueue(object : Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(
                                    this@AddVoterActivity,
                                    "Gagal menambahkan data pemilih",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if(response.isSuccessful){
                                    Toast.makeText(
                                        this@AddVoterActivity,
                                        "Berhasil menambahkan data pemilih",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@AddVoterActivity, VoterActivity::class.java)
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