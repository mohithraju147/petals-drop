package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PetalDropDao {
    // Customers
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomersFlow(): Flow<List<Customer>>

    @Query("SELECT * FROM customers")
    suspend fun getAllCustomers(): List<Customer>

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Int): Customer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer): Long

    @Update
    suspend fun updateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    // Deliveries
    @Query("SELECT * FROM deliveries WHERE date = :date")
    fun getDeliveriesByDateFlow(date: String): Flow<List<DeliveryRecord>>

    @Query("SELECT * FROM deliveries WHERE date = :date")
    suspend fun getDeliveriesByDate(date: String): List<DeliveryRecord>

    @Query("SELECT * FROM deliveries WHERE customerId = :customerId")
    fun getDeliveriesByCustomerFlow(customerId: Int): Flow<List<DeliveryRecord>>

    @Query("SELECT * FROM deliveries")
    suspend fun getAllDeliveries(): List<DeliveryRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDelivery(delivery: DeliveryRecord): Long

    // Payments
    @Query("SELECT * FROM payments WHERE month = :month")
    fun getPaymentsByMonthFlow(month: String): Flow<List<PaymentRecord>>

    @Query("SELECT * FROM payments WHERE month = :month")
    suspend fun getPaymentsByMonth(month: String): List<PaymentRecord>

    @Query("SELECT * FROM payments WHERE customerId = :customerId")
    fun getPaymentsByCustomerFlow(customerId: Int): Flow<List<PaymentRecord>>

    @Query("SELECT * FROM payments")
    suspend fun getAllPayments(): List<PaymentRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentRecord): Long

    @Update
    suspend fun updatePayment(payment: PaymentRecord)
}
