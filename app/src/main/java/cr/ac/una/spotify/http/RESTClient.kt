package cr.ac.una.spotify.http

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RESTClient {
    class SpotifyAuthClient {
        companion object {
            fun getInstance(): Retrofit {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder()
                    .addInterceptor(
                        AuthInterceptor(
                            "fb279e9294af4f6d93ff0c7ce378d4c3",
                            "bb3666cbf5f3463b9c0420a4d5f080a5"
                        )
                    )
                    .addInterceptor(interceptor)
                    .build()
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://accounts.spotify.com/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                return retrofit
            }
        }
    }

    class SpotifyApiClient {

        companion object {
            public var accessInteceptor: AccessInterceptor = AccessInterceptor("")
            fun getInstance(): Retrofit {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder()
                    .addInterceptor(accessInteceptor)
                    .addInterceptor(interceptor)
                    .build()
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.spotify.com/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                return retrofit
            }
        }
    }
}