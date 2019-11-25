package s.jure.sample.app.soccer.data

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface SoccerApiService {

    @GET("hiring/clubs.json")
    fun getClubList(): Call<List<SoccerClub>>

    companion object {
        private const val BASE_URL = "https://public.allaboutapps.at/"

        fun create(): SoccerApiService {

            val client = OkHttpClient.Builder()
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SoccerApiService::class.java)
        }
    }
}
