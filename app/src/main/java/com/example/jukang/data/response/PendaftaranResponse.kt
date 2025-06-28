package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class PendaftaranResponse(

	@field:SerializedName("data")
	val data: DataPend? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataPend(

	@field:SerializedName("user")
	val user: UserPend? = null
)

data class UserPend(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("namalengkap")
	val namalengkap: String? = null,

	@field:SerializedName("photoktp")
	val photoktp: String? = null,

	@field:SerializedName("photoprofile")
	val photoprofile: String? = null,

	@field:SerializedName("nomortelp")
	val nomortelp: String? = null,

	@field:SerializedName("domisili")
	val domisili: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class PendaftaranReq(
	val user_id: String?,
	val namalengkap: String?,
	val nomortelp: String?,
	val email: String?,
	val domisili: String?,
	val photoprofile: String?,
	val photoktp: String? = null,
	val spesialis: String? = null

)
