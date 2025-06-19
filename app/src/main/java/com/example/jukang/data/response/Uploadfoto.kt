package com.example.jukang.data.response

data class Uploadfoto(
	val data: DataFoto? = null,
	val message: String? = null,
	val status: String? = null
)

data class DataFoto(
	val path: String? = null,
	val filename: String? = null
)

