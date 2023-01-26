package ml.zedlabs.tbd.repository

// can be used if web scraping is required
class WebParsingRepository {

//    override suspend fun getImbdShowRating(imdbId: String): Resource<Double> {
//        val ratingDouble: Double
//        try {
//            val webPage = withContext(Dispatchers.IO){
//                return@withContext Jsoup.connect(imdbId.createImdbUrlFromImdbMediaId()).get()
//            }
//            val ratingElement = webPage.getElementsByClass(imdb_rating_class_id)
//            val rating = ratingElement.first()?.text()
//            ratingDouble = rating?.toDouble() ?: 0.0
//        } catch (e: Exception) {
//            /**
//             * use generic exception as multiple exceptions possible
//             * (Web, Url, NumberFormatting)
//             */
//            return Resource.Error(Throwable(e))
//        }
//            return Resource.Success(data = ratingDouble)
//    }
}