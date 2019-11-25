package s.jure.sample.app.soccer.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface SoccerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepoList(clubs: List<SoccerClub>)

    @Query("DELETE FROM clubs WHERE club_id > -1")
    fun purgeClubs()

    @Query("SELECT * from clubs ORDER BY club_id ASC")
    fun getClubList(): List<SoccerClub>

    @Query("SELECT * from clubs ORDER BY club_id ASC")
    fun getClubListLD(): LiveData<List<SoccerClub>>

}