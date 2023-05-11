package com.dicoding.jetreward.ui.navigation

import android.util.Log
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

//sealed class ScreenExtended(
//    private val baseRoute: String,
//    val arguments: List<NamedNavArgument> = emptyList(),
//) {
//    private val route: String = run {
//        var temp = baseRoute
//        arguments.forEach {
//            val (name) = it
//            temp += "/{$name}"
//            Log.d(javaClass.simpleName, "route: $temp")
//        }
//        temp
//    }
//
//    @Throws(IllegalArgumentException::class, IllegalStateException::class)
//    fun createRoute(vararg args: Any): String {
//        if (arguments.isEmpty())
//            throw IllegalStateException("Class member 'arguments' must not be empty to call this function. If 'arguments' is empty, you should not call this function then")
//
//        if (args.size != arguments.size)
//            throw IllegalArgumentException("The number of arguments passed to this function does not match the number of member class 'arguments'")
//
//        val mappedArgs = arguments.zip(args).toMap()
//        var generatedRoute = baseRoute
//
//        mappedArgs.forEach { (navArg, value) ->
//            generatedRoute += "/$value"
//        }
//
//        Log.d(javaClass.simpleName, "generatedRoute: $generatedRoute")
//        return generatedRoute
//    }
//
//    object Home : ScreenExtended("home")
//    object Cart : ScreenExtended("cart")
//    object Profile : ScreenExtended("profile")
//
//    object DetailReward : ScreenExtended(
//        "home",
//        listOf(navArgument("rewardId") { type = NavType.LongType })
//    )
//}

sealed class Screen(
    var route: String,
) {
    object Home : Screen("home")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object DetailReward : Screen("home/{rewardId}") {
        fun createRoute(rewardId: Long): String = "home/$rewardId"
    }
}
