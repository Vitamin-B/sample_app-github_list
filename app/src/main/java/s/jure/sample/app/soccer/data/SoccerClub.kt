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

) {
    override fun equals(other: Any?): Boolean {
        if (other is SoccerClub)
            return name == other.name &&
                    country == other.country &&
                    clubValue == other.clubValue &&
                    imageUrl == other.imageUrl &&
                    europeanTitles == other.europeanTitles

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}