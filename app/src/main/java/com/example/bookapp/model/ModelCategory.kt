package com.example.bookapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ModelCategory {
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = ""
    @ColumnInfo(name = "category")
    var category: String = ""
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0
    @ColumnInfo(name = "uid")
    var uid: String = ""

    constructor()
    constructor(id: String, category: String, timestamp: Long, uid: String) {
        this.id = id
        this.category = category
        this.timestamp = timestamp
        this.uid = uid
    }
}