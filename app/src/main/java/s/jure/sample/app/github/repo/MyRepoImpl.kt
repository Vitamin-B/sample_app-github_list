package s.jure.sample.app.github.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import s.jure.sample.app.github.data.MyCache
import s.jure.sample.app.github.data.entities.GithubRepo

class MyRepoImpl(private val myCache: MyCache): MyRepo {

    private val _networkErrors = MutableLiveData<String>()

    override val networkErrors: LiveData<String>
        get() = _networkErrors

    override fun queryRepoList() =
        LivePagedListBuilder(myCache.getAllRepos(), DATABASE_PAGE_SIZE)
            .setBoundaryCallback(RepoBoundaryCallback())
            .build()

    override fun fetchAdditionalRepos() = myCache.fetchMoreRepos()

    override fun queryRepoDetails(repoId: Int): MyRepo.RepoDetailsResult {

        updateRepoInfo(repoId, _networkErrors)

        return MyRepo.RepoDetailsResult(
            repo = myCache.getRepoLD(repoId),
            contributorList = myCache.getContributingUserListLD(repoId)
        )
    }

    private fun updateRepoInfo(repoId: Int, networkErrors: MutableLiveData<String>) =
        myCache.fetchRepoDetails(repoId, networkErrors)



    inner class RepoBoundaryCallback : PagedList.BoundaryCallback<GithubRepo>() {

        override fun onZeroItemsLoaded() =
            myCache.fetchMoreRepos(_networkErrors)

        override fun onItemAtEndLoaded(itemAtEnd: GithubRepo) =
            myCache.fetchMoreRepos(_networkErrors)
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 25
    }
}