package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.FirstName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.LastName
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Username

data class User(
    val username: Username,
    val email: Email,
    val firstName: FirstName,
    val lastName: LastName,
    val dateOfBirth: DateOfBirth,
)

data class UserCandidate(
    val user: User,
    val password: Password,
)

data class UpdatableUserCandidate(
    val id: UserID,
    val currentPassword: Password,
    val newPassword: Password?,
    val user: User,
)