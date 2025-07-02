package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class DetailTukangResponse(

	@field:SerializedName("detailTukang")
	val detailTukang: DetailTukangs? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PortofolioItem(

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null
)

data class Hargas(

	@field:SerializedName("nominal")
	val nominal: Int? = null,

	@field:SerializedName("satuan")
	val satuan: String? = null,

	@field:SerializedName("rupiah")
	val rupiah: String? = null
)

data class Jadwal(

	@field:SerializedName("kamis")
	val kamis: String? = null,

	@field:SerializedName("senin")
	val senin: String? = null,

	@field:SerializedName("rabu")
	val rabu: String? = null,

	@field:SerializedName("sabtu")
	val sabtu: String? = null,

	@field:SerializedName("minggu")
	val minggu: String? = null,

	@field:SerializedName("jumat")
	val jumat: String? = null,

	@field:SerializedName("selasa")
	val selasa: String? = null
)

data class Dokumens(

	@field:SerializedName("kk")
	val kk: String? = null,

	@field:SerializedName("ktp")
	val ktp: String? = null,

	@field:SerializedName("sim")
	val sim: String? = null,

	@field:SerializedName("npwp")
	val npwp: String? = null
)

data class DataPribadi(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("domisili")
	val domisili: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("tanggal_lahir")
	val tanggalLahir: String? = null,

	@field:SerializedName("nomor_telpon")
	val nomorTelpon: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null
)

data class Lokasis(

	@field:SerializedName("lon")
	val lon: Any? = null,

	@field:SerializedName("lat")
	val lat: Any? = null
)

data class DetailTukangs(

	@field:SerializedName("tukang_id")
	val tukangId: String? = null,

	@field:SerializedName("booked")
	val booked: Boolean? = null,

	@field:SerializedName("lon")
	val lon: Any? = null,

	@field:SerializedName("dataPribadi")
	val dataPribadi: DataPribadi? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("jadwal")
	val jadwal: Jadwal? = null,

	@field:SerializedName("harga")
	val harga: Harga? = null,

	@field:SerializedName("reviewCount")
	val reviewCount: Int? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("spesialis")
	val spesialis: String? = null,

	@field:SerializedName("priceRupiah")
	val priceRupiah: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("lat")
	val lat: Any? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("totalReviewRating")
	val totalReviewRating: Any? = null,

	@field:SerializedName("ulasan")
	val ulasan: List<Any?>? = null,

	@field:SerializedName("dokumen")
	val dokumen: Dokumen? = null,

	@field:SerializedName("lokasi")
	val lokasi: Lokasi? = null,

	@field:SerializedName("namatukang")
	val namatukang: String? = null,

	@field:SerializedName("portofolio")
	val portofolio: List<PortofolioItem?>? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("domisili")
	val domisili: String? = null,

	@field:SerializedName("nomor_telpon")
	val nomorTelpon: String? = null
)
