package s.jure.sample.app.soccer.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import s.jure.sample.app.soccer.R
import s.jure.sample.app.soccer.data.SoccerClub


class ClubListAdapter(private val onEntryClickListener: OnEntryClickListener?) :
    RecyclerView.Adapter<ClubListAdapter.EntryViewHolder>() {

    interface OnEntryClickListener {
        fun onEntryClicked(club: SoccerClub)
    }

    private var userList: MutableList<SoccerClub> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.club_card_view, parent, false)
        return EntryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount() = userList.size

    fun setData(newUserList: List<SoccerClub>) {
        val postDiffCallback = EntryDiffCallback(userList, newUserList)
        val diffResult = DiffUtil.calculateDiff(postDiffCallback)
        userList.clear()
        userList.addAll(newUserList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.club_name_tw)
        private val country: TextView = itemView.findViewById(R.id.club_country_tw)
        private val clubValue: TextView = itemView.findViewById(R.id.club_value_tw)
        private val clubLogo: ImageView = itemView.findViewById(R.id.club_logo)

        fun bind(club: SoccerClub?) {
            name.text = club?.name
            country.text = club?.country
            clubValue.text = club?.clubValue?.let {
                itemView.context.resources.getQuantityString(R.plurals.millions, it, it)
            }

            if (club == null)
                clubLogo.visibility = View.INVISIBLE
            else {
                clubLogo.visibility = View.VISIBLE

                val picasso = Picasso.get()
                picasso.load(club.imageUrl).into(clubLogo)
            }

            itemView.setOnClickListener {
                club?.let { onEntryClickListener?.onEntryClicked(it) }
            }
        }
    }

    internal inner class EntryDiffCallback(
        private val oldPosts: List<SoccerClub>,
        private val newPosts: List<SoccerClub>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldPosts.size
        }

        override fun getNewListSize(): Int {
            return newPosts.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPosts[oldItemPosition].clubId == newPosts[newItemPosition].clubId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPosts[oldItemPosition].name == newPosts[newItemPosition].name &&
                    oldPosts[oldItemPosition].country == newPosts[newItemPosition].country &&
                    oldPosts[oldItemPosition].clubValue == newPosts[newItemPosition].clubValue &&
                    oldPosts[oldItemPosition].imageUrl == newPosts[newItemPosition].imageUrl
        }
    }
}