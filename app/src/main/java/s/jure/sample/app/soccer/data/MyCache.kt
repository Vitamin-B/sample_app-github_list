package s.jure.sample.app.soccer.data

import android.util.Log
import androidx.lifecycle.LiveData
import java.util.concurrent.Executor

class MyCache(
    private val soccerDao: SoccerDao,
    private val ioExecutor: Executor
) {
    fun insertRepoList(clubs: List<SoccerClub>, insertFinished: () -> Unit) {
        ioExecutor.execute {

            Log.d("GotClubs", clubs.toString())

            if (!clubs.isNullOrEmpty()) {

                /**
                 * Workaround for updating list of elements without unique id from API:
                 *
                 * If fetched data matches the table content, do not update,
                 * otherwise replace all entries
                 * so we don't end up with duplicates if the club changes its name
                 */

                if (clubs != soccerDao.getClubList()) {
                    soccerDao.purgeClubs()
                    soccerDao.insertRepoList(clubs)
                }

                insertFinished()
            }

        }
    }

    fun getClubList(): LiveData<List<SoccerClub>> = soccerDao.getClubListLD()
}