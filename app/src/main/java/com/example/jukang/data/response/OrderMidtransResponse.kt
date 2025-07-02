package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class OrderMidtransResponse(

	@field:SerializedName("data")
	val data: DataUsers? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("redirect_url")
	val redirectUrl: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class DataUsers(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("uid_google")
	val uidGoogle: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("namalengkap")
	val namalengkap: String? = null,

	@field:SerializedName("photoprofile")
	val photoprofile: String? = null,

	@field:SerializedName("nomortelp")
	val nomortelp: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)


data class PaymentOrderRequest(
	val total: Int,
	val user_id: String,
	val metodePembayaran: String? = null,

)