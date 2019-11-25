package s.jure.sample.app.soccer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import s.jure.sample.app.soccer.repo.MyRepo

class MainViewModelFactory(private val myRepo: MyRepo) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(myRepo) as T
    }
}