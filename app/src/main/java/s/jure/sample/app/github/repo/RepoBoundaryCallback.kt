package s.jure.sample.app.github.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import s.jure.sample.app.github.data.MyCache
import s.jure.sample.app.github.data.api.GithubApiOperation
import s.jure.sample.app.github.data.api.GithubApiService
import s.jure.sample.app.github.data.entities.GithubRepo
import kotlin.concurrent.thread


class RepoBoundaryCallback(
    private val service: GithubApiService,
    private val cache: MyCache
) : PagedList.BoundaryCallback<GithubRepo>() {

    private var fetchInProgress = false

    // expose error messages
    private val _networkErrors = MutableLiveData<String>()

    val networkErrors: LiveData<String>
        get() = _networkErrors

    override fun onZeroItemsLoaded() {
        requestAndSaveAdditionalData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: GithubRepo) {
        requestAndSaveAdditionalData()
    }

    private fun requestAndSaveAdditionalData() {
        if (fetchInProgress) return

        fetchInProgress = true

        // thread needed because of fetching the last index in database
        thread {
            GithubApiOperation.fetchRepoList(
                service,
                cache.getMaxRepoId(),
                NETWORK_PAGE_SIZE,
                { repos ->
                    cache.insertRepoList(repos) {
                        fetchInProgress = false
                    }
                },
                { error ->
                    _networkErrors.postValue(error)
                    fetchInProgress = false
                })
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 100
    }
}