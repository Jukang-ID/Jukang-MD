package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class Tukang(

	@field:SerializedName("tukang")
	val tukang: List<TukangItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class TukangItem(

	@field:SerializedName("tukang_id")
	val tukangId: String? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("booked")
	val booked: Boolean? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("namatukang")
	val namatukang: String? = null,

	@field:SerializedName("spesialis")
	val spesialis: String? = null,

	@field:SerializedName("priceRupiah")
	val priceRupiah: String? = null
)

data class TukangReq(
	val namatukang: String?,
	val spesialis: String?,
	val review: String?,
	val booked: Boolean?
)
