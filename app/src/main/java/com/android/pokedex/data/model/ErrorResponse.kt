package com.android.pokedex.data.model

import com.google.gson.annotations.SerializedName

open class ErrorResponse {
    @SerializedName("error")
    var error: Error? = null
}
