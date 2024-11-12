package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class Tukang(

	@field:SerializedName("data")
	val data: Datas? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ListTukangItem(

	@field:SerializedName("tukang_id")
	val tukangId: String? = null,

	@field:SerializedName("booked")
	val booked: Boolean? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("namatukang")
	val namatukang: String? = null,

	@field:SerializedName("spesialis")
	val spesialis: String? = null
)

data class Datas(
	@field:SerializedName("listTukang")
	val listTukang: List<ListTukangItem?>? = null
)


