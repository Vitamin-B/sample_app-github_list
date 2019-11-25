package s.jure.sample.app.soccer.repo

import androidx.lifecycle.MutableLiveData
import s.jure.sample.app.soccer.data.MyCache
import s.jure.sample.app.soccer.data.SoccerApiOperation
import s.jure.sample.app.soccer.data.SoccerApiService
import kotlin.concurrent.thread

class MyRepoImpl(
    private val myCache: MyCache,
    private val soccerApiService: SoccerApiService
) : MyRepo {

    private var fetchInProgress = false

    private val networkErrors = MutableLiveData<String>()

    override fun queryClubList(): ClubListResult {

        return ClubListResult(
            clubs = myCache.getClubList(),
            networkErrors = networkErrors
        )
    }

    override fun updateClubList(forced: Boolean) {

        if (!fetchInProgress) {
            fetchInProgress = true

            SoccerApiOperation.fetchClubList(
                soccerApiService,
                {
                    thread {
                        myCache.insertRepoList(it) {
                            fetchInProgress = false
                            if (forced)
                                networkErrors.postValue("Up to date")
                        }
                    }
                },
                { error -> networkErrors.postValue(error); fetchInProgress = false })
        }
    }
}