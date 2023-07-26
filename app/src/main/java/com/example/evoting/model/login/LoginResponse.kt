package com.example.evoting.model.login

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class LoginResponse(

	@field:SerializedName("data")
	val data: DataLogin? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class DataLogin(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("ktp")
	val ktp: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("user_level")
	val userLevel: Int? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("isvoted")
	var isvoted: Int? = null
) : Parcelable
