package com.libraryofalexandria.cards.data.transformer

import com.libraryofalexandria.cards.data.network.CardResponse
import com.libraryofalexandria.cards.data.network.ImageUrisResponse
import com.libraryofalexandria.cards.data.network.PricesResponse
import com.libraryofalexandria.cards.domain.*
import com.libraryofalexandria.cards.domain.Set
import java.util.*

class CardMapper(private val colorMapper: ColorMapper = ColorMapper()) {

    fun transform(response: CardResponse): Card =
        Card(
            response.id,
            response.name,
            response.collectorNumber,
            response.printedText,
            response.flavorText,
            response.typeLine,
            transformPrices(response.prices),
            Rarity.valueOf(response.rarity.toUpperCase()),
            colorMapper.transform(response.manaCost, response.cmc),
            Date(),
            transformImages(response.imageUris),
            Set(
                response.setId,
                response.setName,
                response.setUri
            )
        )


    private fun transformImages(response: ImageUrisResponse): Images =
        Images(
            response.small,
            response.normal,
            response.large,
            response.png,
            response.artCrop,
            response.borderCrop
        )

    private fun transformPrices(response: PricesResponse): Prices =
        Prices(
            response.usd,
            response.eur,
            response.usdFoil
        )
}
