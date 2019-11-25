package s.jure.sample.app.soccer.repo

import androidx.lifecycle.LiveData
import s.jure.sample.app.soccer.data.SoccerClub


data class ClubListResult(
    val clubs: LiveData<List<SoccerClub>>,
    val networkErrors: LiveData<String>
)