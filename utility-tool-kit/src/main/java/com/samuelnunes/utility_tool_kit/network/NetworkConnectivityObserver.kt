package com.samuelnunes.utility_tool_kit.network

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
class NetworkConnectivityObserver @Inject constructor(
    context: Context
) {
    enum class Status {
        AVAILABLE, UNAVAILABLE, LOSING, LOST;

        fun hasConnection(): Boolean = this == AVAILABLE || this == LOSING
    }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun observe(): Flow<Status> {
        return callbackFlow {
            val networkCapabilities = connectivityManager.activeNetwork
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)
            if (actNw != null) {
                when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> send(Status.AVAILABLE)
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> send(Status.AVAILABLE)
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> send(Status.AVAILABLE)
                    else -> send(Status.LOST)
                }
            } else {
                send(Status.LOST)
            }
            val callback = object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(Status.AVAILABLE) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(Status.LOSING) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(Status.LOST) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(Status.UNAVAILABLE) }
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }
}