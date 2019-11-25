package s.jure.sample.app.soccer.repo


interface MyRepo {

    // list of all repos
    fun queryClubList(): ClubListResult

    // trigger manual update of the list (e.g. on swipe down)
    fun updateClubList(forced: Boolean = false)

}