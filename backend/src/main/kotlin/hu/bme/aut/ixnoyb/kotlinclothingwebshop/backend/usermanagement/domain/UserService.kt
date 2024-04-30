package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain

import com.auth0.jwt.JWT
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password

internal interface UserService {

    suspend fun login(email: Email, password: Password): JWT

    suspend fun register(userCandidate: UserCandidate): JWT

    suspend fun logout(jwtId: String)

    suspend fun getUser(userId: UserID): User

    suspend fun updateUser(updatableUserCandidate: UpdatableUserCandidate): User
}

internal class DefaultUserService : UserService {

    override suspend fun login(email: Email, password: Password): JWT {
        TODO()
    }

    override suspend fun register(userCandidate: UserCandidate): JWT {
        TODO()
    }

    override suspend fun logout(jwtId: String ) {
        // TODO
    }

    override suspend fun getUser(userId: UserID): User {
        TODO()
    }

    override suspend fun updateUser(updatableUserCandidate: UpdatableUserCandidate): User {
        TODO()
    }
}