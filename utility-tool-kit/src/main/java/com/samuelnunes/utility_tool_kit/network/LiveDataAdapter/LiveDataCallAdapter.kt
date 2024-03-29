package com.samuelnunes.utility_tool_kit.network.LiveDataAdapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.samuelnunes.utility_tool_kit.R
import com.samuelnunes.utility_tool_kit.domain.Resource
import com.samuelnunes.utility_tool_kit.network.naturalAdapter.ResourceCall
import kotlinx.coroutines.runBlocking
import retrofit2.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataBodyCallAdapter<R>(
    private val responseType: Type
) : CallAdapter<R, LiveData<R>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<R> = object : LiveData<R>() {
        var started = AtomicBoolean(false)

        override fun onActive() {
            super.onActive()
            if (started.compareAndSet(false, true)) {
                call.enqueue(object : Callback<R> {
                    override fun onResponse(call: Call<R>, response: Response<R>) {
                        if (response.isSuccessful && response.code() != 204) {
                            postValue(response.body())
                        } else {
                            postValue(null)
                        }
                    }

                    override fun onFailure(call: Call<R>, throwable: Throwable) {
                        postValue(null)
                    }
                })
            }
        }
    }
}

class LiveDataResultCallAdapter<R>(
    private val responseType: Type,
    private val annotations: Array<out Annotation>
) : CallAdapter<R, LiveData<Resource<R>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(delegate: Call<R>): LiveData<Resource<R>> {
        return object : LiveData<Resource<R>>(Resource.Loading()) {
            var started = AtomicBoolean(false)

            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    ResourceCall(delegate, annotations).enqueue(object : Callback<Resource<R>> {
                        override fun onResponse(
                            call: Call<Resource<R>>,
                            response: Response<Resource<R>>
                        ) {
                            postValue(response.body())
                        }

                        override fun onFailure(call: Call<Resource<R>>, t: Throwable) {
                            postValue(Resource.Error(throwable = t))
                        }
                    })
                }
            }
        }
    }
}