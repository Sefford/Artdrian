package com.sefford.artdrian.connectivity.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.app.NotificationManagerCompat
import com.sefford.artdrian.common.di.Application
import com.sefford.artdrian.connectivity.Connectivity
import com.sefford.artdrian.connectivity.ConnectivityStore
import com.sefford.artdrian.connectivity.ConnectivitySubscription
import com.sefford.artdrian.connectivity.DefaultConnectivitySubscription
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ConnectivityModule {

    @Provides
    @Singleton
    fun provideNotificationManager(@Application context: Context) = NotificationManagerCompat.from(context)

    @Provides
    @Singleton
    fun provideConnectivityManager(@Application context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideConnectivitySubscription(manager: ConnectivityManager): ConnectivitySubscription =
        DefaultConnectivitySubscription(manager)

    @Provides
    fun provideDefaultConnectivity(manager: ConnectivityManager): Connectivity =
        manager.getNetworkCapabilities(manager.activeNetwork)?.let { Connectivity(it) } ?: Connectivity.Undetermined

    @Provides
    @Singleton
    fun provideConnectivityStore(
        initial: Connectivity,
        subscription: ConnectivitySubscription,
    ): ConnectivityStore = ConnectivityStore(initial).also { subscription.start(it) }

}
