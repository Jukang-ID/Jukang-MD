package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class TukangDomisili(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("tukangList")
	val tukangList: List<TukangListItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class TukangListItem(

	@field:SerializedName("tukang_id")
	val tukangId: String? = null,

	@field:SerializedName("totalReviewRating")
	val totalReviewRating: Any? = null,

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

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("priceRupiah")
	val priceRupiah: String? = null,

	@field:SerializedName("domisili")
	val domisili: String? = null
)

data class requestTukang(
	@field:SerializedName("text")
	val text: String?=null
)
