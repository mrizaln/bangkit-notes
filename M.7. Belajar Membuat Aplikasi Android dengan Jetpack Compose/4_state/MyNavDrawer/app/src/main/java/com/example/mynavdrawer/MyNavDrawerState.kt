package com.example.mynavdrawer

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MyNavDrawerState(
    val drawerState: DrawerState,
    val snackbarHostState: SnackbarHostState,
    private val scope: CoroutineScope,
    private val context: Context
) {
    fun onMenuClick() {
        scope.launch {
            drawerState.open()
        }
    }

    fun onItemSelected(title: String) {
        scope.launch {
            drawerState.close()
            val snackbarResult = snackbarHostState.showSnackbar(
                message = context.resources.getString(R.string.coming_soon, title),
                actionLabel = context.resources.getString(R.string.subscribe_question),
                true,
                SnackbarDuration.Short
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.subscribed_info),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onBackPress() {
        if (drawerState.isOpen)
            scope.launch { drawerState.close() }
    }
}
