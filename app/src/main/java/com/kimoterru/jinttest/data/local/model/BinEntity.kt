package com.kimoterru.jinttest.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "bin_table", indices = [Index(value = ["binNumber"], unique = true)])
data class BinEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val binNumber: Long
)