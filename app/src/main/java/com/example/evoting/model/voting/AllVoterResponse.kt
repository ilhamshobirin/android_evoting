package com.example.evoting.model.voting

import com.google.gson.annotations.SerializedName

data class AllVoterResponse(

	@field:SerializedName("data")
	val data: List<DataItemVoter?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItemVoter(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("ktp")
	val ktp: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("user_level")
	val userLevel: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("isvoted")
	val isvoted: Int? = null
)
