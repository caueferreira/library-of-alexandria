package com.libraryofalexandria.cards.data.network.entity

import com.squareup.moshi.Json

data class CardResponse(
    val id: String,
    @Json(name = "oracle_id") val oracleId: String,
    val name: String,
    @Json(name = "released_at") val releasedAt: String,
    @Json(name = "mana_cost") val manaCost: String,
    val cmc: Double,
    @Json(name = "type_line") val typeLine: String,
    val colors: List<String>?,
    @Json(name = "color_identity") val colorIdentity: List<String>?,
    @Json(name = "set") val setId: String,
    @Json(name = "set_name") val setName: String,
    @Json(name = "set_uri") val setUri: String,
    @Json(name = "collector_number") val collectorNumber: String,
    val rarity: String,
    @Json(name = "flavor_text") val flavorText: String?,
    @Json(name = "oracle_text") val printedText: String?,
    val prices: PricesResponse,
    @Json(name = "lang") val language: String,
    @Json(name = "image_uris") val imageUris: ImageUrisResponse
) {
    override fun toString(): String {
        return "CardResponse(id='$id', oracleId='$oracleId', name='$name', releasedAt='$releasedAt', manaCost='$manaCost', cmc=$cmc, typeLine='$typeLine', colors=$colors, colorIdentity=$colorIdentity, setId='$setId', setName='$setName', setUri='$setUri', collectorNumber='$collectorNumber', rarity='$rarity', flavorText=$flavorText, printedText=$printedText, prices=$prices, imageUris=$imageUris)"
    }
}

data class ImageUrisResponse(
    val small: String,
    val normal: String,
    val large: String,
    val png: String,
    @Json(name = "art_crop") val artCrop: String,
    @Json(name = "border_crop") val borderCrop: String
) {
    override fun toString(): String {
        return "ImageUrisResponse(small='$small', normal='$normal', large='$large', png='$png', artCrop='$artCrop', borderCrop='$borderCrop')"
    }
}

data class PricesResponse(
    val usd: Double?,
    @Json(name = "usd_foil") val usdFoil: Double?,
    val eur: Double?
) {
    override fun toString(): String {
        return "PricesResponse(usd=$usd, usdFoil=$usdFoil, eur=$eur)"
    }
}