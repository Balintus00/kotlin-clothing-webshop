package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.dto

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.UserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.UpdatedAccount as DomainUpdatedUserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.User as DomainUser

@Serializable
internal data class User(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
)

internal fun User.toDomainUser(): DomainUser = DomainUser(
    username = Username(username),
    email = Email(email),
    firstName = FirstName(firstName),
    lastName = LastName(lastName),
    dateOfBirth = DateOfBirth(LocalDate.parse(dateOfBirth)),
)

@Serializable
internal data class UpdatedUserCandidate(
    val currentPassword: String,
    val newPassword: String? = null,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
)

internal fun DomainUpdatedUserCandidate.toDto(currentPassword: Password): UpdatedUserCandidate =
    UpdatedUserCandidate(
        currentPassword = currentPassword.value,
        newPassword = newPassword?.value,
        username = username.value,
        email = email.value,
        firstName = firstName.value,
        lastName = lastName.value,
        dateOfBirth = dateOfBirth.value.toString(),
    )

@Serializable
internal data class AuthenticatedUser(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
)

internal fun UserCandidate.toAuthenticatedUser() = AuthenticatedUser(
    username = user.username.value,
    email = user.email.value,
    password = password.value,
    firstName = user.firstName.value,
    lastName = user.lastName.value,
    dateOfBirth = user.dateOfBirth.value.toString(),
)