package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.entity.authenticationToken
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.entity.toDomainUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.entity.toKomapperEntity
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.entity.toUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.entity.user
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UpdatableUserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.datasource.UserDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.datasource.UserDatasource.Companion.ERROR_MESSAGE_TOKEN_NOT_FOUND
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.datasource.UserDatasource.Companion.ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.datasource.UserDatasource.Companion.ERROR_MESSAGE_USER_INVALID_PASSWORD
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.datasource.UserDatasource.Companion.ERROR_MESSAGE_USER_NOT_FOUND
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.datasource.UserDatasource.Companion.ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.model.TokenID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.DateOfBirth
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.expression.WhereDeclaration
import org.komapper.core.dsl.operator.and
import org.komapper.core.dsl.operator.max
import org.komapper.core.dsl.operator.or
import org.komapper.core.dsl.query.firstOrNull
import org.komapper.core.dsl.query.singleOrNull
import org.komapper.r2dbc.R2dbcDatabase
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.model.AuthenticationToken as DomainAuthenticationToken

internal class KomapperUserDatasource(
    private val database: R2dbcDatabase,
    private val passwordEncoder: PasswordEncoder,
) : UserDatasource {

    override suspend fun authenticateUser(email: Email, password: Password): UserID =
        database.runQuery {
            val userSchema = Meta.user

            QueryDsl
                .from(userSchema)
                .where { userSchema.email eq email.value }
                .select(userSchema.password, userSchema.id)
        }.firstOrNull()?.let { passwordWithId ->
            if (passwordEncoder.matches(password.value, passwordWithId.first)) {
                UserID(passwordWithId.second!!)
            } else {
                throw IllegalArgumentException(ERROR_MESSAGE_USER_INVALID_PASSWORD)
            }
        } ?: throw IllegalArgumentException(ERROR_MESSAGE_USER_NOT_FOUND)

    override suspend fun createAuthenticationToken(userID: UserID): DomainAuthenticationToken {
        val authenticationToken = DomainAuthenticationToken(
            id = TokenID(UUID.randomUUID().toString()),
            ownerUserID = userID,
        )

        database.runQuery {
            val authenticationTokenSchema = Meta.authenticationToken

            QueryDsl
                .insert(authenticationTokenSchema)
                .single(authenticationToken.toKomapperEntity(isActive = true))
        }

        return authenticationToken
    }

    override suspend fun saveNewUser(userCandidate: UserCandidate): UserID =
        database.withTransaction {
            val userSchema = Meta.user

            val conflictingUser = database.runQuery {
                val emailFilter: WhereDeclaration =
                    { userSchema.email eq userCandidate.user.email.value }
                val usernameFilter: WhereDeclaration =
                    { userSchema.username eq userCandidate.user.username.value }

                QueryDsl
                    .from(userSchema)
                    .where(emailFilter.or(usernameFilter))
            }.firstOrNull()

            when {
                conflictingUser != null
                        && conflictingUser.email == userCandidate.user.email.value -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN)
                }

                conflictingUser != null
                        && conflictingUser.username == userCandidate.user.username.value -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN)
                }

                else -> {
                    UserID(
                        database.runQuery {
                            QueryDsl
                                .insert(userSchema)
                                .single(
                                    userCandidate.toUser(
                                        passwordEncoder.encode(
                                            userCandidate.password.value
                                        )
                                    )
                                )
                                .returning()
                        }.id
                    )
                }
            }
        }

    override suspend fun invalidateAuthenticationToken(tokenID: TokenID) {
        val authenticationTokenSchema = Meta.authenticationToken

        database.runQuery {
            QueryDsl
                .update(authenticationTokenSchema)
                .set { authenticationTokenSchema.isActive eq false }
                .where { authenticationTokenSchema.id eq tokenID.value }
        }
    }

    override suspend fun getUser(userID: UserID): User = database.runQuery {
        val userSchema = Meta.user

        QueryDsl
            .from(userSchema)
            .selectAsEntity(userSchema)
            .singleOrNull()
    }?.toDomainUser() ?: throw IllegalArgumentException(ERROR_MESSAGE_USER_NOT_FOUND)

    override suspend fun updateUser(updatableUserCandidate: UpdatableUserCandidate): User =
        database.withTransaction {
            val userSchema = Meta.user

            val hashedPassword = database.runQuery {
                QueryDsl
                    .from(userSchema)
                    .where { userSchema.id eq updatableUserCandidate.id.value }
                    .select(userSchema.password)
                    .singleOrNull()
            } ?: throw IllegalArgumentException(ERROR_MESSAGE_USER_NOT_FOUND)

            if (passwordEncoder.matches(
                    updatableUserCandidate.currentPassword.value,
                    hashedPassword
                ).not()
            ) {
                throw IllegalArgumentException(ERROR_MESSAGE_USER_INVALID_PASSWORD)
            }

            val usernameFilter: WhereDeclaration =
                { userSchema.username eq updatableUserCandidate.user.username.value }
            val emailFilter: WhereDeclaration =
                { userSchema.email eq updatableUserCandidate.user.email.value }
            val idFilter: WhereDeclaration = {
                userSchema.id notEq updatableUserCandidate.id.value
            }

            database.runQuery {
                QueryDsl
                    .from(userSchema)
                    .where(idFilter.and(usernameFilter.or(emailFilter)))
                    .distinct()
                    .select(userSchema.username, userSchema.email)
                    .collect { result ->
                        result.collect { resultRecord ->
                            when {
                                resultRecord.first
                                        == updatableUserCandidate.user.username.value -> {
                                    throw IllegalArgumentException(
                                        ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN
                                    )
                                }

                                resultRecord.second
                                        == updatableUserCandidate.user.email.value -> {
                                    throw IllegalArgumentException(
                                        ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN
                                    )
                                }
                            }
                        }
                    }
            }

            database.runQuery {
                val updateQuery = QueryDsl.update(userSchema)

                val updateQueryWithExclusions =
                    if (updatableUserCandidate.newPassword != null) {
                        updateQuery.exclude(userSchema.password)
                    } else {
                        updateQuery
                    }

                updateQueryWithExclusions
                    .single(
                        updatableUserCandidate.toUser(
                            storedPassword = updatableUserCandidate.newPassword?.let {
                                passwordEncoder.encode(it.value)
                            },
                        )
                    )
                    .returning()
            }?.toDomainUser() ?: throw IllegalArgumentException(ERROR_MESSAGE_USER_NOT_FOUND)
        }

    override suspend fun deleteUser(password: Password, userID: UserID) {
        val userSchema = Meta.user
        val authenticationTokenSchema = Meta.authenticationToken

        database.withTransaction {
            database.runQuery {
                QueryDsl
                    .from(userSchema)
                    .where { userSchema.id eq userID.value }
                    .select(userSchema.password)
            }.firstOrNull()?.let { storedPassword ->
                if (passwordEncoder.matches(password.value, storedPassword).not()) {
                    throw IllegalArgumentException(ERROR_MESSAGE_USER_INVALID_PASSWORD)
                }
            } ?: throw IllegalArgumentException(ERROR_MESSAGE_USER_NOT_FOUND)

            database.runQuery {
                QueryDsl
                    .update(authenticationTokenSchema)
                    .set { authenticationTokenSchema.isActive eq false }
                    .where { authenticationTokenSchema.userID eq userID.value }
            }

            database.runQuery {
                QueryDsl
                    .delete(userSchema)
                    .where { userSchema.id eq userID.value }
            }
        }
    }

    override suspend fun checkAuthenticationTokenIsBlacklisted(tokenID: TokenID): Boolean =
        database.runQuery {
            val authenticationTokenSchema = Meta.authenticationToken

            QueryDsl
                .from(authenticationTokenSchema)
                .where { authenticationTokenSchema.id eq tokenID.value }
                .select(authenticationTokenSchema.isActive)
                .firstOrNull()
        } ?: throw IllegalArgumentException(ERROR_MESSAGE_TOKEN_NOT_FOUND)

    override suspend fun getUserRecommendationIndexAndBirthDate(
        userID: UserID?,
    ): Pair<Int, DateOfBirth> = userID?.let {
        database.withTransaction {
            val userSchema = Meta.user

            database.runQuery {
                QueryDsl
                    .from(userSchema)
                    .where { userSchema.id eq userID.value }
                    .firstOrNull()
            }?.let {
                if (it.recommendationIndex != null) {
                    it.recommendationIndex to DateOfBirth(it.dateOfBirth.toKotlinLocalDate())
                } else {
                    getDefaultRecommendationIndex() to DateOfBirth(
                        it.dateOfBirth.toKotlinLocalDate()
                    )
                }
            } ?: throw IllegalArgumentException(ERROR_MESSAGE_USER_NOT_FOUND)
        }
    } ?: run {
        getDefaultRecommendationIndex() to DateOfBirth(
            LocalDate(
                year = DEFAULT_DATE_OF_BIRTH_YEAR,
                monthNumber = DEFAULT_DATE_OF_BIRTH_MONTH,
                dayOfMonth = DEFAULT_DATE_OF_BIRTH_DAY,
            )
        )
    }

    private suspend fun getDefaultRecommendationIndex(): Int = database.runQuery {
        val userSchema = Meta.user

        QueryDsl
            .from(userSchema)
            .select(max(userSchema.recommendationIndex))
    }?.let { it + 1 } ?: 0

    companion object {

        private const val DEFAULT_DATE_OF_BIRTH_YEAR = 1996
        private const val DEFAULT_DATE_OF_BIRTH_MONTH = 1
        private const val DEFAULT_DATE_OF_BIRTH_DAY = 15
    }
}