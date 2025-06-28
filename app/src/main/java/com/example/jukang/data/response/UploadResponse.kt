package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@field:SerializedName("data")
	val data: DataFotos? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataFotos(

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("filePath")
	val filePath: String? = null,

	@field:SerializedName("format")
	val format: String? = null,

	@field:SerializedName("publicId")
	val publicId: String? = null
)
