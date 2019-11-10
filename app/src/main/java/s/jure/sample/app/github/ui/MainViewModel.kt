package s.jure.sample.app.github.ui

import androidx.lifecycle.*
import androidx.paging.PagedList
import s.jure.sample.app.github.data.entities.GithubRepo
import s.jure.sample.app.github.data.entities.GithubUser
import s.jure.sample.app.github.repo.MyRepo
import s.jure.sample.app.github.repo.RepoDetailsResult

class MainViewModel(
    private val myRepo: MyRepo
) : ViewModel() {

    /**
     * Repo list
     */

    val repoList: LiveData<PagedList<GithubRepo>>
    val repoListNetworkErrors: LiveData<String>

    init {
        val repoListResult = myRepo.queryRepoList()
        repoList = repoListResult.repoList
        repoListNetworkErrors = repoListResult.networkErrors
    }

    fun fetchAdditionalRepos() = myRepo.fetchAdditionalRepos()


    /**
     * Selected repo details
     */

    private val selectedRepo = MutableLiveData<Int>()
    private val repoDetailsResult: LiveData<RepoDetailsResult> =
        Transformations.map(selectedRepo) { myRepo.queryRepoDetails(it) }

    val selectedRepoDetails: LiveData<GithubRepo> =
        Transformations.switchMap(repoDetailsResult) { it.repo }
    val selectedRepoContributors: LiveData<List<GithubUser>> =
        Transformations.switchMap(repoDetailsResult) { it.contributorList }
    val selectedRepoNetworkErrors: LiveData<String> =
        Transformations.switchMap(repoDetailsResult) { it.networkErrors }

    fun selectRepo(repoId: Int) {
        selectedRepo.postValue(repoId)
    }
}
