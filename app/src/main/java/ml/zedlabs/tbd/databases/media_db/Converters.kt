package ml.zedlabs.tbd.databases.media_db

import androidx.room.TypeConverter
import com.google.gson.Gson
//Optional type converter for complex data types
class Converters {

    @TypeConverter
    fun listToJsonString(value: List<String?>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String?) = Gson().fromJson(value, Array<String?>::class.java)?.toList()

    @TypeConverter
    fun intListToJsonString(value: List<Int?>?): String? = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToIntList(value: String?) = Gson().fromJson(value, Array<Int?>::class.java)?.toList()
}