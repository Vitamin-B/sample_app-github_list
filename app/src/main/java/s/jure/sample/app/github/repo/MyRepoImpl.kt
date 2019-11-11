package s.jure.sample.app.github.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import s.jure.sample.app.github.data.api.GithubApiOperation
import s.jure.sample.app.github.data.api.GithubApiService
import s.jure.sample.app.github.data.daos.GithubDao
import s.jure.sample.app.github.data.entities.GithubRepoContributor
import kotlin.concurrent.thread

class MyRepoImpl(private val githubDao: GithubDao,
                 private val githubApiService: GithubApiService,
                 private val boundaryCallback: RepoBoundaryCallback
                 ): MyRepo {

    override fun queryRepoList(): RepoListResult {

        val networkErrors = boundaryCallback.networkErrors

        val repoList = LivePagedListBuilder(githubDao.getAllRepos(), DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return RepoListResult(repoList = repoList, networkErrors = networkErrors)
    }

    override fun fetchAdditionalRepos() {
        boundaryCallback.onZeroItemsLoaded()
    }

    override fun queryRepoDetails(repoId: Int): RepoDetailsResult {

        val networkErrors = MutableLiveData<String>()

        updateRepoInfo(repoId, networkErrors)

        return RepoDetailsResult(
            repo = githubDao.getRepoById(repoId),
            contributorList = githubDao.getContributingUserListLD(repoId),
            networkErrors = networkErrors
        )
    }

    private fun updateRepoInfo(repoId: Int, networkErrors: MutableLiveData<String>) {
        // run on thread as it need to get full name from database first
        thread {
            GithubApiOperation.fetchRepoDetails(
                githubApiService,
                githubDao.getFullName(repoId),
                { thread { it?.let { githubDao.insertRepoList(listOf(it)) } } },
                {
                    thread {
                        Log.d("UserData", it.toString())
                        githubDao.insertUserList(it)
                        val currentList = githubDao.getContributingUserList(repoId)
                        if (currentList != it) {
                            // delete old list
                            githubDao.deleteContributors(repoId)
                            // insert new list
                            githubDao.insertContributorList(
                                it.map { gu ->
                                    GithubRepoContributor(
                                        repoId = repoId,
                                        userId = gu.userId
                                    )
                                }
                            )
                        }
                    }
                }, { error -> networkErrors.postValue(error) }
            )
        }
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 25
    }
}