package s.jure.sample.app.github.data.daos

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import s.jure.sample.app.github.data.entities.GithubRepo
import s.jure.sample.app.github.data.entities.GithubRepoContributor
import s.jure.sample.app.github.data.entities.GithubUser

/**
 * Data acquisition objects
 */


@Dao
interface GithubDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepoList(posts: List<GithubRepo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContributorList(posts: List<GithubRepoContributor>)

    @Query("DELETE FROM repo_contributors WHERE repo_id = :repoId")
    fun deleteContributors(repoId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserList(posts: List<GithubUser>)

    @Query("SELECT MAX(repo_id) from repos")
    fun getMaxRepoId(): Int?

    @Query("SELECT * from repos ORDER BY repo_id ASC")
    fun getAllRepos(): DataSource.Factory<Int, GithubRepo>

    @Query("SELECT * from repos WHERE repo_id = :repoId")
    fun getRepoById(repoId: Int): LiveData<GithubRepo>

    @Query("SELECT full_name from repos WHERE repo_id = :repoId")
    fun getFullName(repoId: Int): String

    @Query(
        """SELECT * from users WHERE users.user_id IN
            (SELECT user_id FROM repo_contributors WHERE repo_id = :repoId)"""
    )
    fun getContributingUserListLD(repoId: Int): LiveData<List<GithubUser>>

    @Query(
        """SELECT * from users WHERE users.user_id IN
            (SELECT user_id FROM repo_contributors WHERE repo_id = :repoId)"""
    )
    fun getContributingUserList(repoId: Int): List<GithubUser>
}