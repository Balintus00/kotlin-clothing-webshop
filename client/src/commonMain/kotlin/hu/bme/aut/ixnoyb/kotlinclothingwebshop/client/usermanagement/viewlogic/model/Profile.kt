package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username
import kotlinx.datetime.LocalDate

data class Profile(
    val dateOfBirth: LocalDate,
    val email: String,
    val firstName: String,
    val lastName: String,
    val username: String,
)

internal fun Profile.toUser(): User = User(
    dateOfBirth = DateOfBirth(dateOfBirth),
    email = Email(email),
    firstName = FirstName(firstName),
    lastName = LastName(lastName),
    username = Username(username),
)

internal fun User.toProfile(): Profile = Profile(
    dateOfBirth = dateOfBirth.value,
    email = email.value,
    firstName = firstName.value,
    lastName = lastName.value,
    username = username.value,
)