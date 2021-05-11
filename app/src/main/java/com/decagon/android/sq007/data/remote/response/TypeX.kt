package com.decagon.android.sq007.data.remote.response

data class TypeX(
    val name: String,
    val url: String
) {
    override fun toString(): String {
        return name
    }
}
