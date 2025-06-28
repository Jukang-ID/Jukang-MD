package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class UpdateStatusTransaksiResponse(

	@field:SerializedName("updated_status")
	val updatedStatus: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UpdateStatusReq (
	val id_transaksi: String? = null,
	val status_code: String? = null
)
