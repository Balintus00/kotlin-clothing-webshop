package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.usermanagement.ui.element

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import kotlinclothingwebshop.client.generated.resources.Res
import kotlinclothingwebshop.client.generated.resources.date_picker_dialog_cancel_button_text
import kotlinclothingwebshop.client.generated.resources.date_picker_dialog_confirm_button_text
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

// TODO: on Medium and Expanded with a dropdown should be displayed.
//  But it seems currently it is not supported, so dialog is used with every width.
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
internal fun DateOfBirthPickerDialog(
    earliestAllowedDate: LocalDate,
    lastAllowedDate: LocalDate,
    closeDialogAction: () -> Unit = {},
    selectDateAction: (LocalDate) -> Unit = {},
) {
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input,
        selectableDates = object : SelectableDates {

            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val earliestAllowedUtcMillis =
                    earliestAllowedDate.toEpochDays().toLong() * 24 * 60 * 60 * 1000

                val lastAllowedUtcMillis =
                    lastAllowedDate.toEpochDays().toLong() * 24 * 60 * 60 * 1000

                return utcTimeMillis in earliestAllowedUtcMillis..lastAllowedUtcMillis
            }

            override fun isSelectableYear(year: Int): Boolean {

                return year in earliestAllowedDate.year..lastAllowedDate.year
            }
        },
        yearRange = earliestAllowedDate.year..lastAllowedDate.year
    )

    DatePickerDialog(
        confirmButton = {
            TextButton(
                enabled = datePickerState.selectedDateMillis != null,
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectDateAction(
                            Instant.fromEpochMilliseconds(it)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .date
                        )

                        closeDialogAction()
                    }
                },
            ) {
                Text(stringResource(Res.string.date_picker_dialog_confirm_button_text))
            }
        },
        dismissButton = {
            TextButton(
                onClick = closeDialogAction,
            ) {
                Text(stringResource(Res.string.date_picker_dialog_cancel_button_text))
            }
        },
        onDismissRequest = closeDialogAction,
    ) {
        DatePicker(datePickerState)
    }
}