package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.common.ui

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun LocalDate.toDisplayDateFormat(): String = format(getLocalDateFormat())

private fun getLocalDateFormat(): DateTimeFormat<LocalDate> = LocalDate.Format {
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    dayOfMonth()
    char(' ')
    year()
}

fun LocalDateTime.toDisplayDateTimeFormat(): String = format(
    LocalDateTime.Format {
        date(
            getLocalDateFormat()
        )
        char(' ')
        time(
            LocalTime.Format {
                hour()
                char(':')
                minute()
            }
        )
    }
)