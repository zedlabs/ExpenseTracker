package ml.zedlabs.data.local_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ml.zedlabs.tbd.databases.media_db.Converters

//sample DB model for ROOM with a optional type convertor for complex types
@Entity
@TypeConverters(Converters::class)
data class AddedList(
    @PrimaryKey
    @ColumnInfo(name = "media_id") val mediaId: Int,
    @ColumnInfo(name = "media_name") val title: String?,
    //@ColumnInfo(name = "imdb_rating") val imdbRating: Double,
    @ColumnInfo(name = "language") val language: String?,
    @ColumnInfo(name = "release_year") val releaseYear: String?,
    @ColumnInfo(name = "tags") val tags: List<String>?,
    @ColumnInfo(name = "cast") val cast: List<String>?,
    @ColumnInfo(name = "created_by") val createdBy: List<String>?,
    @ColumnInfo(name = "media_desc") val description: String?,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "user_rating") val userRating: Double,
    // specific for tv
    @ColumnInfo(name = "seasons_watched") val seasonsWatched: Int?,
    @ColumnInfo(name = "episodes_watched") val episodesWatched: Int?,
    @ColumnInfo(name = "total_seasons") val totalSeasons: Int,
    @ColumnInfo(name = "total_episodes") val totalEpisodes: Int,
    // runtime per ep for tv, for entire length for Movies
    @ColumnInfo(name = "runtime") val runtime: Int,
    @ColumnInfo(name = "last_update") val lastUpdateMs: Long,
    @ColumnInfo(name = "seasons") val seasons: List<Int>?,

    @ColumnInfo(name = "rating") val rating: Double,
    @ColumnInfo(name = "vote_count") val voteCount: Double,
)

data class WatchStatusUpdate(
    @ColumnInfo(name = "media_id") val mediaId: Int,
    @ColumnInfo(name = "user_rating") val userRating: Double,
    @ColumnInfo(name = "seasons_watched") val seasonsWatched: Int?,
    @ColumnInfo(name = "episodes_watched") val episodesWatched: Int?,
    @ColumnInfo(name = "last_update") val lastUpdateMs: Long,
)


