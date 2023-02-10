package ml.zedlabs.tbd.databases.expense_type_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExpenseTypeItem(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "type_id")
    val typeId: Int = 0,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "dot_color")
        val dotColor: String
)