package ml.zedlabs.tbd.databases.media_db

import androidx.room.Database
import androidx.room.RoomDatabase
import ml.zedlabs.data.local_db.AddedList

@Database(entities = [AddedList::class], version = 7)
abstract class AddedListDatabase : RoomDatabase() {
    abstract fun addedListDao(): AddedListDao
}