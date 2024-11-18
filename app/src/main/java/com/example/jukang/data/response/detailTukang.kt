package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class DetailTukang(

	@field:SerializedName("detailTukang")
	val detailTukang: DetailTukang? = null,

	@field:SerializedName("status")
	val status: String? = null,

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
	val priceRupiah: String? = null,

	@field:SerializedName("domisili")
	val domisili: String? = null


)
