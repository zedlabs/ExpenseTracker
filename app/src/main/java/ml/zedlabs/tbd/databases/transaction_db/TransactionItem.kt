package ml.zedlabs.tbd.databases.transaction_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionItem(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val transactionId: Int,

    @ColumnInfo(name = "is_expense")
    val isExpense: Boolean,

    //long ms time, not actual java timestamp
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "note")
    val note: String,

    @ColumnInfo(name = "type")
    val type: String,
)