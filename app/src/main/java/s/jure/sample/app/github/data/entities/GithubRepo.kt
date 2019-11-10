package s.jure.sample.app.github.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * List of repos. Numbers are fetched / updated per request
 */


@Entity(tableName = "repos")
data class GithubRepo (
    @PrimaryKey
    @ColumnInfo(name = "repo_id")
    @SerializedName("id")
    val repoId: Int,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,

    @ColumnInfo(name = "full_name")
    @SerializedName("full_name")
    val fullName: String,

    @ColumnInfo(name = "size")
    @SerializedName("size")
    val size: Int? = null,

    @ColumnInfo(name = "stargazers_count")
    @SerializedName("stargazers_count")
    val stargazers: Int? = null,

    @ColumnInfo(name = "forks_count")
    @SerializedName("forks_count")
    val forkCount: Int? = null
)