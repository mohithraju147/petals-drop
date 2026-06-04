package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.ui.translation.AppLanguage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PetalDropViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PetalDropRepository
    val context: Context = application.applicationContext

    // UI Configuration States
    val currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val isLargeFont = MutableStateFlow(false)
    val isHighContrast = MutableStateFlow(false)

    // Routing Active Tab State
    val activeMenuTab = MutableStateFlow("dashboard") // dashboard, customers, deliveries, payments, reports, add_edit_customer

    // Filter and Search states
    val customerSearchQuery = MutableStateFlow("")
    val filterArea = MutableStateFlow("All")
    val filterPaymentStatus = MutableStateFlow("All") // All, Paid, Partially Paid, Pending
    val filterActiveOnly = MutableStateFlow(true)

    // Calendar & Month States
    val selectedDate = MutableStateFlow(getTodayDateString())
    val selectedMonth = MutableStateFlow(getCurrentMonthString())

    // Editing State
    val selectedCustomerForEdit = MutableStateFlow<Customer?>(null)

    // Database flow streams
    val allCustomers: StateFlow<List<Customer>>
    val currentDeliveries: StateFlow<List<DeliveryRecord>>
    val currentPayments: StateFlow<List<PaymentRecord>>

    init {
        val database = PetalDropDatabase.getDatabase(application)
        val dao = database.petalDropDao()
        repository = PetalDropRepository(dao)

        allCustomers = repository.allCustomers
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        currentDeliveries = selectedDate
            .flatMapLatest { date -> repository.getDeliveriesByDate(date) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        currentPayments = selectedMonth
            .flatMapLatest { month -> repository.getPaymentsByMonth(month) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Initial Seed of realistic records
        viewModelScope.launch {
            repository.seedSampleData(context)
        }
    }

    // Helper Date generators
    companion object {
        fun getTodayDateString(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }

        fun getCurrentMonthString(): String {
            val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
            return sdf.format(Date())
        }
    }

    // Languages Toggle
    fun toggleLanguage() {
        currentLanguage.value = if (currentLanguage.value == AppLanguage.ENGLISH) {
            AppLanguage.KANNADA
        } else {
            AppLanguage.ENGLISH
        }
    }

    // Core Customer operations
    fun addCustomer(customer: Customer) {
        viewModelScope.launch {
            repository.insertCustomer(customer)
            // Automatically make sure their current month payment is initialized as PENDING
            val monthStr = selectedMonth.value
            val existingPayments = repository.getPaymentsByMonth(monthStr).firstOrNull() ?: emptyList()
            // Wait, we can generate a new one
            allCustomers.value.firstOrNull { it.name == customer.name }?.let { saved ->
                val alreadyHas = existingPayments.any { it.customerId == saved.id }
                if (!alreadyHas) {
                    repository.insertPayment(
                        PaymentRecord(
                            customerId = saved.id,
                            month = monthStr,
                            status = "PENDING",
                            amountPaid = 0.0,
                            balanceDue = customer.monthlyAmount
                        )
                    )
                }
            }
            Toast.makeText(context, "Customer saved successfully!", Toast.LENGTH_SHORT).show()
            activeMenuTab.value = "customers"
        }
    }

    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            repository.updateCustomer(customer)
            Toast.makeText(context, "Customer profile updated!", Toast.LENGTH_SHORT).show()
            activeMenuTab.value = "customers"
            selectedCustomerForEdit.value = null
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            repository.deleteCustomer(customer)
            Toast.makeText(context, "Customer profile deleted!", Toast.LENGTH_SHORT).show()
        }
    }

    // Delivery record state mutation
    fun recordDelivery(customerId: Int, status: String) {
        viewModelScope.launch {
            val dateStr = selectedDate.value
            val existingDeliveries = repository.getAllDeliveries()
            val match = existingDeliveries.find { it.customerId == customerId && it.date == dateStr }
            
            if (match != null) {
                repository.insertDelivery(match.copy(status = status, timestamp = System.currentTimeMillis()))
            } else {
                repository.insertDelivery(
                    DeliveryRecord(
                        customerId = customerId,
                        date = dateStr,
                        status = status,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    // Payment operations (Paid, Partially Paid, Pending)
    fun recordPayment(customerId: Int, status: String, amountPaid: Double, balanceDue: Double, notes: String) {
        viewModelScope.launch {
            val monthStr = selectedMonth.value
            val allPayments = repository.getAllPayments()
            val match = allPayments.find { it.customerId == customerId && it.month == monthStr }

            val recordDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            if (match != null) {
                repository.insertPayment(
                    match.copy(
                        status = status,
                        amountPaid = amountPaid,
                        balanceDue = balanceDue,
                        paymentDate = if (status == "PENDING") "" else recordDate,
                        notes = notes
                    )
                )
            } else {
                repository.insertPayment(
                    PaymentRecord(
                        customerId = customerId,
                        month = monthStr,
                        status = status,
                        amountPaid = amountPaid,
                        balanceDue = balanceDue,
                        paymentDate = if (status == "PENDING") "" else recordDate,
                        notes = notes
                    )
                )
            }
        }
    }

    // Backups Export / Import
    private val _backupCode = MutableStateFlow("")
    val backupCode: StateFlow<String> = _backupCode

    fun refreshBackupCode() {
        viewModelScope.launch {
            _backupCode.value = repository.exportBackupJson()
        }
    }

    fun importBackup(jsonStr: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val success = repository.importBackupJson(jsonStr)
            if (success) {
                onSuccess()
            } else {
                onError()
            }
        }
    }

    // Action Share PDF / CSV details
    fun shareTextReport(reportTitle: String, content: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, reportTitle)
            putExtra(Intent.EXTRA_TEXT, content)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(shareIntent)
    }

    // Generating Dynamic Shares
    fun generateCustomerPdfText(customers: List<Customer>): String {
        val sb = StringBuilder()
        sb.append("===================================\n")
        sb.append("       PETALDROP CUSTOMER LIST     \n")
        sb.append("===================================\n\n")
        customers.forEachIndexed { i, c ->
            sb.append("${i + 1}. ${c.name}\n")
            sb.append("   Phone: ${c.phone}\n")
            sb.append("   Area: ${c.area}\n")
            sb.append("   Flower: ${c.flowerType}\n")
            sb.append("   Monthly: ₹${c.monthlyAmount}\n")
            sb.append("   Address: ${c.address}\n")
            sb.append("   Status: ${if (c.active) "Active" else "Inactive"}\n")
            sb.append("-----------------------------------\n")
        }
        sb.append("\nTotal Customers: ${customers.size}\n")
        sb.append("Generated on: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}\n")
        return sb.toString()
    }

    fun generatePaymentPdfText(customers: List<Customer>, payments: List<PaymentRecord>): String {
        val sb = StringBuilder()
        sb.append("===================================\n")
        sb.append("       PETALDROP PAYMENT REPORT    \n")
        sb.append("===================================\n\n")
        sb.append("Selected Month: ${selectedMonth.value}\n\n")

        var totalCollected = 0.0
        var totalOutstanding = 0.0

        customers.forEachIndexed { i, c ->
            val pay = payments.find { it.customerId == c.id }
            val status = pay?.status ?: "PENDING"
            val paid = pay?.amountPaid ?: 0.0
            val due = pay?.balanceDue ?: c.monthlyAmount
            
            totalCollected += paid
            totalOutstanding += due

            sb.append("${i + 1}. ${c.name} [Area: ${c.area}]\n")
            sb.append("   Subscription: ₹${c.monthlyAmount}\n")
            sb.append("   Status: $status\n")
            sb.append("   Paid: ₹$paid | Balance Due: ₹$due\n")
            if (pay != null && pay.notes.isNotEmpty()) {
                sb.append("   Notes: ${pay.notes}\n")
            }
            sb.append("-----------------------------------\n")
        }
        sb.append("\n===================================\n")
        sb.append("   TOTAL COLLECTION : ₹$totalCollected\n")
        sb.append("   TOTAL OUTSTANDING: ₹$totalOutstanding\n")
        sb.append("===================================\n")
        sb.append("Generated on: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}\n")
        return sb.toString()
    }

    fun generateMonthlyReportCsv(customers: List<Customer>, deliveries: List<DeliveryRecord>, payments: List<PaymentRecord>): String {
        val sb = StringBuilder()
        sb.append("Customer Name,Phone,Area,Flower Type,Monthly Fee,Status,Paid Amount,Dues,Notes\n")
        customers.forEach { c ->
            val pay = payments.find { it.customerId == c.id }
            val paid = pay?.amountPaid ?: 0.0
            val due = pay?.balanceDue ?: c.monthlyAmount
            val status = pay?.status ?: "PENDING"
            val notes = pay?.notes?.replace(",", ";") ?: ""
            
            sb.append("\"${c.name}\",\"${c.phone}\",\"${c.area}\",\"${c.flowerType}\",${c.monthlyAmount},\"$status\",$paid,$due,\"$notes\"\n")
        }
        return sb.toString()
    }
}
