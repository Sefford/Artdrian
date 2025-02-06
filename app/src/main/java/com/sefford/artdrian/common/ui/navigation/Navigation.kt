package com.sefford.artdrian.common.ui.navigation

import androidx.navigation.NavHostController

fun NavHostController.navigate(route: () -> Routes) = navigate(route().destination)
