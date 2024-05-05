package hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.articlebrowsing.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.bme.aut.ixnoyb.kotlinclothingwebshop.client.network.getRecommendedArticleIds
import kotlinx.coroutines.delay

private const val CUSTOMER_ID_FIRST_OPTION = "customerId1"
private const val CUSTOMER_ID_SECOND_OPTION = "customerId2"
private const val CUSTOMER_ID_THIRD_OPTION = "customerId3"

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun RecommendedArticlesScreenTopAppbar(
    selectCustomerIdAction: (String) -> Unit,
) {
    CenterAlignedTopAppBar(
        actions = {
            var isMenuDisplayed by remember { mutableStateOf(false) }

            IconButton(
                onClick = { isMenuDisplayed = isMenuDisplayed.not() }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                )
            }
            DropdownMenu(
                expanded = isMenuDisplayed,
                onDismissRequest = { isMenuDisplayed = false },
            ) {
                DropdownMenuItem(
                    onClick = {
                        selectCustomerIdAction(CUSTOMER_ID_FIRST_OPTION)
                        isMenuDisplayed = false
                    },
                    text = { Text(CUSTOMER_ID_FIRST_OPTION) },
                )
                DropdownMenuItem(
                    onClick = {
                        selectCustomerIdAction(CUSTOMER_ID_SECOND_OPTION)
                        isMenuDisplayed = false
                    },
                    text = { Text(CUSTOMER_ID_SECOND_OPTION) },
                )
                DropdownMenuItem(
                    onClick = {
                        selectCustomerIdAction(CUSTOMER_ID_THIRD_OPTION)
                        isMenuDisplayed = false
                    },
                    text = { Text(CUSTOMER_ID_THIRD_OPTION) },
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text("Kotlin Clothing Webshop")
        }
    )
}

@Composable
internal fun RecommendedArticlesScreen(
    customerId: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        var isLoading by remember { mutableStateOf(false) }
        var loadingResultMessage by remember { mutableStateOf("Not loaded anything yet") }

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = customerId, textAlign = TextAlign.Center)
            Text(text = loadingResultMessage, textAlign = TextAlign.Center)
        }

        LaunchedEffect(customerId) {
            try {
                isLoading = true
                delay(1000)
                val recommendedIds = getRecommendedArticleIds(customerId)
                loadingResultMessage =
                    "Successfully loaded recommended IDs\n$recommendedIds"
            } catch (t: Throwable) {
                t.printStackTrace()
                loadingResultMessage = "Failed to load recommended article IDs"
            } finally {
                isLoading = false
            }
        }
    }
}