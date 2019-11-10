package s.jure.sample.app.github.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Links between repos and contributors
 */


@Entity(tableName = "repo_contributors")
data class GithubRepoContributor (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "connection_id")
    val connectionId: Int = 0,

    @ColumnInfo(name = "repo_id")
    val repoId: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int
)