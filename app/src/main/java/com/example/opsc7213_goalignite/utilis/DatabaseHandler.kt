package com.example.opsc7213_goalignite.utilis

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.opsc7213_goalignite.model.ToDoModel


class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "toDoListDatabase"
        private const val TABLE_TODO = "todo"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TASK = "task"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_SYNCED = "synced"
        private const val CREATE_TABLE_TODO = "CREATE TABLE $TABLE_TODO ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TASK TEXT, $COLUMN_STATUS INTEGER, $COLUMN_SYNCED INTEGER DEFAULT 0)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_TODO)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TODO")
        onCreate(db)
    }

    fun insertTask(task: ToDoModel) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TASK, task.task)
        values.put(COLUMN_STATUS, task.status)
        values.put(COLUMN_SYNCED, if (task.synced) 1 else 0) // Store as 1 or 0
        db.insert(TABLE_TODO, null, values)
        db.close()
    }


    fun getAllTasks(): List<ToDoModel> {
        val taskList = mutableListOf<ToDoModel>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.query(
            TABLE_TODO,
            arrayOf(COLUMN_ID, COLUMN_TASK, COLUMN_STATUS),
            null,
            null,
            null,
            null,
            null
        )
        cursor?.use { cur ->
            if (cur.moveToFirst()) {
                do {
                    val idIndex = cur.getColumnIndex(COLUMN_ID)
                    val taskIndex = cur.getColumnIndex(COLUMN_TASK)
                    val statusIndex = cur.getColumnIndex(COLUMN_STATUS)

                    if (idIndex != -1 && taskIndex != -1 && statusIndex != -1) {
                        val task = ToDoModel(
                            id = cur.getInt(idIndex),
                            task = cur.getString(taskIndex),
                            status = cur.getInt(statusIndex)
                        )
                        taskList.add(task)
                    }
                } while (cur.moveToNext())
            }
        }
        return taskList
    }

    fun updateStatus(id: Int, status: Int) {
        val cv = ContentValues().apply {
            put(COLUMN_STATUS, status)
        }
        val db = this.writableDatabase
        db.update(TABLE_TODO, cv, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun updateTask(id: Int, task: String) {
        val cv = ContentValues().apply {
            put(COLUMN_TASK, task)
        }
        val db = this.writableDatabase
        db.update(TABLE_TODO, cv, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteTask(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_TODO, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
    fun getUnsyncedTasks(): List<ToDoModel> {
        val unsyncedTaskList = mutableListOf<ToDoModel>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.query(
            TABLE_TODO,
            arrayOf(COLUMN_ID, COLUMN_TASK, COLUMN_STATUS, COLUMN_SYNCED),
            "$COLUMN_SYNCED = ?",
            arrayOf("0"), // Only fetch unsynced tasks
            null,
            null,
            null
        )
        cursor?.use { cur ->
            if (cur.moveToFirst()) {
                do {
                    val idIndex = cur.getColumnIndex(COLUMN_ID)
                    val taskIndex = cur.getColumnIndex(COLUMN_TASK)
                    val statusIndex = cur.getColumnIndex(COLUMN_STATUS)
                    val syncedIndex = cur.getColumnIndex(COLUMN_SYNCED)

                    if (idIndex != -1 && taskIndex != -1 && statusIndex != -1 && syncedIndex != -1) {
                        val task = ToDoModel(
                            id = cur.getInt(idIndex),
                            task = cur.getString(taskIndex),
                            status = cur.getInt(statusIndex),
                            synced = cur.getInt(syncedIndex) == 1 // Convert to Boolean
                        )
                        unsyncedTaskList.add(task)
                    }
                } while (cur.moveToNext())
            }
        }
        return unsyncedTaskList
    }
    fun markTaskAsSynced(id: Int) {
        val cv = ContentValues().apply {
            put(COLUMN_SYNCED, 1) // Set synced status to 1 (true)
        }
        val db = this.writableDatabase
        db.update(TABLE_TODO, cv, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

}