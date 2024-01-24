package ru.snowadv.comapr.data.remote

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class ApiAuthenticator(var token: String? = null) : Interceptor {
    private fun responseCount(initResponse: Response): Int {
        var response: Response? = initResponse
        var result = 1
        while (response?.priorResponse.also { response = it } != null) {
            result++
        }
        return result
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .also { newRequest -> token?.let { newRequest.header("Authorization", "Bearer $it") } }
            .build()
        return chain.proceed(authenticatedRequest)
    }
}