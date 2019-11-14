package s.jure.sample.app.github.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_fragment.*
import s.jure.sample.app.github.R
import s.jure.sample.app.github.ui.MainViewModel
import s.jure.sample.app.github.ui.adapters.GithubRepoAdapter
import s.jure.sample.app.github.ui.adapters.HITS_PER_PAGE

class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var githubRepoAdapter: GithubRepoAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        // init recycler view
        recycler_view.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        githubRepoAdapter = GithubRepoAdapter(activity as GithubRepoAdapter.OnEntryClickListener)
        recycler_view.adapter = githubRepoAdapter

        mainViewModel.repoList.observe(this, Observer {
            githubRepoAdapter.submitList(it)
            githubRepoAdapter.refreshOnUpdate()
        })

        next_btn.setOnClickListener { updatePageNav(githubRepoAdapter.nextPage()) }
        back_btn.setOnClickListener { updatePageNav(githubRepoAdapter.prevPage()) }
        updatePageNav(1)

    }

    private fun updatePageNav(currentPage: Int) {
        val newStr = "${(currentPage - 1) * HITS_PER_PAGE + 1} - ${currentPage * HITS_PER_PAGE}"
        page_info.text = newStr
        back_btn.visibility = View.VISIBLE
        recycler_view.scrollToPosition(0)
    }


}
