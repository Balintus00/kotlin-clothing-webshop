package hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.di

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.DefaultUserService
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.backend.usermanagement.domain.UserService
import org.koin.dsl.module

internal val userManagementModule = module {
    single<UserService> { DefaultUserService() }
}