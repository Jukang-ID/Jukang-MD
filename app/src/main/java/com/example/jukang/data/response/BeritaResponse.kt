package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class BeritaResponse(

	@field:SerializedName("data")
	val data: DataBErita? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: Any? = null
)

data class PostsItem(

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("link")
    val link: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("pubDate")
    val pubDate: String? = null
)

data class DataBErita(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("posts")
	val posts: List<PostsItem?>? = null
)
