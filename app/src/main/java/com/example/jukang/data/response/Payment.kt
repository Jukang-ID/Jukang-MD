package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class Payment(

	@field:SerializedName("data")
	val data: DataPayment? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Transaksi(

	@field:SerializedName("tukang_id")
	val tukangId: String? = null,

	@field:SerializedName("status_code")
	val statusCode: String? = null,

	@field:SerializedName("dataUser")
	val dataUser: ListUserItem? = null,

	@field:SerializedName("dataTukang")
	val dataTukang: DetailTukangs? = null,

	@field:SerializedName("namalengkap")
	val namalengkap: String? = null,

	@field:SerializedName("id_transaksi")
	val idTransaksi: String? = null,

	@field:SerializedName("long")
	val jsonMemberLong: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("total")
	val total: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("namatukang")
	val namatukang: String? = null,

	@field:SerializedName("metodePembayaran")
	val metodePembayaran: String? = null,

	@field:SerializedName("spesialis")
	val spesialis: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("photoprofile")
	val photoprofile: String? = null,

	@field:SerializedName("domisili")
	val domisili: String? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("nomor_telpon")
	val nomorTelpon: String? = null
)

data class DataPayment(

	@field:SerializedName("transaksi")
	val transaksi: Transaksi? = null
)

data class paymentReq (
	val user_id: String,
	val tukang_id: String,
	val deskripsi: String,
	val tanggal: String,
	val alamat: String,
	val metodePembayaran: String,
	val lat : String? = null,
	val long : String? = null,
	val nomor_telpon: String? = null,
	val photoprofile: String? = null,
	val domisili: String? = null,
)
