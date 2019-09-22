package com.libraryofalexandria.cards.data.network.entity

import com.squareup.moshi.Json

data class ExpansionResponse(
    val id: String,
    val code: String,
    val name: String,
    @Json(name = "set_type") val type: String,
    @Json(name = "card_count") val totalCards: Int,
    @Json(name = "icon_svg_uri") val iconUri: String
) {
    override fun toString(): String {
        return "ExpansionResponse(id='$id', code='$code', name='$name', type='$type', totalCards=$totalCards, iconUri='$iconUri')"
    }
}

