package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.di

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.DefaultUserService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.UserService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.service.datasource.UserDatasource
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.datasource.KomapperUserDatasource
import org.koin.dsl.module
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

internal val userManagementModule = module {
    single<PasswordEncoder> { Argon2PasswordEncoder(32, 32, 1, 9216, 4) }
    single<UserService> { DefaultUserService(datasource = get()) }
    single<UserDatasource> { KomapperUserDatasource(database = get(), passwordEncoder = get() ) }
}