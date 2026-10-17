package com.example.data

import kotlinx.coroutines.flow.Flow

class AccountingRepository(private val dao: AccountingDao) {

    val allRecords: Flow<List<AccountingRecord>> = dao.getAllRecords()

    suspend fun saveRecord(record: AccountingRecord): Long {
        return dao.insertRecord(record)
    }

    suspend fun deleteRecord(id: Long) {
        dao.deleteRecordById(id)
    }

    suspend fun clearAll() {
        dao.deleteAllRecords()
    }
}
