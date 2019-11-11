package s.jure.sample.app.github.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * List of users that are contributors to at least one repo
 */

@Entity(tableName = "users")
data class GithubUser (
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    @SerializedName("id")
    val userId: Int,

    @ColumnInfo(name = "login")
    @SerializedName("login")
    val loginName: String,

    @ColumnInfo(name = "avatar_url")
    @SerializedName("avatar_url")
    val avatarUrl: String
)