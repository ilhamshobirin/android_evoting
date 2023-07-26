package com.example.evoting.model.voting

data class AddVoterRequest (
    val name: String,
    val user_name: String,
    val password: String,
    val ktp: String,
    val age: String,
    val address: String
)