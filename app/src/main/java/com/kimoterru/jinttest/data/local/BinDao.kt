package com.kimoterru.jinttest.data.local

import androidx.room.*
import com.kimoterru.jinttest.data.local.model.BinEntity

@Dao
interface BinDao {

    @Query("SELECT * FROM bin_table")
    suspend fun getAllBins():List<BinEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBin(vararg binItem: BinEntity)

    @Query("SELECT * FROM bin_table WHERE id = :id")
    suspend fun getBinById(id: Int): BinEntity
}