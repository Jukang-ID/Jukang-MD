package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class TukangResponse(

	@field:SerializedName("ListAllTukang")
	val listAllTukang: ListAllTukang? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ListAllTukang(

	@field:SerializedName("listTukang")
	val listTukang: List<ListTukangItem?>? = null
)

data class ListTukangItem(

	@field:SerializedName("tukang_id")
	val tukangId: String? = null,

	@field:SerializedName("totalReviewRating")
	val totalReviewRating: Int? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("booked")
	val booked: Boolean? = null,

	@field:SerializedName("reviewCount")
	val reviewCount: Int? = null,

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
