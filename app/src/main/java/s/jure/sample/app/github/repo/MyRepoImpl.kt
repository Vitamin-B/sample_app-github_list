package s.jure.sample.app.github.repo

import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import s.jure.sample.app.github.data.api.GithubApiOperation
import s.jure.sample.app.github.data.api.GithubApiService
import s.jure.sample.app.github.data.daos.GithubDao
import s.jure.sample.app.github.data.entities.GithubRepo
import s.jure.sample.app.github.data.entities.GithubRepoContributor

class MyRepoImpl(private val githubDao: GithubDao,
                 private val githubApiService: GithubApiService,
                 private val boundaryCallback: PagedList.BoundaryCallback<GithubRepo>
                 ): MyRepo {

    override fun queryRepoList(): RepoListResult {

        val networkErrors = MutableLiveData<String>()

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
        GithubApiOperation.fetchRepoDetails(
            githubApiService,
            githubDao.getFullName(repoId),
            { it ?. let { githubDao.insertRepoList(listOf(it)) } },
            {
                githubDao.insertUserList(it)
                val currentList = githubDao.getContributingUserList(repoId)
                if (currentList != it) {
                    // delete old list
                    githubDao.deleteContributors(repoId)
                    // insert new list
                    githubDao.insertContributorList(
                        it.map { gu -> GithubRepoContributor(repoId = repoId, userId = gu.userId) }
                    )
                }
            }, { error -> networkErrors.postValue(error) }
        )
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 25
    }
}