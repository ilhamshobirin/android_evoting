package com.example.evoting.model.voting

data class AddVotingRequest (
    val vote_date: String,
    val voter: Int,
    val vote_to: Int,
)