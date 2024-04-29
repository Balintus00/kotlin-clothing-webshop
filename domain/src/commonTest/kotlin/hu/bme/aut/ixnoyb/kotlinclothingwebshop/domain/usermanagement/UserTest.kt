package hu.bme.aut.ixnoyb.kotlinclothingwebshop.domain.usermanagement

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class UserTest {

    @Test
    fun usernameShouldContainValidValue() {
        @Suppress("SpellCheckingInspection") val validUsernameValue = "Balintus00"

        val username = Username(validUsernameValue)

        assertEquals(validUsernameValue, username.value)
    }

    @Test
    fun usernameWithSpecialHungarianLettersShouldContainValidValue() {
        @Suppress("SpellCheckingInspection") val validUsernameValue = "BálintÚs00"

        val username = Username(validUsernameValue)

        assertEquals(validUsernameValue, username.value)
    }

    @Test
    fun tooShortUsernameShouldThrowIllegalArgumentExceptionWithRightMessage() {
        val shortUsername = "B"

        try {
            Username(shortUsername)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(Username.INVALID_LENGTH_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun tooLongUsernameShouldThrowIllegalArgumentExceptionWithRightMessage() {
        val shortUsername = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB"

        try {
            Username(shortUsername)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(Username.INVALID_LENGTH_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun invalidCharacterContainerUsernameShouldThrowIllegalArgumentExceptionWithRightMessage() {
        @Suppress("SpellCheckingInspection") val invalidCharacterContainerUsername = "ßßßß"

        try {
            Username(invalidCharacterContainerUsername)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(Username.INVALID_CHARACTER_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun validEmailShouldContainValidValue() {
        val emailValue = "traxlerbalint@gmail.com"

        val email = Email(emailValue)

        assertEquals(emailValue, email.value)
    }

    @Test
    fun emailWithoutSeparatorShouldThrowExceptionWithRightMessage() {
        @Suppress("SpellCheckingInspection") val email = "traxlerbalintgmail.com"

        try {
            Email(email)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(Email.INVALID_SEPARATOR_COUNT_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun emailWithTooManySeparatorShouldThrowExceptionWithRightMessage() {
        val email = "traxler@balint@gmail.com"

        try {
            Email(email)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(Email.INVALID_SEPARATOR_COUNT_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun tooLongEmailShouldThrowExceptionWithRightMessage() {
        val email = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaa@t.c"

        try {
            Email(email)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(Email.INVALID_LENGTH_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun backtickContainingEmailShouldThrowExceptionWithRightMessage() {
        @Suppress("SpellCheckingInspection") val email = "traxlerba`lint@gmail.com"

        try {
            Email(email)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(Email.CONTAINS_PROHIBITED_CHARACTER_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun emailWithoutLocalPartShouldThrowExceptionWithRightMessage() {
        val email = "@gmail.com"

        try {
            Email(email)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(Email.INVALID_LOCAL_PART_LENGTH_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun emailWith64LongLocalPartShouldThrowExceptionWithRightMessage() {
        val email = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb@gmail.com"

        try {
            Email(email)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(Email.INVALID_LOCAL_PART_LENGTH_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun emailWithPlusSignContainingDomainShouldThrowExceptionWithRightMessage() {
        val email = "traxlerbalint@gm+ail.com"

        try {
            Email(email)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(Email.INVALID_DOMAIN_PART_CHARACTER_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun hyphenAndNumberDomainContainerEmailShouldContainValidValue() {
        val emailValue = "traxlerbalint@gmail-2.com"

        val email = Email(emailValue)

        assertEquals(emailValue, email.value)
    }

    @Test
    fun validPasswordShouldContainValidValue() {
        val passwordValue = "SeCrEt2Ł#!á*¤óÜö\uD83D\uDE00"

        val password = Password(passwordValue)

        assertEquals(passwordValue, password.value)
    }

    @Test
    fun tooShortPasswordShouldThrowExceptionWithRightMessage() {
        val shortPassword = "a2b4c6d8e0f"

        try {
            Password(shortPassword)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(Password.INVALID_LENGTH_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun tooLongPasswordShouldThrowExceptionWithRightMessage() {
        val longPassword = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

        try {
            Password(longPassword)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(Password.INVALID_LENGTH_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun sixLongWithHungarianAbcLettersFirstNameShouldContainValidValue() {
        @Suppress("SpellCheckingInspection") val firstNameValue = "Bálint"

        val firstName = FirstName(firstNameValue)

        assertEquals(firstNameValue, firstName.value)
    }

    @Test
    fun emptyFirstNameShouldThrowExceptionWithRightMessage() {
        val firstNameValue = ""

        try {
            FirstName(firstNameValue)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, FirstName.INVALID_LENGTH_ERROR_MESSAGE)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun sixtyFourLongFirstNameShouldThrowExceptionWithRightMessage() {
        val firstNameValue = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

        try {
            FirstName(firstNameValue)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, FirstName.INVALID_LENGTH_ERROR_MESSAGE)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun sevenLongWithHungarianAbcLettersLastNameShouldContainValidValue() {
        @Suppress("SpellCheckingInspection") val lastNameValue = "Traxler"

        val lastName = FirstName(lastNameValue)

        assertEquals(lastNameValue, lastName.value)
    }

    @Test
    fun emptyLastNameShouldThrowExceptionWithRightMessage() {
        val lastNameValue = ""

        try {
            LastName(lastNameValue)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, LastName.INVALID_LENGTH_ERROR_MESSAGE)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun sixtyFourLongLastNameShouldThrowExceptionWithRightMessage() {
        val lastNameValue = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

        try {
            LastName(lastNameValue)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, LastName.INVALID_LENGTH_ERROR_MESSAGE)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun sevenLongApostropheAndHyphenContainerLastNameShouldContainValidValue() {
        @Suppress("SpellCheckingInspection") val lastNameValue = "T'ra-xl"

        val lastName = LastName(lastNameValue)

        assertEquals(lastNameValue, lastName.value)
    }

    @Test
    fun birthDateFromNinetiesShouldContainValidValue() {
        val date = LocalDate(year = 1990, monthNumber = 7, dayOfMonth = 15)

        val dateOfBirth = DateOfBirth(date)

        assertEquals(date, dateOfBirth.value)
    }

    @Test
    fun nineteenthCenturyBirthDateShouldThrowExceptionWithRightMessage() {
        val date = LocalDate(year = 1890, monthNumber = 7, dayOfMonth = 15)

        try {
            DateOfBirth(date)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(DateOfBirth.INVALID_DATE_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun exactlyTwoYearOldBirthDateShouldThrowExceptionWithRightMessage() {
        val date = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .minus(DatePeriod(years = 2))


        try {
            DateOfBirth(date)
            fail("No exception was thrown")
        } catch (e: IllegalArgumentException) {
            assertEquals(DateOfBirth.INVALID_DATE_ERROR_MESSAGE, e.message)
        } catch (t: Throwable) {
            fail("Exception with wrong type was thrown: $t")
        }
    }

    @Test
    fun idShouldContainValidValue() {
        val idValue = "ID"

        val id = UserID(idValue)

        assertEquals(idValue, id.value)
    }
}