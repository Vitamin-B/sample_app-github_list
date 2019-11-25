package s.jure.sample.app.soccer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "clubs")
data class SoccerClub (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "club_id")
    @SerializedName("id")
    val clubId: Int = 0,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,

    @ColumnInfo(name = "country")
    @SerializedName("country")
    val country: String,

    @ColumnInfo(name = "value")
    @SerializedName("value")
    val clubValue: Int,

    @ColumnInfo(name = "image")
    @SerializedName("image")
    val imageUrl: String,

    @ColumnInfo(name = "european_titles")
    @SerializedName("european_titles")
    val europeanTitles: Int? = null

)