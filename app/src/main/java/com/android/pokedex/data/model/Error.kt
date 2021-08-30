package com.android.pokedex.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("message")
    var message: String,
    @Expose(serialize = false, deserialize = false)
    var httpErrorCode: Int?
)