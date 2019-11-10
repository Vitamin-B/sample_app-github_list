package s.jure.sample.app.github.data

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.paging.DataSource
import s.jure.sample.app.github.data.daos.GithubDao
import s.jure.sample.app.github.data.entities.GithubRepo
import java.util.concurrent.Executor

class MyCache(
    private val githubDao: GithubDao,
    private val ioExecutor: Executor
) {
    fun insertRepoList(repos: List<GithubRepo>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("GotRepos", repos.toString())
            githubDao.insertRepoList(repos)
            insertFinished()
        }
    }

    fun getAllRepos(): DataSource.Factory<Int, GithubRepo> = githubDao.getAllRepos()

    @WorkerThread
    fun getMaxRepoId() = githubDao.getMaxRepoId()
}