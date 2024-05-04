package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.di

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.DefaultUserService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.datasource.UserDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.DefaultUserDatasource
import org.koin.dsl.module
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

internal val userManagementModule = module {
    single<Argon2PasswordEncoder> { Argon2PasswordEncoder(32, 32, 1, 9216, 4) }
    single<UserService> { DefaultUserService(datasource = get()) }
    single<UserDatasource> { DefaultUserDatasource(database = get(), passwordEncoder = get() ) }
}