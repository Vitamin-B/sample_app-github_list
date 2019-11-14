package s.jure.sample.app.github.data.api

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import s.jure.sample.app.github.data.entities.GithubRepo
import s.jure.sample.app.github.data.entities.GithubUser

private const val TAG = "GithubApiOperation"

object GithubApiOperation {

    /**
     * Fetch repo list
     * @param fromIdExcluding search from repo id onwards (excluding specified id)
     * @param itemsPerPage number of repositories to be returned (max 100)
     *
     */

    fun fetchRepoList(
        service: GithubApiService,
        fromIdExcluding: Int?,
        itemsPerPage: Int,
        onSuccess: (repos: List<GithubRepo>) -> Unit,
        onError: (error: String) -> Unit
    ) {
        Log.d(TAG, "startingIndex: $fromIdExcluding, itemsPerPage: $itemsPerPage")

        service.getRepoList(fromIdExcluding ?: 0, itemsPerPage).enqueue(
            object : Callback<List<GithubRepo>> {
                override fun onFailure(call: Call<List<GithubRepo>>?, t: Throwable) {
                    Log.d(TAG, "failed to get data")
                    onError(t.message ?: "? Repo list fetch error")
                }

                override fun onResponse(
                    call: Call<List<GithubRepo>>?,
                    response: Response<List<GithubRepo>>
                ) {
                    Log.d(TAG, "response: $response")

                    if (response.isSuccessful)
                        onSuccess(response.body().orEmpty())
                    else
                        onError(response.errorBody()?.string() ?: "? Repo list response error")
                }
            }
        )
    }

    /**
     * Fetch info of specific repo
     * @param repoFullName repo full name (user/repo_name)
     * Two parts:
     * 1. Info about repo (statistics)
     * 2. List of contributors
     *
     */

    fun fetchRepoDetails(
        service: GithubApiService,
        repoFullName: String,
        onRepoInfoSuccess: (repo: GithubRepo?) -> Unit,
        onRepoContributorsSuccess: (repos: List<GithubUser>) -> Unit,
        onError: (error: String) -> Unit
    ) {
        Log.d(TAG, "repoFullName: $repoFullName")

        val user = repoFullName.split("/")[0]
        val name = repoFullName.split("/")[1]

        service.getRepo(user, name).enqueue(
            object : Callback<GithubRepo> {
                override fun onFailure(call: Call<GithubRepo>?, t: Throwable) {
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "? Repo info fetch error")
                }

                override fun onResponse(
                    call: Call<GithubRepo>?,
                    response: Response<GithubRepo>
                ) {

                    Log.d(TAG, "got a response $response")

                    if (response.isSuccessful)
                        onRepoInfoSuccess(response.body())
                    else
                        onError(response.errorBody()?.string() ?: "? Repo info response error")
                }
            }
        )
        service.getRepoContributors(user, name).enqueue(
            object : Callback<List<GithubUser>> {
                override fun onFailure(call: Call<List<GithubUser>>?, t: Throwable) {
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "? Contributors fetch error")
                }

                override fun onResponse(
                    call: Call<List<GithubUser>>?,
                    response: Response<List<GithubUser>>
                ) {

                    Log.d(TAG, "got a response $response")

                    if (response.isSuccessful)
                        onRepoContributorsSuccess(response.body().orEmpty())
                    else
                        onError(response.errorBody()?.string() ?: "? Contributors response error")
                }
            }
        )
    }
}