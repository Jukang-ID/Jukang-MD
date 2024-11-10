package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(

	@field:SerializedName("important")
	val important: String? = null,

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("posts")
	val posts: List<PostsItem?>? = null,

	@field:SerializedName("featured_post")
	val featuredPost: FeaturedPost? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Pagination(

	@field:SerializedName("totalPage")
	val totalPage: Any? = null,

	@field:SerializedName("currentPage")
	val currentPage: Any? = null
)

data class FeaturedPost(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("pusblised_at")
	val pusblisedAt: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("image_desc")
	val imageDesc: String? = null,

	@field:SerializedName("headline")
	val headline: String? = null
)

data class PostsItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("premium_badge")
	val premiumBadge: String? = null,

	@field:SerializedName("pusblised_at")
	val pusblisedAt: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("headline")
	val headline: String? = null
)
