package s.jure.sample.app.github

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import s.jure.sample.app.github.data.MyCache
import s.jure.sample.app.github.data.MyDatabase
import s.jure.sample.app.github.data.api.GithubApiService
import s.jure.sample.app.github.repo.MyRepo
import s.jure.sample.app.github.repo.MyRepoImpl
import s.jure.sample.app.github.ui.MainViewModelFactory
import java.util.concurrent.Executors

@Suppress("unused")
class MyApplication : Application(), KodeinAware {

    /**
     * Dependency injection
     */

    override val kodein = Kodein.lazy {
        // manage application context
        import(androidXModule(this@MyApplication))

        // bind API service
        bind() from singleton { GithubApiService.create() }

        // bind Database
        bind() from singleton { MyDatabase(instance()) }

        // bind DAO
        bind() from singleton { instance<MyDatabase>().githubDao() }

        // bind cache
        bind() from singleton { MyCache(instance(), instance(), Executors.newSingleThreadExecutor()) }

        // bind repository
        bind<MyRepo>() with singleton { MyRepoImpl(instance()) }

        // bind View Model
        bind<MainViewModelFactory>() with singleton { MainViewModelFactory(instance()) }
    }
}