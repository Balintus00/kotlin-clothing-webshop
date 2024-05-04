package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.entity

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperTable
import java.time.LocalDate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.User as DomainUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserCandidate as DomainUserCandidate

@KomapperEntity
@KomapperTable("application_users")
data class User(
    @KomapperId
    val id: String = "",
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate, // TODO kotlinx-datetime doesn't work, but it should according to documentation
)

fun DomainUserCandidate.toUser(storedPassword: String, id: String = ""): User = User(
    id = id,
    username = user.username.value,
    email = user.email.value,
    password = storedPassword,
    firstName = user.firstName.value,
    lastName = user.lastName.value,
    dateOfBirth = user.dateOfBirth.value.toJavaLocalDate(),
)

fun User.toDomainUser(): DomainUser = DomainUser(
    username = Username(username),
    email = Email(email),
    firstName = FirstName(firstName),
    lastName = LastName(lastName),
    dateOfBirth = DateOfBirth(dateOfBirth.toKotlinLocalDate()),
)