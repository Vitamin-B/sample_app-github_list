package s.jure.sample.app.github.repo


interface MyRepo {

    // list of all repos
    fun queryRepoList(): RepoListResult

    // details about repo
    fun queryRepoDetails(repoId: Int): RepoDetailsResult

    // manually trigger getting additional repos
    fun fetchAdditionalRepos()

}