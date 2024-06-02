package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.federation.directives.ContactDirective
import com.expediagroup.graphql.server.Schema

@ContactDirective(
    name = "Bálint Traxler",
    url = "https://github.com/Balintus00/",
)
@GraphQLDescription("GraphQL schema of Kotlin Clothing Webshop application")
class KotlinClothingWebshopSchema : Schema