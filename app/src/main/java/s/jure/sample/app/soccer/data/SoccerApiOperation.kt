package s.jure.sample.app.soccer.data

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "SoccerApiOperation"

object SoccerApiOperation {

    fun fetchClubList(
        service: SoccerApiService,
        onSuccess: (clubs: List<SoccerClub>) -> Unit,
        onError: (error: String) -> Unit
    ) {
        Log.d(TAG, "fetching list")

        service.getClubList().enqueue(
            object : Callback<List<SoccerClub>> {
                override fun onFailure(call: Call<List<SoccerClub>>?, t: Throwable) {
                    Log.d(TAG, "Failed to get data")
                    onError(t.message ?: "? Club list fetch error")
                }

                override fun onResponse(
                    call: Call<List<SoccerClub>>?,
                    response: Response<List<SoccerClub>>
                ) {

                    Log.d(TAG, "Got a response $response")

                    if (response.isSuccessful)
                        onSuccess(response.body().orEmpty())
                    else
                        onError(response.errorBody()?.string() ?: "? Club list response error")
                }
            }
        )
    }
}