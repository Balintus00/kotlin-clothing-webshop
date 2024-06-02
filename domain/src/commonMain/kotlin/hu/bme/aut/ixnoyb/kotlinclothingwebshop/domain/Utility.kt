package hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain

private const val HUNGARIAN_LOWERCASE_ABC_LETTERS = "aábcdeéfghiíjklmnoóöőpqrstuúüűvwxyz"

internal val HUNGARIAN_ABC_LETTERS = HUNGARIAN_LOWERCASE_ABC_LETTERS + HUNGARIAN_LOWERCASE_ABC_LETTERS.map { it.uppercase() }.joinToString()

internal val NEW_LINE_SEPARATORS = setOf('\n', '\r', '\u0085', '\u2028', '\u2029')