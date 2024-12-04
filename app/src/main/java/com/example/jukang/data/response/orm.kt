package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class Summary(

	@field:SerializedName("duration")
	val duration: Any? = null,

	@field:SerializedName("distance")
	val distance: Any? = null
)

data class Metadata(

	@field:SerializedName("engine")
	val engine: Engine? = null,

	@field:SerializedName("service")
	val service: String? = null,

	@field:SerializedName("query")
	val query: Query? = null,

	@field:SerializedName("attribution")
	val attribution: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Long? = null
)

data class Query(

	@field:SerializedName("profile")
	val profile: String? = null,

	@field:SerializedName("coordinates")
	val coordinates: List<List<Any?>?>? = null,

	@field:SerializedName("format")
	val format: String? = null
)

data class RoutesItem(

	@field:SerializedName("summary")
	val summary: Summary? = null,

	@field:SerializedName("bbox")
	val bbox: List<Any?>? = null,

	@field:SerializedName("geometry")
	val geometry: String? = null,

	@field:SerializedName("segments")
	val segments: List<SegmentsItem?>? = null,

	@field:SerializedName("way_points")
	val wayPoints: List<Int?>? = null
)

data class Engine(

	@field:SerializedName("build_date")
	val buildDate: String? = null,

	@field:SerializedName("graph_date")
	val graphDate: String? = null,

	@field:SerializedName("version")
	val version: String? = null
)

data class StepsItem(

	@field:SerializedName("duration")
	val duration: Any? = null,

	@field:SerializedName("distance")
	val distance: Any? = null,

	@field:SerializedName("instruction")
	val instruction: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("type")
	val type: Int? = null,

	@field:SerializedName("way_points")
	val wayPoints: List<Int?>? = null
)

data class SegmentsItem(

	@field:SerializedName("duration")
	val duration: Any? = null,

	@field:SerializedName("distance")
	val distance: Any? = null,

	@field:SerializedName("steps")
	val steps: List<StepsItem?>? = null
)

data class Orm(

	@field:SerializedName("routes")
	val routes: List<RoutesItem?>? = null,

	@field:SerializedName("metadata")
	val metadata: Metadata? = null,

	@field:SerializedName("bbox")
	val bbox: List<Any?>? = null
)
