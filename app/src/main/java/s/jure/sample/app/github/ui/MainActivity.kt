package s.jure.sample.app.github.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_activity.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.direct
import org.kodein.di.generic.instance
import s.jure.sample.app.github.R
import s.jure.sample.app.github.ui.adapters.GithubRepoAdapter
import s.jure.sample.app.github.ui.adapters.HITS_PER_PAGE

class MainActivity : AppCompatActivity(), KodeinAware, GithubRepoAdapter.OnEntryClickListener {

    override val kodein by closestKodein()

    private lateinit var mainViewModel: MainViewModel

    private lateinit var githubRepoAdapter: GithubRepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        //  injecting ViewModel instance (singleton)
        mainViewModel = ViewModelProviders
            .of(this, direct.instance<MainViewModelFactory>())
            .get(MainViewModel::class.java)



        // init recycler view
        recycler_view.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        githubRepoAdapter = GithubRepoAdapter(this)
        recycler_view.adapter = githubRepoAdapter

        mainViewModel.repoList.observe(this, Observer {
            githubRepoAdapter.submitList(it)
            githubRepoAdapter.refreshOnUpdate()
        })

        next_btn.setOnClickListener { updatePageNav(githubRepoAdapter.nextPage()) }
        back_btn.setOnClickListener { updatePageNav(githubRepoAdapter.prevPage()) }
        updatePageNav(1)

    }

    override fun onEntryClicked(repoId: Int) {
        //todo: implement
        Toast.makeText(this, "RepoId: $repoId", Toast.LENGTH_SHORT).show()
    }

    override fun requestExtraEntries() {
        mainViewModel.fetchAdditionalRepos()
    }

    private fun updatePageNav(currentPage: Int) {
        val newStr = "${(currentPage - 1) * HITS_PER_PAGE + 1} - ${currentPage * HITS_PER_PAGE}"
        page_info.text = newStr
        back_btn.visibility = View.VISIBLE
        recycler_view.scrollToPosition(0)
    }


}
