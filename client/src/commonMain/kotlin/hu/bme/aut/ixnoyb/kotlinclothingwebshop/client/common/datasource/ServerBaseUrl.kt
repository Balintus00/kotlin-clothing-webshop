package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.datasource

internal fun getServerBaseUrl() = "http://${getServerDomain()}:5400"

internal expect fun getServerDomain(): String