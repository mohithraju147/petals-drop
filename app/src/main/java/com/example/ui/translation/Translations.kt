package com.example.ui.translation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

enum class AppLanguage {
    ENGLISH, KANNADA
}

object Translations {
    private val en = mapOf(
        "app_title" to "PetalDrop",
        "language_toggle" to "ಕನ್ನಡ",
        "lang_name" to "English",
        
        // Dashboard
        "dashboard" to "Dashboard",
        "total_customers" to "Total Customers",
        "delivered_today" to "Delivered Today",
        "pending_deliveries" to "Pending Today",
        "paid_customers_month" to "Paid This Month",
        "pending_customers_month" to "Pending This Month",
        "total_collections_month" to "Collected This Month",
        "total_pending_amount" to "Total Outstanding",
        "quick_actions" to "Quick Actions",
        "add_customer" to "Add Customer",
        "view_deliveries" to "Today's Deliveries",
        "view_payments" to "Pending Payments",
        "view_reports" to "Reports & Backups",
        "reminder_pills" to "Dues Reminders",
        "customer_management" to "Customer Directory",

        // Customer Profile
        "cust_name" to "Customer Name",
        "phone_number" to "Phone Number",
        "address" to "Address",
        "area_route" to "Area / Route",
        "flower_type" to "Flower Type",
        "monthly_amount" to "Monthly Amount (₹)",
        "delivery_notes" to "Delivery Notes",
        "start_date" to "Start Date",
        "status" to "Status",
        "active" to "Active",
        "inactive" to "Inactive",
        "save_customer" to "Save Customer",
        "edit_customer" to "Edit Customer Profile",
        "delete_customer" to "Delete Customer",
        "confirm_delete" to "Are you sure you want to delete this customer? This will also remove their delivery and payment history.",
        "search_customers" to "Search Customers...",
        "all_areas" to "All Areas",
        "filter_payment_status" to "Payment Status",
        "filter_active" to "Active Only",
        "filter_all" to "All Status",
        
        // Delivery screen
        "deliveries" to "Daily Deliveries",
        "marked_delivered" to "Delivered",
        "marked_not_delivered" to "Not Delivered",
        "marked_skipped" to "Skipped",
        "set_delivered" to "Mark Delivered",
        "set_not_delivered" to "Mark Not Delivered",
        "set_skipped" to "Mark Skipped",
        "delivery_status" to "Delivery Status",
        "delivery_history" to "Delivery History",
        "calendar_view" to "Calendar (Past Days)",
        "today_completed" to "Today's deliveries list is up to date!",

        // Payments
        "payments" to "Payment Book",
        "amount_paid" to "Amount Paid (₹)",
        "balance_due" to "Balance Due (₹)",
        "payment_date" to "Payment Date",
        "payment_notes" to "Payment Notes (e.g., GPay, Cash)",
        "month_cycle" to "Monthly Cycle",
        "mark_paid" to "Mark as Paid",
        "mark_partially_paid" to "Partially Paid",
        "mark_pending" to "Stay Pending",
        "outstanding_per_customer" to "Dues per Customer",
        "paid" to "Paid",
        "partial" to "Partially Paid",
        "pending" to "Pending",

        // Reports
        "reports" to "Business Reports & Backups",
        "daily_report" to "Daily Report",
        "monthly_report" to "Monthly Report",
        "yearly_report" to "Yearly Report",
        "revenue_collected" to "Collected Revenue",
        "revenue_pending" to "Pending Revenue",
        "top_paying" to "Top Paying Customers",
        "area_revenue" to "Area-wise Revenue",
        "total_deliveries" to "Total Daily Runs",
        "successful_deliveries" to "Successful Deliveries",
        "missed_deliveries" to "Missed Deliveries",
        "skipped_deliveries" to "Skipped Deliveries",
        "revenue_trend" to "Revenue & Collection Trends",
        "payment_status_pie" to "Payment Status Breakdown",

        // WhatsApp Reminder
        "whatsapp_remind" to "WhatsApp Reminder",
        "click_to_remind" to "Send WhatsApp Reminder",
        "default_reminder_msg" to "Namaskara. Your flower subscription payment for this month is pending. Kindly make the payment. Thank you.",
        "remind_not_provided" to "Phone number not available or invalid.",

        // Backup
        "backup_header" to "Manual Data Backup & Restore",
        "backup_instructions" to "Copy the code below and save it somewhere safe. To restore, paste the saved code and click 'Restore'.",
        "copy_backup" to "Copy Backup Code",
        "paste_backup_here" to "Paste Backup Code Here",
        "restore_backup" to "Restore from Code",
        "backup_copied" to "Backup code copied to clipboard!",
        "backup_failed" to "Restore failed! Please check if the code is valid.",
        "backup_success" to "Data restored successfully!",
        "export_pdf_customer" to "Export Customer List (PDF)",
        "export_pdf_payment" to "Export Payments List (PDF)",
        "export_csv_excel" to "Export Monthly to CSV / Excel",
        "exported_success" to "Report shared successfully!",
        
        // Miscellaneous
        "add_success" to "Customer added successfully!",
        "update_success" to "Customer profile updated!",
        "phone_field_desc" to "10-digit number",
        "empty_customer_list" to "No customers found. Click '+' to add your first customer!",
        "back_to_dashboard" to "Back to Dashboard",
        "accessibility_large_fonts" to "Large Text Mode",
        "accessibility_high_contrast" to "High Contrast Mode"
    )

    private val kn = mapOf(
        "app_title" to "ಪೆಟಲ್ ಡ್ರಾಪ್ (PetalDrop)",
        "language_toggle" to "English",
        "lang_name" to "ಕನ್ನಡ",
        
        // Dashboard
        "dashboard" to "ಡ್ಯಾಶ್‌ಬೋರ್ಡ್",
        "total_customers" to "ಒಟ್ಟು ಗ್ರಾಹಕರು",
        "delivered_today" to "ಇಂದು ತಲುಪಿಸಲಾಗಿದೆ",
        "pending_deliveries" to "ಇಂದು ಬಾಕಿ ಇರುವುದು",
        "paid_customers_month" to "ಈ ತಿಂಗಳು ಹಣ ಪಾವತಿಸಿದವರು",
        "pending_customers_month" to "ಈ ತಿಂಗಳು ಹಣ ಬಾಕಿ ಇರುವವರು",
        "total_collections_month" to "ಈ ತಿಂಗಳ ಒಟ್ಟು ಸಂಗ್ರಹ",
        "total_pending_amount" to "ಒಟ್ಟು ಬರಬೇಕಾದ ಬಾಕಿ ಹಣ",
        "quick_actions" to "ತ್ವರಿತ ಕ್ರಿಯೆಗಳು",
        "add_customer" to "ಹೊಸ ಗ್ರಾಹಕರ ಸೇರ್ಪಡೆ",
        "view_deliveries" to "ಇಂದಿನ ಹೂವಿನ ವಿತರಣೆ",
        "view_payments" to "ಬಾಕಿ ಹಣದ ವಿವರ",
        "view_reports" to "ವರದಿಗಳು ಮತ್ತು ಬ್ಯಾಕಪ್",
        "reminder_pills" to "ಬಾಕಿ ಹಣದ ಜ್ಞಾಪನೆ",
        "customer_management" to "ಗ್ರಾಹಕರ ವಿವರಗಳು",

        // Customer Profile
        "cust_name" to "ಗ್ರಾಹಕರ ಹೆಸರು",
        "phone_number" to "ಮೊಬೈಲ್ ಸಂಖ್ಯೆ",
        "address" to "ವಿಳಾಸ",
        "area_route" to "ಬಡಾವಣೆ / ಮಾರ್ಗ",
        "flower_type" to "ಹೂವಿನ ವಿಧ",
        "monthly_amount" to "ತಿಂಗಳ ಚಂದಾ ಹಣ (₹)",
        "delivery_notes" to "ವಿತರಣಾ ಸೂಚನೆಗಳು",
        "start_date" to "ಪ್ರಾರಂಭದ ದಿನಾಂಕ",
        "status" to "ಸ್ಥಿತಿ",
        "active" to "ಸಕ್ರಿಯ (चालू)",
        "inactive" to "ನಿಷ್ಕ್ರಿಯ (बंद)",
        "save_customer" to "ಗ್ರಾಹಕರ ವಿವರ ಉಳಿಸಿ",
        "edit_customer" to "ಗ್ರಾಹಕರ ಮರು-ಸಂಪಾದನೆ",
        "delete_customer" to "ಗ್ರಾಹಕರನ್ನು ತೆಗೆದುಹಾಕಿ",
        "confirm_delete" to "ಖಂಡಿತವಾಗಿಯೂ ಈ ಗ್ರಾಹಕರನ್ನು ತೆಗೆದುಹಾಕಬೇಕೆ? ಇದರಿಂದ ಅವರ ವಿತರಣೆ ಮತ್ತು ಪಾವತಿ ಇತಿಹಾಸವು ಸಂಪೂರ್ಣವಾಗಿ ಅಳಿಸಲ್ಪಡುತ್ತದೆ.",
        "search_customers" to "ಗ್ರಾಹಕರನ್ನು ಹುಡುಕಿ...",
        "all_areas" to "ಎಲ್ಲಾ ಬಡಾವಣೆಗಳು",
        "filter_payment_status" to "ಪಾವತಿ ಸ್ಥಿತಿ",
        "filter_active" to "ಸಕ್ರಿಯ ಗ್ರಾಹಕರು ಮಾತ್ರ",
        "filter_all" to "ಎಲ್ಲ ಗ್ರಾಹಕರು",
        
        // Delivery screen
        "deliveries" to "ದೈನಂದಿನ ಹೂವಿನ ವಿತರಣೆ",
        "marked_delivered" to "ತಲುಪಿಸಲಾಗಿದೆ",
        "marked_not_delivered" to "ತಲುಪಿಸಲಾಗಿಲ್ಲ",
        "marked_skipped" to "ಬಿಟ್ಟುಬಿಡಲಾಗಿದೆ",
        "set_delivered" to "ತಲುಪಿಸಲಾಗಿದೆ ಎಂದು ಗುರುತಿಸಿ",
        "set_not_delivered" to "ತಲುಪಿಸಿಲ್ಲ ಎಂದು ಗುರುತಿಸಿ",
        "set_skipped" to "ಬಿಡಲಾಗಿದೆ ಎಂದು ಗುರುತಿಸಿ",
        "delivery_status" to "ವಿತರಣಾ ಸ್ಥಿತಿ",
        "delivery_history" to "ಹಿಂದಿನ ವಿತರಣಾ ವಿವರಗಳು",
        "calendar_view" to "ಕ್ಯಾಲೆಂಡರ್ ಇತಿಹಾಸ",
        "today_completed" to "ಇಂದಿನ ಹೂವಿನ ವಿತರಣಾ ಪಟ್ಟಿ ಪೂರ್ಣಗೊಂಡಿದೆ!",

        // Payments
        "payments" to "ಪಾವತಿ ಪುಸ್ತಕ",
        "amount_paid" to "ಪಾವತಿಸಿದ ಹಣ (₹)",
        "balance_due" to "ಬಾಕಿ ಉಳಿದ ಹಣ (₹)",
        "payment_date" to "ಪಾವತಿಸಿದ ದಿನಾಂಕ",
        "payment_notes" to "ಪಾವತಿ ಟಿಪ್ಪಣಿ (ಉದಾ. ಫೋನ್ ಪೇ, ನಗದು)",
        "month_cycle" to "ತಿಂಗಳ ಸೈಕಲ್",
        "mark_paid" to "ಪೂರ್ಣ ಪಾವತಿಸಲಾಗಿದೆ",
        "mark_partially_paid" to "ಭಾಗಶಃ ಪಾವತಿಸಲಾಗಿದೆ",
        "mark_pending" to "ಬಾಕಿ ಉಳಿಸಿ",
        "outstanding_per_customer" to "ಗ್ರಾಹಕರ ಬಾಕಿ ಹಣದ ಪಟ್ಟಿ",
        "paid" to "ಪಾವತಿಸಲಾಗಿದೆ",
        "partial" to "ಭಾಗಶಃ ಪಾವತಿ",
        "pending" to "ಬಾಕಿ ಇದೆ",

        // Reports
        "reports" to "ವ್ಯವಹಾರದ ವರದಿಗಳು ಮತ್ತು ಬ್ಯಾಕಪ್",
        "daily_report" to "ದೈನಂದಿನ ವರದಿ",
        "monthly_report" to "ಮಾಸಿಕ ವರದಿ",
        "yearly_report" to "ವಾರ್ಷಿಕ ವರದಿ",
        "revenue_collected" to "ಸಂಗ್ರಹವಾದ ಹಣ",
        "revenue_pending" to "ಬರಬೇಕಾದ ಬಾಕಿ ಹಣ",
        "top_paying" to "ಅತಿ ಹೆಚ್ಚು ಹಣ ನೀಡುವ ಗ್ರಾಹಕರು",
        "area_revenue" to "ಬಡಾವಣೆವಾರು ಆದಾಯ",
        "total_deliveries" to "ಒಟ್ಟು ಹೂವಿನ ವಿತರಣಾ ಪ್ರಯತ್ನಗಳು",
        "successful_deliveries" to "ಯಶಸ್ವಿಯಾಗಿ ತಲುಪಿಸಲಾಗಿದೆ",
        "missed_deliveries" to "ತಲುಪಿಸದ ದಿನಗಳು",
        "skipped_deliveries" to "ಬಿಟ್ಟುಹೋದ ದಿನಗಳು",
        "revenue_trend" to "ಸಂಗ್ರಹಣೆ ಮತ್ತು ಆದಾಯದ ಅಂಕಿ-ಅಂಶ",
        "payment_status_pie" to "ಪಾವತಿ ಸ್ಥಿತಿ ವಿಶ್ಲೇಷಣೆ",

        // WhatsApp Reminder
        "whatsapp_remind" to "ವಾಟ್ಸಾಪ್ ಜ್ಞಾಪನೆ",
        "click_to_remind" to "ವಾಟ್ಸಾಪ್ ಜ್ಞಾಪನೆ ಕಳುಹಿಸಿ",
        "default_reminder_msg" to "ನಮಸ್ಕಾರ. ಈ ತಿಂಗಳ ನಿಮ್ಮ ಹೂವಿನ ಚಂದಾ ಹಣ ಪಾವತಿ ಬಾಕಿ ಇದೆ. ದಯವಿಟ್ಟು ಹಣವನ್ನು ಪಾವತಿಸಿ. ಧನ್ಯವಾದಗಳು.",
        "remind_not_provided" to "ಮೊಬೈಲ್ ಸಂಖ್ಯೆ ಲಭ್ಯವಿಲ್ಲ ಅಥವಾ ಸರಿಯಾಗಿಲ್ಲ.",

        // Backup
        "backup_header" to "ಡೇಟಾ ಬ್ಯಾಕಪ್ ಮತ್ತು ಮರುಸ್ಥಾಪನೆ",
        "backup_instructions" to "ಕೆಳಗಿನ ಕೋಡ್ ಅನ್ನು ನಕಲಿಸಿ ಮತ್ತು ಧೃಡವಾಗಿ ಇಟ್ಟುಕೊಳ್ಳಿ. ಮರುಸ್ಥಾಪಿಸಲು ಆ ಕೋಡ್ ಅನ್ನು ಇಲ್ಲಿ ಪೇಸ್ಟ್ ಮಾಡಿ ಮತ್ತು 'ಮರುಸ್ಥಾಪಿಸಿ' ಕ್ಲಿಕ್ ಮಾಡಿ.",
        "copy_backup" to "ಬ್ಯಾಕಪ್ ಕೋಡ್ ನಕಲಿಸಿ",
        "paste_backup_here" to "ಬ್ಯಾಕಪ್ ಕೋಡ್ ಅನ್ನು ಇಲ್ಲಿ ಪೇಸ್ಟ್ ಮಾಡಿ",
        "restore_backup" to "ಕೋಡ್‌ನಿಂದ ಡೇಟಾ ಮರುಸ್ಥಾಪಿಸಿ",
        "backup_copied" to "ಬ್ಯಾಕಪ್ ಕೋಡ್ ನಕಲಿಸಲಾಗಿದೆ!",
        "backup_failed" to "ಡೇಟಾ ಮರುಸ್ಥಾಪನೆ ವಿಫಲವಾಗಿದೆ! ದಯವಿಟ್ಟು ಕೋಡ್ ಪರಿಶೀಲಿಸಿ.",
        "backup_success" to "ಡೇಟಾವನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಮರುಸ್ಥಾಪಿಸಲಾಗಿದೆ!",
        "export_pdf_customer" to "ಗ್ರಾಹಕರ ಪಟ್ಟಿ ಪಿಡಿಎಫ್ ಡೌನ್‌ಲೋಡ್",
        "export_pdf_payment" to "ಪಾವತಿಗಳ ಪಟ್ಟಿ ಪಿಡಿಎಫ್ ಡೌನ್‌ಲೋಡ್",
        "export_csv_excel" to "ತಿಂಗಳ ವರದಿ ಎಕ್ಸೆಲ್ (CSV) ಗೆ ಹಂಚಿ",
        "exported_success" to "ವರದಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಹಂಚಿಕೊಳ್ಳಲಾಗಿದೆ!",

        // Miscellaneous
        "add_success" to "ಹೊಸ ಗ್ರಾಹಕರನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಸೇರಿಸಲಾಗಿದೆ!",
        "update_success" to "ಗ್ರಾಹಕರ ವಿವರಗಳನ್ನು ನವೀಕರಿಸಲಾಗಿದೆ!",
        "phone_field_desc" to "೧೦ ಅಂಕಿಗಳ ಮೊಬೈಲ್ ಸಂಖ್ಯೆ",
        "empty_customer_list" to "ಯಾವುದೇ ಗ್ರಾಹಕರು ಸಿಗಲಿಲ್ಲ. ಹೊಸ ಗ್ರಾಹಕರನ್ನು ಸೇರಿಸಲು '+' ಕ್ಲಿಕ್ ಮಾಡಿ!",
        "back_to_dashboard" to "ಡ್ಯಾಶ್‌ಬೋರ್ಡ್‌ಗೆ ಹಿಂತಿರುಗಿ",
        "accessibility_large_fonts" to "ದೊಡ್ಡ ಅಕ್ಷರಗಳ ಮೋಡ್",
        "accessibility_high_contrast" to "ಹೆಚ್ಚಿನ ಕಾಂಟ್ರಾಸ್ಟ್ ಮೋಡ್"
    )

    fun getString(key: String, language: AppLanguage): String {
        return if (language == AppLanguage.KANNADA) {
            kn[key] ?: en[key] ?: key
        } else {
            en[key] ?: key
        }
    }
}

val LocalAppLanguage = staticCompositionLocalOf { AppLanguage.ENGLISH }

@Composable
fun stringResource(key: String): String {
    val currentLang = LocalAppLanguage.current
    return Translations.getString(key, currentLang)
}
