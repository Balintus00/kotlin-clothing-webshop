package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual val multiplatformIODispatcher: CoroutineDispatcher = Dispatchers.IO