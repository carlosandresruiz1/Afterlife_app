package com.example.afterlife

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "shopping.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_PRODUCTS = "products"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"
        
        private const val TABLE_USERS = "users"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createProductsTable = "CREATE TABLE $TABLE_PRODUCTS ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_PRICE REAL, $COLUMN_DESCRIPTION TEXT)"
        db?.execSQL(createProductsTable)
        
        val createUsersTable = "CREATE TABLE $TABLE_USERS ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_USERNAME TEXT, $COLUMN_PASSWORD TEXT)"
        db?.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropProductsTable = "DROP TABLE IF EXISTS $TABLE_PRODUCTS"
        db?.execSQL(dropProductsTable)
        
        val dropUsersTable = "DROP TABLE IF EXISTS $TABLE_USERS"
        db?.execSQL(dropUsersTable)
        
        onCreate(db)
    }

    fun insertProduct(product: Product) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_DESCRIPTION, product.description)
        }
        db.insert(TABLE_PRODUCTS, null, values)
        db.close()
    }

    fun insertUser(user: User) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_PASSWORD, user.password)
        }
        db.insert(TABLE_USERS, null, values)
        db.close()
    }

    fun getProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_PRODUCTS"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex(COLUMN_ID)
            val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
            val priceIndex = cursor.getColumnIndex(COLUMN_PRICE)
            val descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION)

            if (idIndex != -1 && nameIndex != -1 && priceIndex != -1 && descriptionIndex != -1) {
                val id = cursor.getInt(idIndex)
                val name = cursor.getString(nameIndex)
                val price = cursor.getDouble(priceIndex)
                val description = cursor.getString(descriptionIndex)
                products.add(Product(id, name, price, description))
            }
        }
        cursor.close()
        db.close()
        return products
    }

    fun showProducts() {
        val products = getProducts()
        for (product in products) {
            println("ID: ${product.id}, Name: ${product.name}, Price: ${product.price}, Description: ${product.description}")
        }
    }

    fun getProductById(id: Int): Product? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_PRODUCTS WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        return if (cursor.moveToFirst()) {
            val productId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            cursor.close()
            db.close()
            Product(productId, name, price, description)
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    fun updateProduct(product: Product) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_DESCRIPTION, product.description)
        }
        db.update(TABLE_PRODUCTS, values, "$COLUMN_ID = ?", arrayOf(product.id.toString()))
        db.close()
    }

    fun deleteProduct(id: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(id.toString())
        db.delete(TABLE_PRODUCTS, whereClause, whereArgs)
        db.close()
    }

}