package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val address: String,
    val area: String,
    val flowerType: String,
    val monthlyAmount: Double,
    val deliveryNotes: String = "",
    val startDate: String = "",
    val active: Boolean = true
)

@Entity(tableName = "deliveries")
data class DeliveryRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerId: Int,
    val date: String, // YYYY-MM-DD
    val status: String, // "DELIVERED", "NOT_DELIVERED", "SKIPPED"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "payments")
data class PaymentRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerId: Int,
    val month: String, // YYYY-MM
    val status: String, // "PAID", "PARTIALLY_PAID", "PENDING"
    val amountPaid: Double,
    val balanceDue: Double,
    val paymentDate: String = "", // YYYY-MM-DD
    val notes: String = ""
)
