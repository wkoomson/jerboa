package com.jerboa.ui.components.community.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.jerboa.R
import com.jerboa.datatypes.sampleCommunityView
import com.jerboa.datatypes.types.*
import com.jerboa.db.entity.SearchHistory
import com.jerboa.ui.components.common.simpleVerticalScrollbar
import com.jerboa.ui.components.community.CommunityLinkLarger
import com.jerboa.ui.components.community.CommunityLinkLargerWithUserCount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityListHeader(
    openDrawer: () -> Unit,
    search: String,
    onSearchChange: (search: String) -> Unit,
) {
    TopAppBar(
        title = {
            CommunityTopBarSearchView(
                search = search,
                onSearchChange = onSearchChange,
            )
        },
        actions = {
            IconButton(
                onClick = { // TODO
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = stringResource(R.string.moreOptions),
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    Icons.Outlined.Menu,
                    contentDescription = stringResource(R.string.home_menu),
                )
            }
        },
    )
}

@Composable
fun CommunityListings(
    communities: List<CommunityView>,
    onClickCommunity: (community: Community) -> Unit,
    modifier: Modifier = Modifier,
    blurNSFW: Boolean,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier.simpleVerticalScrollbar(listState),
    ) {
        items(
            communities,
            key = { it.community.id },
        ) { item ->
            // A hack for the community follower views that were coerced into community views without counts
            if (item.counts.users_active_month == 0) {
                CommunityLinkLarger(
                    community = item.community,
                    onClick = onClickCommunity,
                    showDefaultIcon = true,
                    blurNSFW = blurNSFW,
                )
            } else {
                CommunityLinkLargerWithUserCount(
                    communityView = item,
                    onClick = onClickCommunity,
                    showDefaultIcon = true,
                    blurNSFW = blurNSFW,
                )
            }
        }
    }
}

@Preview
@Composable
fun CommunityListingsPreview() {
    val communities = listOf(sampleCommunityView, sampleCommunityView)
    CommunityListings(
        communities = communities,
        onClickCommunity = {},
        blurNSFW = true,
    )
}

@Composable
fun CommunityTopBarSearchView(
    search: String,
    onSearchChange: (search: String) -> Unit,
) {
    TextField(
        value = search,
        onValueChange = onSearchChange,
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = {
            Text(stringResource(R.string.community_list_search))
        },
        modifier = Modifier
            .fillMaxWidth(),
        trailingIcon = {
            if (search.isNotEmpty()) {
                IconButton(
                    onClick = { onSearchChange("") },
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = "",
                    )
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,

        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    )
}

@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    CommunityTopBarSearchView(
        search = "",
        onSearchChange = {},
    )
}

@Composable
fun SearchHistoryList(
    history: List<SearchHistory>,
    onHistoryItemClicked: (SearchHistory) -> Unit,
    onHistoryItemDeleted: (SearchHistory) -> Unit,
) {
    Column {
        if (history.isNotEmpty()) {
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(R.string.community_list_recent_searches),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelLarge,
                    )
                },
            )
        }
        history.forEach {
            ListItem(
                modifier = Modifier.clickable { onHistoryItemClicked(it) },
                headlineContent = {
                    Text(
                        text = it.searchTerm,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                trailingContent = {
                    IconButton(
                        onClick = { onHistoryItemDeleted(it) },
                        content = {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = stringResource(
                                    R.string.community_list_delete_search_item,
                                    it.searchTerm,
                                ),
                                tint = MaterialTheme.colorScheme.surfaceTint,
                            )
                        },
                    )
                },
            )
        }
    }
}
