package kz.diashima.newsapiproject

import kz.diashima.newsapiproject.models.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("/v2/everything")
    fun getEverything(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ) : Call<News>

    @GET("/v2/top-headlines")
    fun getTopHeadlines(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ) : Call<News>
}