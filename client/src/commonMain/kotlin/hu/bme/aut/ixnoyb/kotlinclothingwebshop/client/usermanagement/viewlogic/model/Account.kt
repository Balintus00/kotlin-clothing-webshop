package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.UserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username
import kotlinx.datetime.LocalDate

data class Account(
    val dateOfBirth: LocalDate,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val username: String,
)

internal fun Account.toUserCandidate(): UserCandidate = UserCandidate(
    user = User(
        dateOfBirth = DateOfBirth(dateOfBirth),
        email = Email(email),
        firstName = FirstName(firstName),
        lastName = LastName(lastName),
        username = Username(username),
    ),
    password = Password(password),
)