package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.entity.authenticationToken
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.entity.toDomainUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.entity.toKomapperEntity
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.entity.toUser
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.entity.user
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UpdatableUserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.User
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserCandidate
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.datasource.UserDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.datasource.UserDatasource.Companion.ERROR_MESSAGE_TOKEN_NOT_FOUND
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.datasource.UserDatasource.Companion.ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.datasource.UserDatasource.Companion.ERROR_MESSAGE_USER_INVALID_PASSWORD
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.datasource.UserDatasource.Companion.ERROR_MESSAGE_USER_NOT_FOUND
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.datasource.UserDatasource.Companion.ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Email
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.Password
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.TokenID
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.UserID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.expression.WhereDeclaration
import org.komapper.core.dsl.operator.and
import org.komapper.core.dsl.operator.or
import org.komapper.core.dsl.query.firstOrNull
import org.komapper.core.dsl.query.singleOrNull
import org.komapper.r2dbc.R2dbcDatabase
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import java.util.*
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement.AuthenticationToken as DomainAuthenticationToken

internal class DefaultUserDatasource(
    private val database: R2dbcDatabase,
    private val passwordEncoder: Argon2PasswordEncoder,
) : UserDatasource {

    private val datasourceScope = CoroutineScope(Dispatchers.Default)

    private val isDatabaseInitialized = MutableStateFlow(false)

    init {
        datasourceScope.launch {
            database.runQuery {
                QueryDsl.create(Meta.user, Meta.authenticationToken)
            }

            isDatabaseInitialized.update { true }
        }
    }

    override suspend fun authenticateUser(email: Email, password: Password): UserID =
        executeAfterDatabaseIsInitialized {
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
        }

    private suspend inline fun <T> executeAfterDatabaseIsInitialized(block: () -> T): T {
        if (isDatabaseInitialized.value.not()) {
            isDatabaseInitialized.first { it }
        }

        return block()
    }

    override suspend fun createAuthenticationToken(userID: UserID): DomainAuthenticationToken =
        executeAfterDatabaseIsInitialized {
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

    override suspend fun saveNewUser(userCandidate: UserCandidate): UserID = executeAfterDatabaseIsInitialized {
        database.withTransaction {
            val userSchema = Meta.user

            val conflictingUser = database.runQuery {
                val emailFilter: WhereDeclaration = { userSchema.email eq userCandidate.user.email.value }
                val usernameFilter: WhereDeclaration = { userSchema.username eq userCandidate.user.username.value }

                QueryDsl
                    .from(userSchema)
                    .where(emailFilter.or(usernameFilter))
            }.firstOrNull()

            when {
                conflictingUser != null && conflictingUser.email == userCandidate.user.email.value -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN)
                }

                conflictingUser != null && conflictingUser.username == userCandidate.user.username.value -> {
                    throw IllegalArgumentException(ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN)
                }

                else -> {
                    UserID(
                        database.runQuery {
                            QueryDsl
                                .insert(userSchema)
                                .single(userCandidate.toUser(passwordEncoder.encode(userCandidate.password.value)))
                                .returning()
                        }.id
                    )
                }
            }
        }
    }

    override suspend fun invalidateAuthenticationToken(tokenID: TokenID) {
        executeAfterDatabaseIsInitialized {
            val authenticationTokenSchema = Meta.authenticationToken

            database.runQuery {
                QueryDsl
                    .update(authenticationTokenSchema)
                    .set { authenticationTokenSchema.isActive eq false }
                    .where { authenticationTokenSchema.id eq tokenID.value }
            }
        }
    }

    override suspend fun getUser(userID: UserID): User = executeAfterDatabaseIsInitialized {
        database.runQuery {
            val userSchema = Meta.user

            QueryDsl
                .from(userSchema)
                .selectAsEntity(userSchema)
                .singleOrNull()
        }?.toDomainUser() ?: throw IllegalArgumentException(ERROR_MESSAGE_USER_NOT_FOUND)
    }

    override suspend fun updateUser(updatableUserCandidate: UpdatableUserCandidate): User =
        executeAfterDatabaseIsInitialized {
            database.withTransaction {
                val userSchema = Meta.user

                val hashedPassword = database.runQuery {
                    QueryDsl
                        .from(userSchema)
                        .where { userSchema.id eq updatableUserCandidate.id.value }
                        .select(userSchema.password)
                        .singleOrNull()
                } ?: throw IllegalArgumentException(ERROR_MESSAGE_USER_NOT_FOUND)

                if (passwordEncoder.matches(updatableUserCandidate.currentPassword.value, hashedPassword).not()) {
                    throw IllegalArgumentException(ERROR_MESSAGE_USER_INVALID_PASSWORD)
                }

                val usernameFilter: WhereDeclaration =
                    { userSchema.username eq updatableUserCandidate.userCandidate.user.username.value }
                val emailFilter: WhereDeclaration =
                    { userSchema.email eq updatableUserCandidate.userCandidate.user.email.value }
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
                                    resultRecord.first == updatableUserCandidate.userCandidate.user.username.value -> {
                                        throw IllegalArgumentException(ERROR_MESSAGE_USER_USERNAME_ALREADY_TAKEN)
                                    }

                                    resultRecord.second == updatableUserCandidate.userCandidate.user.email.value -> {
                                        throw IllegalArgumentException(ERROR_MESSAGE_USER_EMAIL_ALREADY_TAKEN)
                                    }
                                }
                            }
                        }
                }

                database.runQuery {
                    QueryDsl
                        .update(userSchema)
                        .single(
                            updatableUserCandidate.userCandidate.toUser(
                                id = updatableUserCandidate.id.value,
                                storedPassword = passwordEncoder.encode(
                                    updatableUserCandidate.userCandidate.password.value
                                ),
                            )
                        )
                        .returning()
                }?.toDomainUser() ?: throw IllegalArgumentException(ERROR_MESSAGE_USER_NOT_FOUND)
            }
        }

    override suspend fun deleteUser(userID: UserID) {
        executeAfterDatabaseIsInitialized {
            val userSchema = Meta.user
            val authenticationTokenSchema = Meta.authenticationToken

            database.withTransaction {
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
    }

    override suspend fun checkAuthenticationTokenIsBlacklisted(tokenID: TokenID): Boolean =
        executeAfterDatabaseIsInitialized {
            database.runQuery {
                val authenticationTokenSchema = Meta.authenticationToken

                QueryDsl
                    .from(authenticationTokenSchema)
                    .where { authenticationTokenSchema.id eq tokenID.value }
                    .select(authenticationTokenSchema.isActive)
                    .firstOrNull()
            } ?: throw IllegalArgumentException(ERROR_MESSAGE_TOKEN_NOT_FOUND)
        }
}