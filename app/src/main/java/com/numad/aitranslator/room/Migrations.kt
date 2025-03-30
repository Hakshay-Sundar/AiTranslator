package com.numad.aitranslator.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Migration Code goes here. For example:
            // database.execSQL("ALTER TABLE products ADD COLUMN productDescription TEXT");
        }
    }

    val MIGRATIONS = arrayOf(
        MIGRATION_1_2,
        // Add more migrations as needed
    )
}