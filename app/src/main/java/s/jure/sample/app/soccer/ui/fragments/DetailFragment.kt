package s.jure.sample.app.soccer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detail_fragment.*
import s.jure.sample.app.soccer.R
import s.jure.sample.app.soccer.ui.MainViewModel

internal class DetailFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

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

        // simple bind from selected repo
        mainViewModel.selectedClub.observe(this, Observer { club ->

            val picasso = Picasso.get()
            picasso.load(club.imageUrl).into(d_club_logo)

            d_club_country_tw.text = club.country

            var descriptionText=
                resources.getQuantityString(
                    R.plurals.club_value_desc,
                    club.clubValue,
                    club.name,
                    club.country,
                    club.clubValue
                )

            club.europeanTitles ?. also {euroChips ->
                descriptionText += "<br><br>"
                descriptionText += resources.getQuantityString(
                    R.plurals.club_euro_chips_desc,
                    euroChips,
                    club.name,
                    euroChips
                )
            }

            club_description.text = HtmlCompat.fromHtml(descriptionText, FROM_HTML_MODE_LEGACY)
        } )

    }


}