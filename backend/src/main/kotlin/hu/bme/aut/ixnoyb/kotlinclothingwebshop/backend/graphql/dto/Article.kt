package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql.dto

import com.expediagroup.graphql.generator.annotations.GraphQLDescription

@GraphQLDescription("Clothing Article")
data class Article(
    @GraphQLDescription("article ID") val id: String,
)