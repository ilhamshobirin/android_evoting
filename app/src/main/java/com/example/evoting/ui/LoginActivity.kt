package com.example.evoting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.evoting.retrofit.ApiService
import com.example.evoting.R
import com.example.evoting.SessionManager
import com.example.evoting.model.login.DataLogin
import com.example.evoting.model.login.LoginRequest
import com.example.evoting.model.login.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sessionManager = SessionManager(this)

        val btnLogin: Button = findViewById(R.id.btn_login)
        val etUsername: EditText = findViewById(R.id.et_username)
        val etPassword: EditText = findViewById(R.id.et_password)

        btnLogin.setOnClickListener{
            val username = etUsername.text.toString()
            val pass = etPassword.text.toString()
            val loginRequest = LoginRequest(username, pass)

            if(username.isEmpty()){
                Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else if(pass.isEmpty()){
                Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else{
            ApiService.endpoint.postLogin(loginRequest)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            this@LoginActivity,
                            t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        printLog(t.toString())
                    }
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.code() == 200 && response.body()?.data != null) {
                            Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()
                            printLog("Login Success")
                            val result = response.body()
//                            printLog(result.toString())
                            if(result?.data != null){
                                val dataLogin: DataLogin = result.data
                                printLog("Data Login ${dataLogin.userLevel}")
                                sessionManager.setDataLogin(dataLogin)
                            }
                            sessionManager.createLoginSession(result?.data?.token)
                            goToHome(result?.data)
                        } else {
                            Toast.makeText(this@LoginActivity, "Login gagal!", Toast.LENGTH_SHORT).show()
                            printLog("Login Failed ${response.body()}")
                        }
                    }
                })
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val sessionManager = SessionManager(this)

        var isLogin = sessionManager.isLogin
        // check if there is a session
        if (isLogin) {
            goToHome(sessionManager.getDataLogin())
        }

    }

    private fun goToHome(dataLogin: DataLogin?){
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(HomeActivity.EXTRA_LOGIN_DATA, dataLogin)
        startActivity(intent)
        finish()
    }

    private fun printLog(message: String){
        Log.d("Login Activity", message)
    }
}