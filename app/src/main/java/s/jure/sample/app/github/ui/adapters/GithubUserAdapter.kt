package s.jure.sample.app.github.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import s.jure.sample.app.github.R
import s.jure.sample.app.github.data.entities.GithubUser


class GithubUserAdapter : RecyclerView.Adapter<GithubUserAdapter.EntryViewHolder>() {

    private var userList: MutableList<GithubUser> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_card_view, parent, false)
        return EntryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
       holder.bind(userList[position])
    }

    override fun getItemCount() = userList.size

    fun setData(newUserList: List<GithubUser>) {
        val postDiffCallback = EntryDiffCallback(userList, newUserList)
        val diffResult = DiffUtil.calculateDiff(postDiffCallback)
        userList.clear()
        userList.addAll(newUserList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val loginName: TextView = itemView.findViewById(R.id.user_login_name)
        private val profilePic: ImageView = itemView.findViewById(R.id.profile_pic)

        fun bind(gu: GithubUser?) {
            loginName.text = gu?.loginName

            if (gu == null)
                profilePic.visibility = View.INVISIBLE
            else {
                profilePic.visibility = View.VISIBLE

                val picasso = Picasso.get()
                picasso.load(gu.avatarUrl).into(profilePic)
            }
        }
    }

    internal inner class EntryDiffCallback(
        private val oldPosts: List<GithubUser>,
        private val newPosts: List<GithubUser>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldPosts.size
        }

        override fun getNewListSize(): Int {
            return newPosts.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPosts[oldItemPosition].userId == newPosts[newItemPosition].userId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPosts[oldItemPosition].loginName == newPosts[newItemPosition].loginName &&
                    oldPosts[oldItemPosition].avatarUrl == newPosts[newItemPosition].avatarUrl
        }
    }
}