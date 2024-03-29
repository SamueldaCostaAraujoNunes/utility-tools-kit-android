package com.samuelnunes.data.local.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samuelnunes.domain.entity.Breed

@Entity
data class BreedEntity(
    val adaptability: Int,
    val affectionLevel: Int,
    val altNames: String?,
    val cfaUrl: String?,
    val childFriendly: Int,
    val countryCode: String,
    val countryCodes: String,
    val description: String,
    val dogFriendly: Int,
    val energyLevel: Int,
    val experimental: Int,
    val grooming: Int,
    val hairless: Int,
    val healthIssues: Int,
    val hypoallergenic: Int,
    @PrimaryKey
    val breedId: String,
    val indoor: Int,
    val intelligence: Int,
    val lap: Int,
    val lifeSpan: String,
    val name: String,
    val natural: Int,
    val origin: String,
    val rare: Int,
    val rex: Int,
    val sheddingLevel: Int,
    val shortLegs: Int,
    val socialNeeds: Int,
    val strangerFriendly: Int,
    val suppressedTail: Int,
    val temperament: String,
    val vcahospitalsUrl: String?,
    val vetstreetUrl: String?,
    val vocalisation: Int,
    val wikipediaUrl: String?
) {
    fun toBreed(image: List<Breed.Image>): Breed = Breed(
        adaptability,
        affectionLevel,
        altNames,
        cfaUrl,
        childFriendly,
        countryCode,
        countryCodes,
        description,
        dogFriendly,
        energyLevel,
        experimental,
        grooming,
        hairless,
        healthIssues,
        hypoallergenic,
        breedId,
        image,
        indoor,
        intelligence,
        lap,
        lifeSpan,
        name,
        natural,
        origin,
        rare,
        rex,
        sheddingLevel,
        shortLegs,
        socialNeeds,
        strangerFriendly,
        suppressedTail,
        temperament,
        vcahospitalsUrl,
        vetstreetUrl,
        vocalisation,
        wikipediaUrl
    )

}