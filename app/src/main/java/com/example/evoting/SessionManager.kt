package com.example.evoting

import android.content.Context
import android.content.SharedPreferences
import com.example.evoting.model.login.DataLogin
import com.google.gson.Gson

class SessionManager(var context: Context) {
    companion object {
        private const val KEY_TOKEN = "tokenLogin"
    }

    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    private var PRIVATE_MODE = 0 //0 agar cuma bsa dibaca hp itu sendiri

    private var PREF_NAME = "prefs"

    //konstruktor
    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    //membuat session Login
    fun createLoginSession(token: String?) {
        //editor.remove()
        editor.putString(KEY_TOKEN, token)
        editor.commit() //commit digunakan untuk menyimpan perubahan
    }

    //logout user
    fun logout() {
        editor.clear()
        editor.commit()
    }

    val fetchSavedToken: String?
        get() {
            var token = pref.getString(KEY_TOKEN, "")
            return token
        }

    //cek Login
    val isLogin: Boolean
        get(){
            var token = pref.getString(KEY_TOKEN, "")
            return !token?.equals("")!!
        }

    var email: String?
        get() = pref.getString("email", "")
        set(email) {
            editor.putString("email", email)
            editor.commit()
        }

    var id_user: Int?
        get() = pref.getInt("id_user", 0)
        set(id) {
            editor.putInt("id_user", id!!)
            editor.commit()
        }

    //contoh simpan data list ke prefs

    fun setDataLogin(dataLogin: DataLogin) {
        var gson = Gson()
        var json = gson.toJson(dataLogin)
        editor.putString("DATA_LOGIN", json)
        editor.commit()
    }

    fun getDataLogin(): DataLogin? {
        val json = pref.getString("DATA_LOGIN", null)

        return Gson().fromJson(json, DataLogin::class.java)
    }
//    var selectedDesa: Int?
//        get() = pref.getInt("SelectedDesa", 0)


}