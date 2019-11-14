package s.jure.sample.app.github.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import s.jure.sample.app.github.R
import s.jure.sample.app.github.data.entities.GithubRepo
import kotlin.math.min

const val HITS_PER_PAGE = 25

class GithubRepoAdapter(
    private val onEntryClickListener: OnEntryClickListener?
) : PagedListAdapter<GithubRepo, GithubRepoAdapter.EntryViewHolder>(entryDiffCallback) {

    interface OnEntryClickListener {
        fun onEntryClicked(repoId: Int)
        fun requestExtraEntries()
    }

    private var resultPageNumber = 1

    private var missingItems = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.repo_card_view, parent, false)
        return EntryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        // holder.bind(getItem(position))

        /**
         * Modifications for page-wise display
         */

        if (position < HITS_PER_PAGE) {
            val actualPosition = position + (resultPageNumber - 1) * HITS_PER_PAGE

            // save information if there is not enough repos in cache
            missingItems = if (actualPosition < super.getItemCount()) {
                holder.bind(getItem(actualPosition))
                false
            } else {
                holder.bind(null)
                true
            }
        }
    }


    /**
     * Extensions for page-wise display
     */

    fun prevPage(): Int {
        if (resultPageNumber > 1) {
            resultPageNumber--
            refresh()
        }
        return resultPageNumber
    }

    fun nextPage(): Int {
        resultPageNumber++
        refresh()

        return resultPageNumber
    }

    fun refreshOnUpdate() {
        if (missingItems)
            refresh()
    }

    private fun refresh() {
        notifyItemRangeChanged(0, HITS_PER_PAGE)

        // manually request preload on next 3 pages if needed
        if ((resultPageNumber + 3) * HITS_PER_PAGE > super.getItemCount())
            onEntryClickListener?.requestExtraEntries()
    }

    override fun getItemCount(): Int {
        val count = super.getItemCount()
        return min(count, HITS_PER_PAGE)
    }

    /**
     * End of extensions for page-wise display
     */

    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.repo_name_short)
        private val longName: TextView = itemView.findViewById(R.id.repo_name_long)

        fun bind(gr: GithubRepo?) {
            name.text = gr?.name
            longName.text = gr?.fullName

            itemView.setOnClickListener {
                gr?.repoId?.let { onEntryClickListener?.onEntryClicked(it) }
            }
        }
    }

    companion object {

        private val entryDiffCallback = object : DiffUtil.ItemCallback<GithubRepo>() {
            override fun areItemsTheSame(oldItem: GithubRepo, newItem: GithubRepo) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: GithubRepo, newItem: GithubRepo) =
                oldItem.name == newItem.name && oldItem.fullName == newItem.fullName
        }
    }
}