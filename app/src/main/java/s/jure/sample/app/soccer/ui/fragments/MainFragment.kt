package s.jure.sample.app.soccer.ui.fragments

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
import kotlinx.android.synthetic.main.main_fragment.*
import s.jure.sample.app.soccer.R
import s.jure.sample.app.soccer.ui.MainViewModel
import s.jure.sample.app.soccer.ui.adapters.ClubListAdapter

class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var soccerRepoAdapter: ClubListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        // init recycler view
        recycler_view.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        // pass entry click callback
        soccerRepoAdapter = ClubListAdapter(activity as ClubListAdapter.OnEntryClickListener)
        recycler_view.adapter = soccerRepoAdapter

        // bond with data
        mainViewModel.clubList.observe(this, Observer {
            soccerRepoAdapter.setData(it)
            recycler_view.scrollToPosition(0)
            swipe_to_refresh.isRefreshing = false
        })

        // show network errors to user
        mainViewModel.clubListNetworkErrors.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            swipe_to_refresh.isRefreshing = false
        })

        // enable swipe to refresh
        swipe_to_refresh.setOnRefreshListener { mainViewModel.updateList() }
    }
}
