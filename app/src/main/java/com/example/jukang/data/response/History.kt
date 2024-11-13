package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class History(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("total")
	val total: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("namalengkap")
	val namalengkap: String? = null,

	@field:SerializedName("namatukang")
	val namatukang: String? = null,

	@field:SerializedName("metodePembayaran")
	val metodePembayaran: String? = null,

	@field:SerializedName("spesialis")
	val spesialis: String? = null,

	@field:SerializedName("id_transaksi")
	val idTransaksi: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null
)
