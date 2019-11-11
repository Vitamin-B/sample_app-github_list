package s.jure.sample.app.github.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.main_activity.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance
import s.jure.sample.app.github.R
import s.jure.sample.app.github.ui.adapters.GithubRepoAdapter
import s.jure.sample.app.github.ui.fragments.DetailFragment
import s.jure.sample.app.github.ui.fragments.MainFragment

class MainActivity : AppCompatActivity(), KodeinAware, GithubRepoAdapter.OnEntryClickListener {

    override val kodein by closestKodein()

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        //  injecting ViewModel
        mainViewModel = ViewModelProviders
            .of(this, direct.instance<MainViewModelFactory>())
            .get(MainViewModel::class.java)


        if (savedInstanceState == null && fragment_container != null) {
            // populate main fragment
            val mainFragment = MainFragment()

            // Add the fragment to the 'fragmentsContainer' FrameLayout
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, mainFragment).commit()
        }
    }

    override fun onEntryClicked(repoId: Int) {

        // set selected repo in view model
        mainViewModel.selectRepo(repoId)

        // handle fragment if needed
        if (fragment_container != null) {

            val detailFragment = DetailFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }

    }

    override fun requestExtraEntries() {
        // fetch extra data for repo list
        mainViewModel.fetchAdditionalRepos()
    }
}
