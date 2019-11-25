package s.jure.sample.app.soccer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import s.jure.sample.app.soccer.data.SoccerClub
import s.jure.sample.app.soccer.repo.MyRepo

class MainViewModel(
    private val myRepo: MyRepo
) : ViewModel() {

    /**
     * Club list
     */

    private val unsortedClubList: LiveData<List<SoccerClub>>

    val clubListNetworkErrors: LiveData<String>

    init {
        val repoListResult = myRepo.queryClubList()
        unsortedClubList = repoListResult.clubs
        clubListNetworkErrors = repoListResult.networkErrors
        updateList()
    }

    fun updateList() = myRepo.updateClubList()


    /**
     * Sorting
     *
     */


    companion object {
        enum class SortMethod { CLUB_NAME, CLUB_VALUE }
    }

    private val sortByMLD = MutableLiveData(SortMethod.CLUB_NAME)

    // makes list react to changes in sorting and in source data
    private val sortedClubList = MediatorLiveData<List<SoccerClub>>().also { mld ->
        mld.addSource(sortByMLD) { mld.value = sortClubs() }
        mld.addSource(unsortedClubList) { mld.value = sortClubs() }
    }

    // sorting logic
    private fun sortClubs(): List<SoccerClub> = when (sortByMLD.value) {
        SortMethod.CLUB_NAME -> unsortedClubList.value?.sortedBy { it.name }
        SortMethod.CLUB_VALUE -> unsortedClubList.value?.sortedByDescending { it.clubValue }
        else -> unsortedClubList.value?.sortedBy { it.name }
    } ?: listOf()

    // expose LiveData (instead of MediatorLiveData)
    val clubList: LiveData<List<SoccerClub>> = sortedClubList

    fun nextSortMethod() {
        sortByMLD.postValue(
            SortMethod.values().getOrElse(
                (sortByMLD.value?.ordinal ?: -1) + 1
            ) { SortMethod.CLUB_NAME }
        )
    }


    /**
     * Selected club
     *
     * Easy way is to just save the instance.
     * Data refresh would not work, but it cannot be reliably refreshed anyways as the clubs do not
     * come with unambiguous id from backend
     *
     */

    var selectedClub: SoccerClub? = null
}
