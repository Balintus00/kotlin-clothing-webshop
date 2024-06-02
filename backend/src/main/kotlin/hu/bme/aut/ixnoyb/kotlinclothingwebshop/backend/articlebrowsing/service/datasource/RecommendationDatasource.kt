package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.articlebrowsing.service.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth

interface RecommendationDatasource {

    suspend fun getUserEmbedding(userIDIndex: Int, userBirthDate: DateOfBirth): FloatArray
}