package com.example.inventory.data

import android.widget.TimePicker
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import java.util.Date

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val descripcion: String,
    val type: Boolean, // 1 = task, 0 = note
    val status: Boolean, // 1 = finished, 0 = not finished
    val date: String,
    val photoUris: List<String> = emptyList(),
    val videoUris: List<String> = emptyList(),
    val audioUris: List<String> = emptyList(),
    val fileUris: List<String> = emptyList()
)
