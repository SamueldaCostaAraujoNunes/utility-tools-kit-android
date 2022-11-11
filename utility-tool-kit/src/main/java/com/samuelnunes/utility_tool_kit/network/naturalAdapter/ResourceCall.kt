package com.samuelnunes.utility_tool_kit.network.naturalAdapter

import com.samuelnunes.utility_tool_kit.domain.Resource
import com.samuelnunes.utility_tool_kit.network.HttpStatusCode
import com.samuelnunes.utility_tool_kit.network.parseError
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResourceCall<T>(private val delegate: Call<T>, val annotations: Array<out Annotation>) :
    Call<Resource<T>> {

    override fun enqueue(callback: Callback<Resource<T>>) {
        delegate.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) =
                callback.onResponse(this@ResourceCall, successReponse(response))

            override fun onFailure(call: Call<T>, t: Throwable) =
                callback.onResponse(this@ResourceCall, failureResponse(t))
            }
        )
    }

    private fun failureResponse(t: Throwable): Response<Resource<T>> =
        Response.success(Resource.Error(t))

    private fun successReponse(
        response: Response<T>
    ): Response<Resource<T>> {
        val httpStatusCode = HttpStatusCode.enumByStatusCode(response.code())
        return Response.success(
            if (httpStatusCode.isSuccess()) {
                val body = response.body()
                if (body == null || httpStatusCode == HttpStatusCode.NO_CONTENT) {
                    Resource.Empty()
                } else {
                    Resource.Success(
                        data = body,
                        statusCode = httpStatusCode
                    )
                }
            } else {
                val errorResponse = parseError(
                    statusCode = httpStatusCode,
                    responseBody = response.errorBody(),
                    annotations = annotations
                )
                Resource.Error(
                    message = response.message(),
                    statusCode = httpStatusCode,
                    errorData = errorResponse
                )
            }
        )
    }

    override fun execute(): Response<Resource<T>> = try {
        val response = delegate.execute()
        successReponse(response)
    } catch (t: Throwable) {
        failureResponse(t)
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun clone(): Call<Resource<T>> = ResourceCall(delegate.clone(), annotations.clone())

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

}
