package s.jure.sample.app.github.repo

import androidx.lifecycle.LiveData
import s.jure.sample.app.github.data.entities.GithubRepo
import s.jure.sample.app.github.data.entities.GithubUser


data class RepoDetailsResult(
    val repo: LiveData<GithubRepo>,
    val contributorList: LiveData<List<GithubUser>>,
    val networkErrors: LiveData<String>
)