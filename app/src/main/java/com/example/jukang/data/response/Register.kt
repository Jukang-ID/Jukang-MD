package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class Register(

	@field:SerializedName("data")
	val data: Dataa? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class User(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("namalengkap")
	val namalengkap: String? = null,

	@field:SerializedName("nomortelp")
	val nomortelp: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class Dataa(
	@field:SerializedName("user")
	val user: User? = null
)

data class registerRequest(
	val namalengkap: String,
	val nomortelp: String,
	val email: String,
	val password: String
)