package cr.ac.una.spotify.http

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(val clientId: String, val clientSecret: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val base64Auth = Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Basic $base64Auth")
            .build()
        return chain.proceed(request)
    }
}