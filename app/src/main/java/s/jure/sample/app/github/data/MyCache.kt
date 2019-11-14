package s.jure.sample.app.github.data

import androidx.lifecycle.MutableLiveData
import s.jure.sample.app.github.data.api.GithubApiOperation
import s.jure.sample.app.github.data.api.GithubApiService
import s.jure.sample.app.github.data.daos.GithubDao
import s.jure.sample.app.github.data.entities.GithubRepo
import s.jure.sample.app.github.data.entities.GithubRepoContributor
import s.jure.sample.app.github.data.entities.GithubUser
import java.util.concurrent.Executor

class MyCache(
    private val githubDao: GithubDao,
    private val githubApiService: GithubApiService,
    private val ioExecutor: Executor
) {

    companion object {
        private const val FETCHING_REPO_LIST_SIZE = 100
    }

    private var fetchInProgress = false

    private fun insertRepoList(repos: List<GithubRepo>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            githubDao.insertRepoList(repos)
            insertFinished()
        }
    }

    private fun updateRepo(repo: GithubRepo) = ioExecutor.execute { githubDao.updateRepo(repo) }

    private fun updateContributors(repoId: Int, contributors: List<GithubUser>) {
        ioExecutor.execute {
            githubDao.insertUserList(contributors)
            val currentList = githubDao.getContributingUserList(repoId)
            if (currentList != contributors) {
                // delete old list
                githubDao.deleteContributors(repoId)
                // insert new list
                githubDao.insertContributorList(
                    contributors.map { gu ->
                        GithubRepoContributor(
                            repoId = repoId,
                            userId = gu.userId
                        )
                    }
                )
            }
        }
    }

    fun fetchMoreRepos(networkErrors: MutableLiveData<String>? = null) {
        // prevent parallel repo list fetch operations
        if (fetchInProgress) return

        fetchInProgress = true

        ioExecutor.execute {
            GithubApiOperation.fetchRepoList(
                githubApiService,
                githubDao.getMaxRepoId(),  // database read
                FETCHING_REPO_LIST_SIZE,
                { repos -> insertRepoList(repos) { fetchInProgress = false } },
                { error ->
                    networkErrors?.postValue(error)
                    fetchInProgress = false
                })
        }
    }

    fun fetchRepoDetails(repoId: Int, networkErrors: MutableLiveData<String>?) {
        ioExecutor.execute {
            GithubApiOperation.fetchRepoDetails(
                githubApiService,
                githubDao.getFullName(repoId),  // database read
                { repo -> repo?.let { updateRepo(repo) } },
                { contributors -> updateContributors(repoId, contributors) },
                { error -> networkErrors?.postValue(error) }
            )
        }
    }

    fun getAllRepos() = githubDao.getAllRepos()

    fun getRepoLD(repoId: Int) = githubDao.getRepoLD(repoId)

    fun getContributingUserListLD(repoId: Int) = githubDao.getContributingUserListLD(repoId)
}