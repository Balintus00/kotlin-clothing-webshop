package hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement

import hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.HUNGARIAN_ABC_LETTERS
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.jvm.JvmInline

@JvmInline
@Suppress("MemberVisibilityCanBePrivate")
value class UserID(val value: String)

@JvmInline
@Suppress("MemberVisibilityCanBePrivate")
value class Username(val value: String) {

    init {
        require(value.length >= MINIMUM_LENGTH) { TOO_SHORT_ERROR_MESSAGE }
        require(value.length <= MAXIMUM_LENGTH) { TOO_LONG_ERROR_MESSAGE }

        require(value.all { it.lowercase() in HUNGARIAN_ABC_LETTERS || it.isDigit() }) {
            INVALID_CHARACTER_ERROR_MESSAGE
        }
    }

    companion object {
        const val MINIMUM_LENGTH = 3
        const val MAXIMUM_LENGTH = 63

        const val TOO_SHORT_ERROR_MESSAGE =
            "Username must be at least $MINIMUM_LENGTH characters long!"
        const val TOO_LONG_ERROR_MESSAGE =
            "Username must be at most $MAXIMUM_LENGTH characters long!"
        const val INVALID_CHARACTER_ERROR_MESSAGE =
            "Username can only contain hungarian ABC letters and numbers!"
    }
}

@JvmInline
@Suppress("MemberVisibilityCanBePrivate")
value class Email(val value: String) {

    init {
        require(value.length <= TOTAL_MAXIMUM_LENGTH) { TOO_LONG_ERROR_MESSAGE }
        require(value.all { (it in PROHIBITED_CHARACTERS).not() }) {
            CONTAINS_PROHIBITED_CHARACTER_ERROR_MESSAGE
        }

        val emailParts = value.split('@')
        require(emailParts.size == 2) { INVALID_SEPARATOR_COUNT_ERROR_MESSAGE }

        val localPart = emailParts[0]
        require(
            localPart.length in LOCAL_PART_MINIMUM_LENGTH..LOCAL_PART_MAXIMUM_LENGTH
        ) {
            INVALID_LOCAL_PART_LENGTH_ERROR_MESSAGE
        }

        val domainPart = emailParts[1]
        require(
            domainPart.all { it.isLetterOrDigit() || it in DOMAIN_ALLOWED_SPECIAL_CHARACTERS }
        ) {
            INVALID_DOMAIN_PART_CHARACTER_ERROR_MESSAGE
        }
    }

    companion object {
        const val TOTAL_MAXIMUM_LENGTH = 254

        private const val LOCAL_PART_MINIMUM_LENGTH = 1
        private const val LOCAL_PART_MAXIMUM_LENGTH = 63

        private const val PROHIBITED_CHARACTERS = "`'\"" + 0.toChar()

        private const val DOMAIN_ALLOWED_SPECIAL_CHARACTERS = "-."

        const val TOO_LONG_ERROR_MESSAGE = "Email address is too long!"
        const val CONTAINS_PROHIBITED_CHARACTER_ERROR_MESSAGE =
            "Email address contains prohibited characters!"
        const val INVALID_SEPARATOR_COUNT_ERROR_MESSAGE =
            "Email address must contain exactly 1 @ character!"
        const val INVALID_LOCAL_PART_LENGTH_ERROR_MESSAGE = "Invalid local part length!"
        const val INVALID_DOMAIN_PART_CHARACTER_ERROR_MESSAGE = "Invalid email address!"

        val ERROR_MESSAGES = setOf(
            TOO_LONG_ERROR_MESSAGE,
            CONTAINS_PROHIBITED_CHARACTER_ERROR_MESSAGE,
            INVALID_SEPARATOR_COUNT_ERROR_MESSAGE,
            INVALID_LOCAL_PART_LENGTH_ERROR_MESSAGE,
            INVALID_DOMAIN_PART_CHARACTER_ERROR_MESSAGE,
        )
    }
}

@JvmInline
@Suppress("MemberVisibilityCanBePrivate")
value class Password(val value: String) {

    init {
        require(value.length >= MINIMUM_LENGTH) { TOO_SHORT_ERROR_MESSAGE }
        require(value.length <= MAXIMUM_LENGTH) { TOO_LONG_ERROR_MESSAGE }
    }

    companion object {
        const val MINIMUM_LENGTH = 12
        const val MAXIMUM_LENGTH = 127

        const val TOO_SHORT_ERROR_MESSAGE = "Password length is too short!"
        const val TOO_LONG_ERROR_MESSAGE = "Password length is too long!"
    }
}

@JvmInline
@Suppress("MemberVisibilityCanBePrivate")
value class FirstName(val value: String) {

    init {
        require(value.length >= MINIMUM_LENGTH) { TOO_SHORT_ERROR_MESSAGE }
        require(value.length <= MAXIMUM_LENGTH) { TOO_LONG_ERROR_MESSAGE }
        require(value.all { it.lowercase() in HUNGARIAN_ABC_LETTERS }) {
            INVALID_CHARACTER_ERROR_MESSAGE
        }
    }

    companion object {
        const val MINIMUM_LENGTH = 3
        const val MAXIMUM_LENGTH = 63

        const val TOO_SHORT_ERROR_MESSAGE =
            "First name must be at least $MINIMUM_LENGTH characters long!"
        const val TOO_LONG_ERROR_MESSAGE =
            "First name must be at most $MAXIMUM_LENGTH characters long!"
        const val INVALID_CHARACTER_ERROR_MESSAGE = "First name contains invalid character!"
    }
}

@JvmInline
@Suppress("MemberVisibilityCanBePrivate")
value class LastName(val value: String) {
    init {
        require(value.length >= MINIMUM_LENGTH) { TOO_SHORT_ERROR_MESSAGE }
        require(value.length <= MAXIMUM_LENGTH) { TOO_LONG_ERROR_MESSAGE }

        require(
            value.all { it.lowercase() in (HUNGARIAN_ABC_LETTERS + LAST_NAME_SPECIAL_CHARACTERS) }
        ) {
            INVALID_CHARACTER_ERROR_MESSAGE
        }
    }

    companion object {
        const val MINIMUM_LENGTH = 3
        const val MAXIMUM_LENGTH = 63

        private const val LAST_NAME_SPECIAL_CHARACTERS = "-'"

        const val TOO_SHORT_ERROR_MESSAGE =
            "Last name must be at least $MINIMUM_LENGTH characters long!"
        const val TOO_LONG_ERROR_MESSAGE =
            "Last name must be at most $MAXIMUM_LENGTH characters long!"
        const val INVALID_CHARACTER_ERROR_MESSAGE = "Last name contains invalid character!"
    }
}

@JvmInline
@Suppress("MemberVisibilityCanBePrivate")
value class DateOfBirth(val value: LocalDate) {

    init {
        require(
            value >= LocalDate(
                year = EARLIEST_ALLOWED_YEAR,
                monthNumber = EARLIEST_ALLOWED_MONTH,
                dayOfMonth = EARLIEST_ALLOWED_DAY,
            )
        ) { TOO_EARLY_DATE_ERROR_MESSAGE }

        require(
            value <= Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
                .minus(DatePeriod(years = LATEST_ALLOWED_YEAR_DIFFERENCE))
        ) { TOO_LATE_DATE_ERROR_MESSAGE }
    }

    companion object {
        const val EARLIEST_ALLOWED_YEAR = 1900
        const val EARLIEST_ALLOWED_MONTH = 1
        const val EARLIEST_ALLOWED_DAY = 1

        const val LATEST_ALLOWED_YEAR_DIFFERENCE = 8

        const val TOO_EARLY_DATE_ERROR_MESSAGE =
            "Earliest supported date is the following in ISO8601 format: " +
                    "${EARLIEST_ALLOWED_YEAR}-${EARLIEST_ALLOWED_MONTH}-${EARLIEST_ALLOWED_DAY}!"
        const val TOO_LATE_DATE_ERROR_MESSAGE = "Last supported date is before exactly " +
                "$LATEST_ALLOWED_YEAR_DIFFERENCE before the current date!"
    }
}