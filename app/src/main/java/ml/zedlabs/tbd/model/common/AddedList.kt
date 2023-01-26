package ml.zedlabs.tbd.model.common

data class AddedList(
    val dbMediaId: Int,
    val title: String?,
    val description: String?,
    val rating: Double,
    val voteCount: Double,
    val language: String?,
    val releaseYear: String?,
    val tags: List<String>?,
    val cast: List<String>?,
    val createdBy: List<String>?,
    val posterPath: String?,
    val userRating: Double,
    val seasonsWatched: Int?,
    val episodesWatched: Int?,
    val totalSeasons: Int,
    val totalEpisodes: Int,
    val runtime: Int,
    val lastUpdateMs: Long,
    val seasons: List<Int>?,
)
