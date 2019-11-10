package s.jure.sample.app.github.repo

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import s.jure.sample.app.github.data.entities.GithubRepo
import s.jure.sample.app.github.data.entities.GithubUser


data class RepoListResult(
    val repoList: LiveData<PagedList<GithubRepo>>,
    val networkErrors: LiveData<String>
)