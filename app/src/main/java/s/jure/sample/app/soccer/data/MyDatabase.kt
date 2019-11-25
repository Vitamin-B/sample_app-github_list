package s.jure.sample.app.soccer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

const val SOCCER_DATABASE_NAME = "soccer_database"

@Database(
    entities = [SoccerClub::class],
    version = MyDatabase.VERSION,
    exportSchema = false
)
abstract class MyDatabase : RoomDatabase() {
    abstract fun soccerDao(): SoccerDao

    companion object {

        const val VERSION = 1

        @Volatile
        private var instance: MyDatabase? = null

        operator fun invoke(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MyDatabase::class.java, SOCCER_DATABASE_NAME
            )
                .build()
    }
}