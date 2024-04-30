package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.api.dto

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserCandidate as DomainAuthenticatedUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.User as DomainUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
internal data class User(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
)

internal fun DomainUser.toDto(): User = User(
    username = username.value,
    email = email.value,
    firstName =  firstName.value,
    lastName = lastName.value,
    dateOfBirth = dateOfBirth.value.toString(),
)

@Serializable
internal data class UpdatedUserCandidate(
    val updatedUser: AuthenticatedUser,
    val currentPassword: String,
)

@Serializable
internal data class AuthenticatedUser(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
) {

    fun toDomainModel(): DomainAuthenticatedUser = DomainAuthenticatedUser(
        user = DomainUser(
            username = Username(username),
            email = Email(email),
            firstName = FirstName(firstName),
            lastName = LastName(lastName),
            dateOfBirth = DateOfBirth(LocalDate.parse(dateOfBirth)),
        ),
        password = Password(password),
    )
}