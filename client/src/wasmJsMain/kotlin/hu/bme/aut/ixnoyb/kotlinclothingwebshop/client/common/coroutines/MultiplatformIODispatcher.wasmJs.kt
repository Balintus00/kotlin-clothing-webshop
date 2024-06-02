package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// TODO look for better alternative or update solution when IO Dispatcher will be available
internal actual val multiplatformIODispatcher: CoroutineDispatcher = Dispatchers.Default