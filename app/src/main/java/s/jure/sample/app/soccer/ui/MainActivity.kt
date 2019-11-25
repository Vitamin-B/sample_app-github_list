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

    private val detailFragment by lazy { DetailFragment() }

    // landscape tablet has split view, which affects the app bar
    private val splitView: Boolean
        get() = fragment_container == null

    // to know is main list is displayed
    private val hasBackStack: Boolean
        get() = supportFragmentManager.backStackEntryCount > 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        //  injecting ViewModel
        mainViewModel = ViewModelProviders
            .of(this, direct.instance<MainViewModelFactory>())
            .get(MainViewModel::class.java)


        if (!splitView) {
            if (savedInstanceState == null)
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, MainFragment()).commit()

            supportFragmentManager.addOnBackStackChangedListener { updateActionBar() }
        }

        // refresh action bar
        updateActionBar()
    }

    private fun updateActionBar() {

        // title
        supportActionBar?.title =
            if (hasBackStack || splitView)
                mainViewModel.selectedClub.value?.name ?: getString(R.string.app_name)
            else
                getString(R.string.app_name)

        // back arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(hasBackStack && !splitView)

        // refresh options menu
        invalidateOptionsMenu()
    }

    override fun onEntryClicked(club: SoccerClub) {

        // set selected repo in view model
        mainViewModel.selectClub(club)

        if (!splitView) {
            // handle fragment if needed
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        } else
            supportActionBar?.title = club.name

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
        // shows sorting option
        menu?.findItem(R.id.switch_sort)?.isVisible = !hasBackStack || splitView
        return super.onPrepareOptionsMenu(menu)
    }
}
