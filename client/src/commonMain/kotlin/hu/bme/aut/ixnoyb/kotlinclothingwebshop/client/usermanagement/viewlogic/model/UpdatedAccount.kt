package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.viewlogic.model

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username
import kotlinx.datetime.LocalDate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.repository.model.UpdatedAccount as DomainUpdatedAccount

data class UpdatedAccount(
    val dateOfBirth: LocalDate,
    val email: String,
    val firstName: String,
    val lastName: String,
    val newPassword: String,
    val username: String,
)

internal fun UpdatedAccount.toDomainUpdatedAccount(): DomainUpdatedAccount = DomainUpdatedAccount(
    dateOfBirth = DateOfBirth(dateOfBirth),
    email = Email(email),
    firstName = FirstName(firstName),
    lastName = LastName(lastName),
    username = Username(username),
    newPassword = if (newPassword.isNotEmpty()) Password(newPassword) else null,
)