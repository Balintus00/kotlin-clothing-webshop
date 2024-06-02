package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

internal actual fun Int.formatAmount(): String {
    val formatter = NSNumberFormatter()
    formatter.numberStyle = 1u  // decimal
    return formatter.stringFromNumber(NSNumber(this)) ?: toString()
}