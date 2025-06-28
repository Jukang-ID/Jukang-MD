package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class DetailTukangResponse(

	@field:SerializedName("detailTukang")
	val detailTukang: DetailTukang? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DetailTukangs(

	@field:SerializedName("tukang_id")
	val tukangId: String? = null,

	@field:SerializedName("totalReviewRating")
	val totalReviewRating: Int? = null,

	@field:SerializedName("booked")
	val booked: Boolean? = null,

	@field:SerializedName("lon")
	val lon: Any? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

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
	val domisili: String? = null,

	@field:SerializedName("nomor_telpon")
	val nomorTelpon: String? = null,

	@field:SerializedName("lat")
	val lat: Any? = null
)
