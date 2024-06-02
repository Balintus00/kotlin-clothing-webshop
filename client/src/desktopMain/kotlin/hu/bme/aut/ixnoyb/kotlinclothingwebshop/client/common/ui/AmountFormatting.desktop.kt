package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui

import java.text.DecimalFormat

internal actual fun Int.formatAmount(): String = DecimalFormat("#,###").format(this)