package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui

internal actual fun Int.formatAmount(): String = toLocaleString(this)

@JsFun("(number) => number.toLocaleString()")
private external fun toLocaleString(number: Int): String