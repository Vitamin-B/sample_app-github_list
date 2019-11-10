package s.jure.sample.app.github.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import s.jure.sample.app.github.data.daos.GithubDao
import s.jure.sample.app.github.data.entities.GithubRepo
import s.jure.sample.app.github.data.entities.GithubRepoContributor
import s.jure.sample.app.github.data.entities.GithubUser


/**
 * Creates the database.
 */

const val GITHUB_DATABASE_NAME = "github_database"

@Database(
    entities = [GithubRepo::class, GithubUser::class, GithubRepoContributor::class],
    version = MyDatabase.VERSION,
    exportSchema = false
)
abstract class MyDatabase : RoomDatabase() {
    abstract fun githubDao(): GithubDao

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
                MyDatabase::class.java, GITHUB_DATABASE_NAME
            )
                .build()
    }
}