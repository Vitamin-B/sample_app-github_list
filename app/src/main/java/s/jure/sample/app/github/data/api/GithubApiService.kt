package s.jure.sample.app.github.data.api

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import s.jure.sample.app.github.data.entities.GithubRepo
import s.jure.sample.app.github.data.entities.GithubUser


interface GithubApiService {

    @GET("repositories")
    fun getRepoList(
        @Query("since") fromIdExcluding: Int,
        @Query("per_page") itemsPerPage: Int
    ): Call<List<GithubRepo>>

    @GET("repos/{full_name}")
    fun getRepo(
        @Path("full_name") fullName: String
    ): Call<GithubRepo>

    @GET("repos/{full_name}/contributors")
    fun getRepoContributors(
        @Path("full_name") fullName: String
    ): Call<List<GithubUser>>


    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): GithubApiService {

            val client = OkHttpClient.Builder()
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubApiService::class.java)
        }
    }
}
