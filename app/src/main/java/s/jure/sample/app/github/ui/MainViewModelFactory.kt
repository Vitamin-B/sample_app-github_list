package s.jure.sample.app.github.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import s.jure.sample.app.github.data.entities.GithubRepo
import s.jure.sample.app.github.repo.MyRepo
import s.jure.sample.app.github.repo.RepoBoundaryCallback

class MainViewModelFactory(private val myRepo: MyRepo) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(myRepo) as T
    }
}