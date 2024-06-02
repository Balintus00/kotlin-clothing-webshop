package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.datasource.serialization

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.User as DomainUser

@Serializable
internal data class User(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
)

internal fun DomainUser.toSerializableUser(): User = User(
    username = username.value,
    email = email.value,
    firstName = firstName.value,
    lastName = lastName.value,
    dateOfBirth = dateOfBirth.value.toString(),
)

internal fun User.toDomainUser(): DomainUser = DomainUser(
    username = Username(username),
    email = Email(email),
    firstName = FirstName(firstName),
    lastName = LastName(lastName),
    dateOfBirth = DateOfBirth(LocalDate.parse(dateOfBirth)),
)