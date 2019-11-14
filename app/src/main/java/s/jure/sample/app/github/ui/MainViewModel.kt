package s.jure.sample.app.github.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import s.jure.sample.app.github.data.entities.GithubRepo
import s.jure.sample.app.github.data.entities.GithubUser
import s.jure.sample.app.github.repo.MyRepo

class MainViewModel(
    private val myRepo: MyRepo
) : ViewModel() {

    val networkErrors = myRepo.networkErrors

    val repoList = myRepo.queryRepoList()

    fun fetchAdditionalRepos() = myRepo.fetchAdditionalRepos()


    /**
     * Selected repo details
     */

    private val selectedRepo = MutableLiveData<Int>()
    private val repoDetailsResult: LiveData<MyRepo.RepoDetailsResult> =
        Transformations.map(selectedRepo) { myRepo.queryRepoDetails(it) }

    val selectedRepoDetails: LiveData<GithubRepo> =
        Transformations.switchMap(repoDetailsResult) { it.repo }
    val selectedRepoContributors: LiveData<List<GithubUser>> =
        Transformations.switchMap(repoDetailsResult) { it.contributorList }

    fun selectRepo(repoId: Int) = selectedRepo.postValue(repoId)

}
