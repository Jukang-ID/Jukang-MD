package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class Tukang(

	@field:SerializedName("tukang")
	val tukang: List<TukangListItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)


data class TukangReq(
	val namatukang: String?,
	val spesialis: String?,
	val review: String?,
	val booked: Boolean?
)


