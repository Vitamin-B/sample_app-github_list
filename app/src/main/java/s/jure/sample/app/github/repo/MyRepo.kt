package s.jure.sample.app.github.repo

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import s.jure.sample.app.github.data.entities.GithubRepo
import s.jure.sample.app.github.data.entities.GithubUser


interface MyRepo {

    data class RepoDetailsResult(
        val repo: LiveData<GithubRepo>,
        val contributorList: LiveData<List<GithubUser>>
    )

    // displays network error messages
    val networkErrors: LiveData<String>

    // list of all repos
    fun queryRepoList(): LiveData<PagedList<GithubRepo>>

    // details about repo
    fun queryRepoDetails(repoId: Int): RepoDetailsResult

    // manually trigger getting additional repos
    fun fetchAdditionalRepos()

}