package s.jure.sample.app.soccer.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.main_activity.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance
import s.jure.sample.app.soccer.R
import s.jure.sample.app.soccer.data.SoccerClub
import s.jure.sample.app.soccer.ui.adapters.ClubListAdapter
import s.jure.sample.app.soccer.ui.fragments.DetailFragment
import s.jure.sample.app.soccer.ui.fragments.MainFragment

class MainActivity : AppCompatActivity(), KodeinAware, ClubListAdapter.OnEntryClickListener {

    override val kodein by closestKodein()

    private lateinit var mainViewModel: MainViewModel

    private var mainFragmentActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        //  injecting ViewModel
        mainViewModel = ViewModelProviders
            .of(this, direct.instance<MainViewModelFactory>())
            .get(MainViewModel::class.java)


        if (fragment_container != null) {
            if (savedInstanceState == null) {
                // populate main fragment
                val mainFragment = MainFragment()

                // Add the fragment to the 'fragmentsContainer' FrameLayout
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mainFragment).commit()
            }

            supportFragmentManager.addOnBackStackChangedListener { updateActionBar() }

            updateActionBar()
        }
    }

    private fun updateActionBar() {

        mainFragmentActive = supportFragmentManager.fragments.lastOrNull() is MainFragment

        // show back arrow if not MainFragment
        supportActionBar?.setDisplayHomeAsUpEnabled(!mainFragmentActive)

        // change title
        when (supportFragmentManager.fragments.lastOrNull()) {
            is MainFragment -> supportActionBar?.title = getString(R.string.app_name)
            is DetailFragment -> supportActionBar?.title = mainViewModel.selectedClub?.name
        }

        // refresh options menu
        invalidateOptionsMenu()
    }

    override fun onEntryClicked(club: SoccerClub) {

        // set selected repo in view model
        mainViewModel.selectedClub = club

        // handle fragment if needed
        if (fragment_container != null) {

            val detailFragment = DetailFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
            R.id.switch_sort -> {
                mainViewModel.nextSortMethod()
                return false
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // shows sorting option only on main fragment
        menu?.findItem(R.id.switch_sort)?.isVisible = mainFragmentActive
        return super.onPrepareOptionsMenu(menu)
    }
}
