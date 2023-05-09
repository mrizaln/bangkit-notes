package com.example.mynavdrawer

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynavdrawer.ui.theme.MyNavDrawerTheme
import kotlinx.coroutines.CoroutineScope

data class MenuItem(
    val title: String,
    val icon: ImageVector,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberMyNavDrawerState(
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    context: Context = LocalContext.current,
): MyNavDrawerState = remember(drawerState, snackbarHostState, coroutineScope, context) {
    MyNavDrawerState(drawerState, snackbarHostState, coroutineScope, context)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNavDrawerApp() {
    val appState = rememberMyNavDrawerState()

    ModalNavigationDrawer(
        drawerState = appState.drawerState,
        drawerContent = {
            MyDrawerContent(
                onItemSelected = appState::onItemSelected,
                onBackPress = appState::onBackPress
            )
        },
        gesturesEnabled = appState.drawerState.isOpen,
        modifier = Modifier.fillMaxHeight()
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(appState.snackbarHostState) },
            topBar = {
                MyTopBar(onMenuClick = appState::onMenuClick)
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.hello_world))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    onMenuClick: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                onMenuClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu),
                )
            }
        },
        title = { Text(stringResource(R.string.app_name)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDrawerContent(
    modifier: Modifier = Modifier,
    onItemSelected: (title: String) -> Unit,
    onBackPress: () -> Unit,
) {
    ModalDrawerSheet {
        val items = listOf(
            MenuItem(
                title = stringResource(R.string.home),
                icon = Icons.Default.Home
            ),
            MenuItem(
                title = stringResource(R.string.favourite),
                icon = Icons.Default.Favorite
            ),
            MenuItem(
                title = stringResource(R.string.profile),
                icon = Icons.Default.AccountCircle
            ),
        )
        Column(
            modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(190.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
            for (item in items) {
                Row(
                    modifier = Modifier
                        .clickable { onItemSelected(item.title) }
                        .padding(
                            vertical = 12.dp,
                            horizontal = 16.dp
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            Divider()
        }
    }
    BackPressHandler {
        onBackPress()
    }

    // wrapper bawaan
//    BackHandler(true) {
//        onBackPress()
//    }
}

@Composable
fun BackPressHandler(
    enabled: Boolean = true,
    onBackPressed: () -> Unit,
) {
    val currentOnBackPressed by rememberUpdatedState(onBackPressed)
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    SideEffect { backCallback.isEnabled = enabled }

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

//---------------------------------------------[ preview ]----------------------------------------

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyNavDrawerTheme {
        MyNavDrawerApp()
    }
}
