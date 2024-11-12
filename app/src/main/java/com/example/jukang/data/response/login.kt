package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class Users(

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

data class Dataas(

	@field:SerializedName("user")
	val user: Users? = null
)

data class Login(

	@field:SerializedName("data")
	val data: Dataas? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)


data class loginRequest(

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("password")
	val password: String? = null
)