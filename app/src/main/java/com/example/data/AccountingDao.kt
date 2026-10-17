package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountingDao {

    @Query("SELECT * FROM accounting_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<AccountingRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: AccountingRecord): Long

    @Query("DELETE FROM accounting_records WHERE id = :id")
    suspend fun deleteRecordById(id: Long)

    @Query("DELETE FROM accounting_records")
    suspend fun deleteAllRecords()
}
