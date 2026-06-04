package com.example.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

class PetalDropRepository(private val dao: PetalDropDao) {

    val allCustomers: Flow<List<Customer>> = dao.getAllCustomersFlow()

    fun getDeliveriesByDate(date: String): Flow<List<DeliveryRecord>> = dao.getDeliveriesByDateFlow(date)
    fun getDeliveriesByCustomer(customerId: Int): Flow<List<DeliveryRecord>> = dao.getDeliveriesByCustomerFlow(customerId)

    fun getPaymentsByMonth(month: String): Flow<List<PaymentRecord>> = dao.getPaymentsByMonthFlow(month)
    fun getPaymentsByCustomer(customerId: Int): Flow<List<PaymentRecord>> = dao.getPaymentsByCustomerFlow(customerId)

    suspend fun getAllCustomers(): List<Customer> = dao.getAllCustomers()
    suspend fun getCustomerById(id: Int): Customer? = dao.getCustomerById(id)

    suspend fun insertCustomer(customer: Customer): Long = dao.insertCustomer(customer)
    suspend fun updateCustomer(customer: Customer) = dao.updateCustomer(customer)
    suspend fun deleteCustomer(customer: Customer) = dao.deleteCustomer(customer)

    suspend fun getAllDeliveries(): List<DeliveryRecord> = dao.getAllDeliveries()
    suspend fun insertDelivery(delivery: DeliveryRecord): Long = dao.insertDelivery(delivery)

    suspend fun getAllPayments(): List<PaymentRecord> = dao.getAllPayments()
    suspend fun insertPayment(payment: PaymentRecord): Long = dao.insertPayment(payment)
    suspend fun updatePayment(payment: PaymentRecord) = dao.updatePayment(payment)

    suspend fun seedSampleData(context: Context) {
        val currentCustomers = dao.getAllCustomers()
        if (currentCustomers.isNotEmpty()) {
            return
        }

        // 1. Seed Customers
        val customersList = listOf(
            Customer(name = "Sunitha Ramesh", phone = "9876543210", address = "12, 4th Cross, Indiranagar", area = "Indiranagar", flowerType = "Marigold / ಕನಕಾಂಬರ", monthlyAmount = 600.0, deliveryNotes = "Leave at gate", startDate = "2026-05-01", active = true),
            Customer(name = "Ananth Kumar", phone = "9812345678", address = "Flat 302, Green Glen Layout", area = "Bellandur", flowerType = "Rose / ಗುಲಾಬಿ", monthlyAmount = 800.0, deliveryNotes = "Ring doorbell", startDate = "2026-05-01", active = true),
            Customer(name = "Meenakshi Vilas", phone = "8765432109", address = "Apartment 10A, Brigade Metropolis", area = "Mahadevapura", flowerType = "Jasmine / ಮಲ್ಲಿಗೆ", monthlyAmount = 1200.0, deliveryNotes = "Deliver by 6:30 AM", startDate = "2026-05-01", active = true),
            Customer(name = "Sridhar Shastri", phone = "7654321098", address = "56, 12th Main, Malleshwaram", area = "Malleshwaram", flowerType = "Mixed Flowers / ಬಿಡಿ ಹೂವು", monthlyAmount = 450.0, deliveryNotes = "Keep near Tulsi plant", startDate = "2026-05-01", active = true),
            Customer(name = "Vijay Lakshmi", phone = "6543210987", address = "112, 1st Stage, Jayanagar", area = "Jayanagar", flowerType = "Rose + Jasmine Duo", monthlyAmount = 1000.0, deliveryNotes = "Deliver on porch table", startDate = "2026-05-01", active = true),
            Customer(name = "Golden Bakes Café", phone = "9012345678", address = "89, Double Road, Indiranagar", area = "Indiranagar", flowerType = "Samanthi / ಸೇವಂತಿ", monthlyAmount = 1500.0, deliveryNotes = "Deliver at cash counter", startDate = "2026-05-02", active = true),
            Customer(name = "Shanthi Appts Office", phone = "9112233445", address = "Block B Recp, JP Nagar 5th Phase", area = "Jayanagar", flowerType = "Lotus / ಕಮಲ", monthlyAmount = 2000.0, deliveryNotes = "Corporate delivery", startDate = "2026-05-10", active = false)
        )

        val insertedIds = mutableListOf<Int>()
        for (customer in customersList) {
            val id = dao.insertCustomer(customer)
            insertedIds.add(id.toInt())
        }

        // Make historical deliveries for previous 4 days: 2026-06-01 to 2026-06-04
        val dates = listOf("2026-06-01", "2026-06-02", "2026-06-03", "2026-06-04")

        for (cIndex in insertedIds.indices) {
            if (cIndex >= 6) continue // skip inactive customer for deliveries
            val customerId = insertedIds[cIndex]

            for (dIndex in dates.indices) {
                val date = dates[dIndex]
                val status = if ((cIndex + dIndex) % 5 == 0) "SKIPPED" else if ((cIndex + dIndex) % 7 == 0) "NOT_DELIVERED" else "DELIVERED"
                dao.insertDelivery(
                    DeliveryRecord(
                        customerId = customerId,
                        date = date,
                        status = status,
                        timestamp = System.currentTimeMillis() - dIndex * 86400000
                    )
                )
            }
        }

        // Seed Payments for May 2026
        val payMay = listOf(
            PaymentRecord(customerId = insertedIds[0], month = "2026-05", status = "PAID", amountPaid = 600.0, balanceDue = 0.0, paymentDate = "2026-05-05", notes = "UPI Pay"),
            PaymentRecord(customerId = insertedIds[1], month = "2026-05", status = "PAID", amountPaid = 800.0, balanceDue = 0.0, paymentDate = "2026-05-04", notes = "Cash"),
            PaymentRecord(customerId = insertedIds[2], month = "2026-05", status = "PAID", amountPaid = 1200.0, balanceDue = 0.0, paymentDate = "2026-05-07", notes = "GPay"),
            PaymentRecord(customerId = insertedIds[3], month = "2026-05", status = "PARTIALLY_PAID", amountPaid = 200.0, balanceDue = 250.0, paymentDate = "2026-05-10", notes = "Partially paid, balance next month"),
            PaymentRecord(customerId = insertedIds[4], month = "2026-05", status = "PAID", amountPaid = 1000.0, balanceDue = 0.0, paymentDate = "2026-05-06", notes = "UPI"),
            PaymentRecord(customerId = insertedIds[5], month = "2026-05", status = "PAID", amountPaid = 1500.0, balanceDue = 0.0, paymentDate = "2026-05-12", notes = "Cash collected")
        )
        for (pay in payMay) {
            dao.insertPayment(pay)
        }

        // Seed Payments for June 2026 (Current Month)
        val payJune = listOf(
            PaymentRecord(customerId = insertedIds[0], month = "2026-06", status = "PAID", amountPaid = 600.0, balanceDue = 0.0, paymentDate = "2026-06-02", notes = "GPay"),
            PaymentRecord(customerId = insertedIds[1], month = "2026-06", status = "PENDING", amountPaid = 0.0, balanceDue = 800.0, paymentDate = "", notes = ""),
            PaymentRecord(customerId = insertedIds[2], month = "2026-06", status = "PARTIALLY_PAID", amountPaid = 800.0, balanceDue = 400.0, paymentDate = "2026-06-03", notes = "Paid bulk, will pay balance soon"),
            PaymentRecord(customerId = insertedIds[3], month = "2026-06", status = "PENDING", amountPaid = 0.0, balanceDue = 450.0, paymentDate = "", notes = ""),
            PaymentRecord(customerId = insertedIds[4], month = "2026-06", status = "PAID", amountPaid = 1000.0, balanceDue = 0.0, paymentDate = "2026-06-01", notes = "PhonePe"),
            PaymentRecord(customerId = insertedIds[5], month = "2026-06", status = "PENDING", amountPaid = 0.0, balanceDue = 1500.0, paymentDate = "", notes = "")
        )
        for (pay in payJune) {
            dao.insertPayment(pay)
        }
    }

    suspend fun exportBackupJson(): String {
        val root = JSONObject()
        
        val customersArray = JSONArray()
        dao.getAllCustomers().forEach {
            val obj = JSONObject()
            obj.put("id", it.id)
            obj.put("name", it.name)
            obj.put("phone", it.phone)
            obj.put("address", it.address)
            obj.put("area", it.area)
            obj.put("flowerType", it.flowerType)
            obj.put("monthlyAmount", it.monthlyAmount)
            obj.put("deliveryNotes", it.deliveryNotes)
            obj.put("startDate", it.startDate)
            obj.put("active", it.active)
            customersArray.put(obj)
        }
        root.put("customers", customersArray)

        val deliveriesArray = JSONArray()
        dao.getAllDeliveries().forEach {
            val obj = JSONObject()
            obj.put("id", it.id)
            obj.put("customerId", it.customerId)
            obj.put("date", it.date)
            obj.put("status", it.status)
            obj.put("timestamp", it.timestamp)
            deliveriesArray.put(obj)
        }
        root.put("deliveries", deliveriesArray)

        val paymentsArray = JSONArray()
        dao.getAllPayments().forEach {
            val obj = JSONObject()
            obj.put("id", it.id)
            obj.put("customerId", it.customerId)
            obj.put("month", it.month)
            obj.put("status", it.status)
            obj.put("amountPaid", it.amountPaid)
            obj.put("balanceDue", it.balanceDue)
            obj.put("paymentDate", it.paymentDate)
            obj.put("notes", it.notes)
            paymentsArray.put(obj)
        }
        root.put("payments", paymentsArray)

        return root.toString(2)
    }

    suspend fun importBackupJson(jsonString: String): Boolean {
        return try {
            val root = JSONObject(jsonString)
            
            val customers = root.optJSONArray("customers")
            val deliveries = root.optJSONArray("deliveries")
            val payments = root.optJSONArray("payments")

            if (customers != null) {
                dao.getAllCustomers().forEach { dao.deleteCustomer(it) }
                for (i in 0 until customers.length()) {
                    val obj = customers.getJSONObject(i)
                    dao.insertCustomer(
                        Customer(
                            id = obj.optInt("id", 0),
                            name = obj.getString("name"),
                            phone = obj.optString("phone", ""),
                            address = obj.optString("address", ""),
                            area = obj.optString("area", ""),
                            flowerType = obj.optString("flowerType", ""),
                            monthlyAmount = obj.optDouble("monthlyAmount", 0.0),
                            deliveryNotes = obj.optString("deliveryNotes", ""),
                            startDate = obj.optString("startDate", ""),
                            active = obj.optBoolean("active", true)
                        )
                    )
                }
            }

            if (deliveries != null) {
                // Clear state is managed dynamically by Room REPLACE/Updates
                for (i in 0 until deliveries.length()) {
                    val obj = deliveries.getJSONObject(i)
                    dao.insertDelivery(
                        DeliveryRecord(
                            id = obj.optInt("id", 0),
                            customerId = obj.getInt("customerId"),
                            date = obj.getString("date"),
                            status = obj.getString("status"),
                            timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                        )
                    )
                }
            }

            if (payments != null) {
                for (i in 0 until payments.length()) {
                    val obj = payments.getJSONObject(i)
                    dao.insertPayment(
                        PaymentRecord(
                            id = obj.optInt("id", 0),
                            customerId = obj.getInt("customerId"),
                            month = obj.getString("month"),
                            status = obj.getString("status"),
                            amountPaid = obj.optDouble("amountPaid", 0.0),
                            balanceDue = obj.optDouble("balanceDue", 0.0),
                            paymentDate = obj.optString("paymentDate", ""),
                            notes = obj.optString("notes", "")
                        )
                    )
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
