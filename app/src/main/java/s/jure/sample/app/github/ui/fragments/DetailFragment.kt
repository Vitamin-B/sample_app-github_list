package s.jure.sample.app.github.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.detail_fragment.*
import s.jure.sample.app.github.R
import s.jure.sample.app.github.ui.MainViewModel
import s.jure.sample.app.github.ui.adapters.GithubUserAdapter

internal class DetailFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var contributorsAdapter: GithubUserAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        mainViewModel.selectedRepoDetails.observe(this, Observer { gr ->
            n_stars_tw.text = gr.stargazers ?. toString() ?: " "
            n_forks_tw.text = gr.forkCount ?. toString() ?: " "
            repo_size_tw.text = gr.size
                ?. let { if (it < 10000) "$it kB" else "${it/1000} MB" } ?: " "
            dt_repo_name.text = gr.name
            dt_repo_name_long.text = gr.fullName
        })

        contributors_list.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        contributorsAdapter = GithubUserAdapter()
        contributors_list.adapter = contributorsAdapter

        mainViewModel.selectedRepoContributors.observe(this, Observer { gul ->
            contributorsAdapter.setData(gul)
        })

        mainViewModel.selectedRepoNetworkErrors.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })

    }


}