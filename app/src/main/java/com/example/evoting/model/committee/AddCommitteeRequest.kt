package com.example.evoting.model.committee

data class AddCommitteeRequest (
    val name: String,
    val user_name: String,
    val password: String,
    val ktp: String,
    val age: String,
    val address: String
)