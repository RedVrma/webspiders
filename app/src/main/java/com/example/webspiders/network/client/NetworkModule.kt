package com.example.webspiders.network.client

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    //this should come from a commons file and should not be here, only here for this project purposes.
    private const val BASE_URL =  "https://jsonplaceholder.typicode.com/users/";

    fun provideNetworkClient(useFakeJson: Boolean = true) : UserService{
        val clientBuilder = OkHttpClient.Builder();
        if(useFakeJson){
            clientBuilder.addInterceptor(FakeJsonInterceptor())
        }

        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserService::class.java)
    }
}


class FakeJsonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val fakeUsersJson = """
            [
              {
                "id": 1,
                "name": "Leanne Graham",
                "email": "Sincere@april.biz",
                "companyName": "Romaguera-Crona"
              },
              {
                "id": 2,
                "name": "Ervin Howell",
                "email": "Shanna@melissa.tv",
                "companyName": "Deckow-Crist"
              },
              {
                "id": 3,
                "name": "Clementine Bauch",
                "email": "Nathan@yesenia.net",
                "companyName": "Romaguera-Jacobson"
              }
            ]
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(fakeUsersJson.toResponseBody("application/json".toMediaType()))
            .build()
    }
}