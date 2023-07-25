package com.example.evoting.model

import com.google.gson.annotations.SerializedName

data class QuickCountResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("all_candidate")
	val allCandidate: Int? = null,

	@field:SerializedName("total_done_vote")
	val totalDoneVote: Int? = null,

	@field:SerializedName("voter_only")
	val voterOnly: Int? = null,

	@field:SerializedName("total_not_vote")
	val totalNotVote: Int? = null
)
