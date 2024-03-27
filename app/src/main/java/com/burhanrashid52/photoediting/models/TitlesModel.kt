package com.burhanrashid52.photoediting.models

import com.google.gson.annotations.SerializedName

data class TitlesModel(

	@field:SerializedName("name")
	val occasion: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
