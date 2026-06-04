package com.example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.PetalDropViewModel
import com.example.ui.theme.*
import com.example.ui.translation.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private val viewModel: PetalDropViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
            val isLargeFont by viewModel.isLargeFont.collectAsStateWithLifecycle()
            val isHighContrast by viewModel.isHighContrast.collectAsStateWithLifecycle()

            CompositionLocalProvider(LocalAppLanguage provides language) {
                MyApplicationTheme(isHighContrast = isHighContrast) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing),
                        containerColor = MaterialTheme.colorScheme.background
                    ) { innerPadding ->
                        MainAppContent(
                            viewModel = viewModel,
                            isLargeFont = isLargeFont,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

// Global text size helper for elderly people
@Composable
fun getScaledFontSize(baseSp: Int, isLarge: Boolean): androidx.compose.ui.unit.TextUnit {
    return if (isLarge) (baseSp * 1.35).sp else baseSp.sp
}

@Composable
fun MainAppContent(
    viewModel: PetalDropViewModel,
    isLargeFont: Boolean,
    modifier: Modifier = Modifier
) {
    val activeTab by viewModel.activeMenuTab.collectAsStateWithLifecycle()
    val customers by viewModel.allCustomers.collectAsStateWithLifecycle()
    val deliveries by viewModel.currentDeliveries.collectAsStateWithLifecycle()
    val payments by viewModel.currentPayments.collectAsStateWithLifecycle()
    
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedMonth by viewModel.selectedMonth.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        // Dynamic accessible Top Header Area
        AppHeader(viewModel = viewModel, isLargeFont = isLargeFont)

        // Screen selection navigation content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (activeTab) {
                "dashboard" -> DashboardScreen(
                    viewModel = viewModel,
                    customers = customers,
                    deliveries = deliveries,
                    payments = payments,
                    isLargeFont = isLargeFont
                )
                "customers" -> CustomerListScreen(
                    viewModel = viewModel,
                    customers = customers,
                    payments = payments,
                    isLargeFont = isLargeFont
                )
                "deliveries" -> DailyDeliveryScreen(
                    viewModel = viewModel,
                    customers = customers,
                    deliveries = deliveries,
                    selectedDate = selectedDate,
                    isLargeFont = isLargeFont
                )
                "payments" -> PaymentBookScreen(
                    viewModel = viewModel,
                    customers = customers,
                    payments = payments,
                    selectedMonth = selectedMonth,
                    isLargeFont = isLargeFont
                )
                "reports" -> ReportsAndBackupScreen(
                    viewModel = viewModel,
                    customers = customers,
                    deliveries = deliveries,
                    payments = payments,
                    isLargeFont = isLargeFont
                )
                "add_edit_customer" -> AddEditCustomerScreen(
                    viewModel = viewModel,
                    isLargeFont = isLargeFont
                )
            }
        }

        // Custom responsive Bottom Navigation Menu (Large Touch-targets, highly visible)
        AppBottomNavigation(
            activeTab = activeTab,
            onTabSelected = { tab ->
                viewModel.customerSearchQuery.value = ""
                viewModel.filterArea.value = "All"
                viewModel.filterPaymentStatus.value = "All"
                viewModel.activeMenuTab.value = tab
            },
            isLargeFont = isLargeFont
        )
    }
}

@Composable
fun AppHeader(viewModel: PetalDropViewModel, isLargeFont: Boolean) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val isHighContrast by viewModel.isHighContrast.collectAsStateWithLifecycle()
    val isLarge by viewModel.isLargeFont.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF3F9F1),
                        Color.Transparent
                    )
                )
            )
            .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource("app_title"),
                        fontSize = getScaledFontSize(baseSp = 30, isLarge = isLargeFont),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B1C17),
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "ಹೂವಿನ ವಿತರಣೆ • Flower Delivery",
                        fontSize = getScaledFontSize(baseSp = 13, isLarge = isLargeFont),
                        fontWeight = FontWeight.Medium,
                        color = SleekMutedText
                    )
                }

                // Beautiful custom Language Toggle matching the "Sleek Interface" HTML design
                val toggleText = if (language == AppLanguage.ENGLISH) "ಕನ್ನಡ" else "English"
                val toggleBadge = if (language == AppLanguage.ENGLISH) "EN" else "KN"

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(SleekLanguageBtn)
                        .clickable { viewModel.toggleLanguage() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("language_toggle_button"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = toggleText,
                        color = Color(0xFF1B1C17),
                        fontSize = getScaledFontSize(baseSp = 12, isLarge = isLargeFont),
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = toggleBadge,
                            color = Color(0xFF1B1C17),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Accessible Layout features with Sleek Pill Checkbox design
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Large Font toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isLarge) Color(0xFFD7E8CD) else Color.White)
                        .border(1.dp, SleekOutline, RoundedCornerShape(12.dp))
                        .clickable { viewModel.isLargeFont.value = !isLarge }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Checkbox(
                        checked = isLarge,
                        onCheckedChange = { viewModel.isLargeFont.value = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = SleekOutline
                        ),
                        modifier = Modifier
                            .size(20.dp)
                            .testTag("accessibility_large_font")
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource("accessibility_large_fonts"),
                        fontSize = getScaledFontSize(baseSp = 11, isLarge = isLargeFont),
                        color = Color(0xFF1B1C17),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // High Contrast toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isHighContrast) Color(0xFFD7E8CD) else Color.White)
                        .border(1.dp, SleekOutline, RoundedCornerShape(12.dp))
                        .clickable { viewModel.isHighContrast.value = !isHighContrast }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Checkbox(
                        checked = isHighContrast,
                        onCheckedChange = { viewModel.isHighContrast.value = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = SleekOutline
                        ),
                        modifier = Modifier
                            .size(20.dp)
                            .testTag("accessibility_high_contrast")
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource("accessibility_high_contrast"),
                        fontSize = getScaledFontSize(baseSp = 11, isLarge = isLargeFont),
                        color = Color(0xFF1B1C17),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun AppBottomNavigation(
    activeTab: String,
    onTabSelected: (String) -> Unit,
    isLargeFont: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            // Elegant premium border divider on top
            HorizontalDivider(color = SleekOutline, modifier = Modifier.fillMaxWidth())

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val items = listOf(
                    NavigationItem("dashboard", Icons.Default.Dashboard, "dashboard"),
                    NavigationItem("deliveries", Icons.Default.LocalFlorist, "view_deliveries"),
                    NavigationItem("payments", Icons.Default.Payment, "view_payments"),
                    NavigationItem("customers", Icons.Default.People, "customer_management"),
                    NavigationItem("reports", Icons.Default.Assessment, "view_reports")
                )

                items.forEach { item ->
                    val isSelected = activeTab == item.route
                    val contentColor = if (isSelected) Color(0xFF1B1C17) else SleekMutedText.copy(alpha = 0.7f)

                    Column(
                        modifier = Modifier
                            .clickable { onTabSelected(item.route) }
                            .padding(vertical = 4.dp)
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .width(56.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = stringResource(item.labelKey),
                                tint = contentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(item.labelKey),
                            fontSize = getScaledFontSize(baseSp = 10, isLarge = isLargeFont),
                            color = contentColor,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

data class NavigationItem(val route: String, val icon: ImageVector, val labelKey: String)

// ==========================================
// 1. DASHBOARD SCREEN
// ==========================================
@Composable
fun DashboardScreen(
    viewModel: PetalDropViewModel,
    customers: List<Customer>,
    deliveries: List<DeliveryRecord>,
    payments: List<PaymentRecord>,
    isLargeFont: Boolean
) {
    val activeCustomers = customers.filter { it.active }
    val totalCustCount = customers.size

    // Delivered Today counter (Status: DELIVERED)
    val deliveredToday = deliveries.count { it.status == "DELIVERED" }

    // Remaining today
    val finishedIdsToday = deliveries.map { it.customerId }
    val pendingToday = activeCustomers.count { it.id !in finishedIdsToday }

    // Payments collected / pending
    val paidThisMonth = payments.count { it.status == "PAID" }
    
    // Unpaid this month: active customers with PENDING or PARTIAL, or no payment record yet
    val paidCustIds = payments.filter { it.status == "PAID" }.map { it.customerId }
    val pendingThisMonthCount = activeCustomers.count { it.id !in paidCustIds }

    // Collection totals
    val collectionsThisMonth = payments.sumOf { it.amountPaid }
    val pendingDuesTotal = payments.sumOf { it.balanceDue } + activeCustomers.filter { c ->
        payments.none { it.customerId == c.id }
    }.sumOf { it.monthlyAmount }

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.02f)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFlorist,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Namaskara, Flowers Owner!",
                            fontSize = getScaledFontSize(baseSp = 18, isLarge = isLargeFont),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Track your subscriptions and deliveries on the go.",
                            fontSize = getScaledFontSize(baseSp = 12, isLarge = isLargeFont),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                        )
                    }
                }
            }
        }

        // Sleek Hero Progress Card replacing standard delivered counters
        item {
            val skippedToday = deliveries.count { it.status == "SKIPPED" }
            TodayProgressCard(
                delivered = deliveredToday,
                total = activeCustomers.size,
                pending = pendingToday,
                skipped = skippedToday,
                isLargeFont = isLargeFont
            )
        }

        // Beautiful Sleek Interface Metrics Counters
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard(
                        title = stringResource("total_customers"),
                        value = "$totalCustCount",
                        color = MaterialTheme.colorScheme.primary,
                        icon = Icons.Default.Group,
                        isLargeFont = isLargeFont,
                        cardStyle = "sage",
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = stringResource("total_collections_month"),
                        value = "₹${collectionsThisMonth.toInt()}",
                        color = MaterialTheme.colorScheme.primary,
                        icon = Icons.Default.AccountBalanceWallet,
                        isLargeFont = isLargeFont,
                        cardStyle = "pink",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard(
                        title = stringResource("paid_customers_month"),
                        value = "$paidThisMonth",
                        color = Color(0xFF2E7D32),
                        icon = Icons.Default.CheckCircle,
                        isLargeFont = isLargeFont,
                        cardStyle = "white",
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = stringResource("pending_customers_month"),
                        value = "$pendingThisMonthCount",
                        color = Color(0xFFFF5252),
                        icon = Icons.Default.HighlightOff,
                        isLargeFont = isLargeFont,
                        cardStyle = "white",
                        modifier = Modifier.weight(1f)
                    )
                }

                MetricCard(
                    title = stringResource("total_pending_amount"),
                    value = "₹${pendingDuesTotal.toInt()}",
                    color = MaterialTheme.colorScheme.primary,
                    icon = Icons.Default.MonetizationOn,
                    isLargeFont = isLargeFont,
                    cardStyle = "white",
                    fullWidth = true
                )
            }
        }

        // Quick Actions Section (Large Touch Surface targets)
        item {
            Column {
                Text(
                    text = stringResource("quick_actions"),
                    fontSize = getScaledFontSize(baseSp = 16, isLarge = isLargeFont),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B1C17),
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        label = stringResource("add_customer"),
                        icon = Icons.Default.PersonAdd,
                        isLargeFont = isLargeFont,
                        onClick = {
                            viewModel.selectedCustomerForEdit.value = null
                            viewModel.activeMenuTab.value = "add_edit_customer"
                        },
                        modifier = Modifier.weight(1f).testTag("add_customer_button")
                    )

                    QuickActionCard(
                        label = stringResource("deliveries"),
                        icon = Icons.Default.DirectionsRun,
                        isLargeFont = isLargeFont,
                        onClick = { viewModel.activeMenuTab.value = "deliveries" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // One-click WhatsApp dues reminder panel directly inside Dashboard
        item {
            Column {
                Text(
                    text = stringResource("reminder_pills"),
                    fontSize = getScaledFontSize(baseSp = 16, isLarge = isLargeFont),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                val pendingDuesCustomers = activeCustomers.filter { c ->
                    val record = payments.find { it.customerId == c.id }
                    record == null || record.status != "PAID"
                }

                if (pendingDuesCustomers.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text(
                            text = "No pending dues for this month! Great business!",
                            modifier = Modifier.padding(16.dp),
                            fontSize = getScaledFontSize(baseSp = 14, isLarge = isLargeFont),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        pendingDuesCustomers.take(4).forEach { customer ->
                            val payment = payments.find { it.customerId == customer.id }
                            val due = payment?.balanceDue ?: customer.monthlyAmount
                            
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = customer.name,
                                            fontSize = getScaledFontSize(baseSp = 16, isLarge = isLargeFont),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Due: ₹${due.toInt()} | Area: ${customer.area}",
                                            fontSize = getScaledFontSize(baseSp = 13, isLarge = isLargeFont),
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }

                                    // One-click WhatsApp remind Button
                                    Button(
                                        onClick = {
                                            val msg = "Namaskara ${customer.name}. Your flower subscription payment for this month (Balance: ₹${due.toInt()}) is pending. Kindly make the payment. Thank you."
                                            sendWhatsAppMessage(context, customer.phone, msg)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF25D366) // WhatsApp green colour
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Chat,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Remind",
                                            fontSize = getScaledFontSize(baseSp = 12, isLarge = isLargeFont),
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun TodayProgressCard(
    delivered: Int,
    total: Int,
    pending: Int,
    skipped: Int,
    isLargeFont: Boolean
) {
    val pct = if (total > 0) (delivered * 100) / total else 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFF1B1C17))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "TODAY'S PROGRESS",
                        fontSize = getScaledFontSize(baseSp = 10, isLarge = isLargeFont),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF909285),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "$delivered / $total Delivered",
                        fontSize = getScaledFontSize(baseSp = 22, isLarge = isLargeFont),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFD7E8CD))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$pct% DONE",
                        fontSize = getScaledFontSize(baseSp = 9, isLarge = isLargeFont),
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1B1C17)
                    )
                }
            }

            // Custom Progress line track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF44483D))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = (delivered.toFloat() / total.coerceAtLeast(1)).coerceIn(0f, 1f))
                        .clip(CircleShape)
                        .background(Color(0xFFD7E8CD))
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD7E8CD))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$pending Pending",
                        fontSize = getScaledFontSize(baseSp = 12, isLarge = isLargeFont),
                        color = Color(0xFFE3E3DC),
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFB4AB))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$skipped Skipped",
                        fontSize = getScaledFontSize(baseSp = 12, isLarge = isLargeFont),
                        color = Color(0xFFE3E3DC),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    color: Color,
    icon: ImageVector,
    isLargeFont: Boolean,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = false,
    cardStyle: String = "white"
) {
    val bg = when (cardStyle) {
        "sage" -> Color(0xFFD7E8CD)
        "pink" -> Color(0xFFFCE4EC)
        else -> Color.White
    }

    val borderCol = when (cardStyle) {
        "sage" -> Color(0xFFC5D9B8)
        "pink" -> Color(0xFFF8BBD0)
        else -> SleekOutline
    }

    val textColor = Color(0xFF1B1C17)
    val mutedTextColor = SleekMutedText
    val iconTint = if (cardStyle == "white") color else Color(0xFF1B1C17)
    val iconBg = if (cardStyle == "white") color.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.5f)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, borderCol)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (fullWidth) 20.dp else 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title.uppercase(),
                    fontSize = getScaledFontSize(baseSp = 10, isLarge = isLargeFont),
                    color = mutedTextColor,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    fontSize = getScaledFontSize(baseSp = if (fullWidth) 26 else 20, isLarge = isLargeFont),
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(
    label: String,
    icon: ImageVector,
    isLargeFont: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, SleekOutline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SleekLanguageBtn),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF1B1C17),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = label,
                fontSize = getScaledFontSize(baseSp = 13, isLarge = isLargeFont),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF1B1C17)
            )
        }
    }
}

// ==========================================
// 2. CUSTOMER LIST SCREEN
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomerListScreen(
    viewModel: PetalDropViewModel,
    customers: List<Customer>,
    payments: List<PaymentRecord>,
    isLargeFont: Boolean
) {
    val searchQuery by viewModel.customerSearchQuery.collectAsStateWithLifecycle()
    val areaFilter by viewModel.filterArea.collectAsStateWithLifecycle()
    val payFilter by viewModel.filterPaymentStatus.collectAsStateWithLifecycle()
    val activeOnly by viewModel.filterActiveOnly.collectAsStateWithLifecycle()

    val context = LocalContext.current

    // Extract dynamic unique areas recorded across customers for beautiful filter selection
    val areaList = remember(customers) {
        listOf("All") + customers.map { it.area }.distinct().filter { it.isNotEmpty() }
    }

    // Filter logic
    val filteredCustomers = customers.filter { c ->
        val matchesSearch = c.name.contains(searchQuery, ignoreCase = true) || c.phone.contains(searchQuery)
        val matchesArea = areaFilter == "All" || c.area == areaFilter
        val matchesActive = !activeOnly || c.active
        
        // Match payment status
        val matchesPay = when (payFilter) {
            "Paid" -> payments.any { it.customerId == c.id && it.status == "PAID" }
            "Partially Paid" -> payments.any { it.customerId == c.id && it.status == "PARTIALLY_PAID" }
            "Pending" -> payments.none { it.customerId == c.id && (it.status == "PAID" || it.status == "PARTIALLY_PAID") } || payments.any { it.customerId == c.id && it.status == "PENDING" }
            else -> true
        }

        matchesSearch && matchesArea && matchesActive && matchesPay
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Headers and Total Label
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource("customer_management"),
                fontSize = getScaledFontSize(baseSp = 18, isLarge = isLargeFont),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B1C17)
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFD7E8CD))
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "${filteredCustomers.size} Customers",
                    fontSize = getScaledFontSize(baseSp = 11, isLarge = isLargeFont),
                    color = Color(0xFF1B1C17),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Search Input with modern sleek styling and borders
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.customerSearchQuery.value = it },
            placeholder = { Text(stringResource("search_customers")) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SleekMutedText) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag("search_input"),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1B1C17),
                unfocusedBorderColor = SleekOutline,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        // Filters horizontal row
        Text(
            text = "Area / Route Filters",
            fontSize = getScaledFontSize(baseSp = 11, isLarge = isLargeFont),
            color = SleekMutedText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(areaList) { area ->
                val isSelected = areaFilter == area
                val background = if (isSelected) Color(0xFFD7E8CD) else Color.White
                val borderCol = if (isSelected) Color(0xFFC5D9B8) else SleekOutline
                val contentColor = Color(0xFF1B1C17)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(background)
                        .border(1.dp, borderCol, RoundedCornerShape(12.dp))
                        .clickable { viewModel.filterArea.value = area }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (area == "All") stringResource("all_areas") else area,
                        color = contentColor,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Active State Filter Grid Row
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = activeOnly,
                onCheckedChange = { viewModel.filterActiveOnly.value = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF1B1C17),
                    uncheckedColor = SleekOutline
                )
            )
            Text(
                text = stringResource("filter_active"),
                fontSize = getScaledFontSize(baseSp = 13, isLarge = isLargeFont),
                color = Color(0xFF1B1C17),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { viewModel.filterActiveOnly.value = !activeOnly }
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Payment status filter dropdown simulation styled beautifully
            val paymentFilters = listOf("All", "Paid", "Partially Paid", "Pending")
            var payExpanded by remember { mutableStateOf(false) }
            Box {
                Button(
                    onClick = { payExpanded = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF1B1C17)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, SleekOutline),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Pay: $payFilter",
                        fontSize = getScaledFontSize(baseSp = 12, isLarge = isLargeFont),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF1B1C17))
                }
                DropdownMenu(expanded = payExpanded, onDismissRequest = { payExpanded = false }) {
                    paymentFilters.forEach { f ->
                        DropdownMenuItem(
                            text = { Text(f, fontWeight = FontWeight.Medium) },
                            onClick = {
                                viewModel.filterPaymentStatus.value = f
                                payExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Customers Scrollable items list
        if (filteredCustomers.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SentimentDissatisfied,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource("empty_customer_list"),
                        textAlign = TextAlign.Center,
                        fontSize = getScaledFontSize(baseSp = 14, isLarge = isLargeFont),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredCustomers) { customer ->
                    val pay = payments.find { it.customerId == customer.id }
                    val due = pay?.balanceDue ?: customer.monthlyAmount
                    val status = pay?.status ?: "PENDING"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (customer.active) Color.White else Color(0xFFF9FBF8)
                        ),
                        border = BorderStroke(1.dp, SleekOutline),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = customer.name,
                                            fontSize = getScaledFontSize(baseSp = 17, isLarge = isLargeFont),
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (!customer.active) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = stringResource("inactive"),
                                                    fontSize = 8.sp,
                                                    color = Color.DarkGray
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${customer.area} | ${customer.address}",
                                        fontSize = getScaledFontSize(baseSp = 13, isLarge = isLargeFont),
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }

                                // Interactive Quick WhatsApp Direct Key
                                IconButton(
                                    onClick = {
                                        val msg = "Namaskara ${customer.name}. Your flower subscription payment for this month (Balance: ₹${due.toInt()}) is pending. Kindly make the payment. Thank you."
                                        sendWhatsAppMessage(context, customer.phone, msg)
                                    },
                                    modifier = Modifier.background(Color(0xFF25D366).copy(alpha = 0.1f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Message,
                                        contentDescription = "WhatsApp Remind",
                                        tint = Color(0xFF25D366)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                            Spacer(modifier = Modifier.height(8.dp))

                            // Details block
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Flower: ${customer.flowerType}",
                                        fontSize = getScaledFontSize(baseSp = 12, isLarge = isLargeFont),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Sub Fee: ₹${customer.monthlyAmount.toInt()}/month",
                                        fontSize = getScaledFontSize(baseSp = 12, isLarge = isLargeFont),
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    // Edit Trigger Button
                                    IconButton(
                                        onClick = {
                                            viewModel.selectedCustomerForEdit.value = customer
                                            viewModel.activeMenuTab.value = "add_edit_customer"
                                        },
                                        modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Profile",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    // Delete Trigger Button
                                    var showDeleteAlert by remember { mutableStateOf(false) }
                                    IconButton(
                                        onClick = { showDeleteAlert = true },
                                        modifier = Modifier.background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Profile",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    if (showDeleteAlert) {
                                        AlertDialog(
                                            onDismissRequest = { showDeleteAlert = false },
                                            title = { Text(stringResource("delete_customer")) },
                                            text = { Text(stringResource("confirm_delete")) },
                                            confirmButton = {
                                                Button(
                                                    onClick = {
                                                        viewModel.deleteCustomer(customer)
                                                        showDeleteAlert = false
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                                ) {
                                                    Text("Delete")
                                                }
                                            },
                                            dismissButton = {
                                                TextButton(onClick = { showDeleteAlert = false }) {
                                                    Text("Cancel")
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. DAILY DELIVERY SCREEN
// ==========================================
@Composable
fun DailyDeliveryScreen(
    viewModel: PetalDropViewModel,
    customers: List<Customer>,
    deliveries: List<DeliveryRecord>,
    selectedDate: String,
    isLargeFont: Boolean
) {
    val activeCustomers = customers.filter { it.active }

    // Merging data and status for precise daily display
    val deliveriesByCustomer = deliveries.associateBy { it.customerId }

    val deliveredCount = activeCustomers.count { c -> deliveriesByCustomer[c.id]?.status == "DELIVERED" }
    val missedCount = activeCustomers.count { c -> deliveriesByCustomer[c.id]?.status == "NOT_DELIVERED" }
    val skippedCount = activeCustomers.count { c -> deliveriesByCustomer[c.id]?.status == "SKIPPED" }
    val totalRuns = activeCustomers.size

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Calendar date display control beautifully styled matching Sleek theme
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, SleekOutline),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { changeSelectedDate(viewModel, selectedDate, offsetDays = -1) },
                    modifier = Modifier.background(SleekLanguageBtn, CircleShape).size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = "Previous Day",
                        tint = Color(0xFF1B1C17),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource("deliveries").uppercase(Locale.ROOT),
                        fontSize = getScaledFontSize(baseSp = 10, isLarge = isLargeFont),
                        color = SleekMutedText,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = selectedDate,
                        fontSize = getScaledFontSize(baseSp = 18, isLarge = isLargeFont),
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1B1C17)
                    )
                }

                IconButton(
                    onClick = { changeSelectedDate(viewModel, selectedDate, offsetDays = 1) },
                    modifier = Modifier.background(SleekLanguageBtn, CircleShape).size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward, 
                        contentDescription = "Next Day",
                        tint = Color(0xFF1B1C17),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Daily counters display grid
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DailyCounterBadge(label = "Runs", count = totalRuns, color = Color.Gray, modifier = Modifier.weight(1f))
            DailyCounterBadge(label = "Delivered", count = deliveredCount, color = Color(0xFF4CAF50), modifier = Modifier.weight(1f))
            DailyCounterBadge(label = "Missed", count = missedCount, color = Color(0xFFF44336), modifier = Modifier.weight(1f))
            DailyCounterBadge(label = "Skipped", count = skippedCount, color = Color(0xFFFF9800), modifier = Modifier.weight(1f))
        }

        // List of Active Customers Daily Runs
        if (activeCustomers.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No active customers to deliver flowers to. Activate or add customers first.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(activeCustomers) { customer ->
                    val deliveryRecord = deliveriesByCustomer[customer.id]
                    val deliveryStatus = deliveryRecord?.status ?: "PENDING"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when (deliveryStatus) {
                                "DELIVERED" -> Color(0xFFF3F9F1)
                                "NOT_DELIVERED" -> Color(0xFFFFF5F5)
                                "SKIPPED" -> Color(0xFFFFF9F2)
                                else -> Color.White
                            }
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = when (deliveryStatus) {
                                "DELIVERED" -> Color(0xFFC5D9B8)
                                "NOT_DELIVERED" -> Color(0xFFF8BBD0)
                                "SKIPPED" -> Color(0xFFFFCC80)
                                else -> SleekOutline
                            }
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = customer.name,
                                        fontSize = getScaledFontSize(baseSp = 18, isLarge = isLargeFont),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${customer.flowerType} | Route: ${customer.area}",
                                        fontSize = getScaledFontSize(baseSp = 13, isLarge = isLargeFont),
                                        fontWeight = FontWeight.Medium
                                    )
                                    if (customer.deliveryNotes.isNotEmpty()) {
                                        Text(
                                            text = "Notes: ${customer.deliveryNotes}",
                                            fontSize = getScaledFontSize(baseSp = 12, isLarge = isLargeFont),
                                            color = Color.DarkGray,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        )
                                    }
                                }

                                // Status badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            when (deliveryStatus) {
                                                "DELIVERED" -> Color(0xFF2E7D32)
                                                "NOT_DELIVERED" -> Color(0xFFC62828)
                                                "SKIPPED" -> Color(0xFFEF6C00)
                                                else -> Color.Gray
                                            }
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (deliveryStatus == "PENDING") "PENDING" else stringResource(
                                            when (deliveryStatus) {
                                                "DELIVERED" -> "marked_delivered"
                                                "NOT_DELIVERED" -> "marked_not_delivered"
                                                else -> "marked_skipped"
                                            }
                                        ),
                                        color = Color.White,
                                        fontSize = getScaledFontSize(baseSp = 10, isLarge = isLargeFont),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Large Accessible Delivery Actions Touch buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.recordDelivery(customer.id, "DELIVERED") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .testTag("deliver_status_button"),
                                    contentPadding = PaddingValues(vertical = 12.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("Delivered", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }

                                Button(
                                    onClick = { viewModel.recordDelivery(customer.id, "NOT_DELIVERED") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(vertical = 12.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Cancel, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("Missed", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }

                                Button(
                                    onClick = { viewModel.recordDelivery(customer.id, "SKIPPED") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(vertical = 12.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.SkipNext, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("Skip", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailyCounterBadge(label: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (count > 0) color.copy(alpha = 0.08f) else Color.White),
        border = BorderStroke(1.dp, if (count > 0) color.copy(alpha = 0.3f) else SleekOutline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label.uppercase(Locale.ROOT),
                fontSize = 9.sp,
                color = SleekMutedText,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$count",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = if (count > 0) color else Color(0xFF1B1C17)
            )
        }
    }
}

private fun changeSelectedDate(viewModel: PetalDropViewModel, currentDate: String, offsetDays: Int) {
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(currentDate) ?: Date()
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DAY_OF_MONTH, offsetDays)
        viewModel.selectedDate.value = sdf.format(cal.time)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// ==========================================
// 4. PAYMENT BOOK SCREEN
// ==========================================
@Composable
fun PaymentBookScreen(
    viewModel: PetalDropViewModel,
    customers: List<Customer>,
    payments: List<PaymentRecord>,
    selectedMonth: String,
    isLargeFont: Boolean
) {
    val activeCustomers = customers.filter { it.active }

    // Aggregate monthly data helper
    val paidCustCount = payments.count { it.status == "PAID" }
    val unpaidCustCount = activeCustomers.size - paidCustCount
    val totalCollected = payments.sumOf { it.amountPaid }
    val totalOutstanding = payments.sumOf { it.balanceDue } + activeCustomers.filter { c ->
        payments.none { it.customerId == c.id }
    }.sumOf { it.monthlyAmount }

    val context = LocalContext.current

    // Modal dialog trigger parameters for managing dues payment
    var mCustomerForPostPay by remember { mutableStateOf<Customer?>(null) }
    var mAmountInput by remember { mutableStateOf("") }
    var mNotesInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Dropdown Month selector styled beautifully matching Sleek theme
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, SleekOutline),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { changeSelectedMonth(viewModel, selectedMonth, offsetMonths = -1) },
                    modifier = Modifier.background(SleekLanguageBtn, CircleShape).size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = "Previous Month",
                        tint = Color(0xFF1B1C17),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource("month_cycle").uppercase(Locale.ROOT),
                        fontSize = getScaledFontSize(baseSp = 10, isLarge = isLargeFont),
                        color = SleekMutedText,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = selectedMonth,
                        fontSize = getScaledFontSize(baseSp = 18, isLarge = isLargeFont),
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1B1C17)
                    )
                }

                IconButton(
                    onClick = { changeSelectedMonth(viewModel, selectedMonth, offsetMonths = 1) },
                    modifier = Modifier.background(SleekLanguageBtn, CircleShape).size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward, 
                        contentDescription = "Next Month",
                        tint = Color(0xFF1B1C17),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Monthly Payments Summary ledger card grid
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DailyCounterBadge(label = "Paid Cust", count = paidCustCount, color = Color(0xFF009688), modifier = Modifier.weight(1f))
            DailyCounterBadge(label = "Pending Cust", count = unpaidCustCount, color = Color(0xFFF44336), modifier = Modifier.weight(1f))
            DailyCounterBadge(label = "Collected (₹)", count = totalCollected.toInt(), color = Color(0xFF4CAF50), modifier = Modifier.weight(1f))
            DailyCounterBadge(label = "Dues (₹)", count = totalOutstanding.toInt(), color = Color(0xFFE91E63), modifier = Modifier.weight(1f))
        }

        // Ledger Title list header
        Text(
            text = "Customer Balances & Mark Actions",
            fontSize = getScaledFontSize(baseSp = 14, isLarge = isLargeFont),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // List scroll container
        if (activeCustomers.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Database empty. Add customer first.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(activeCustomers) { customer ->
                    val payRecord = payments.find { it.customerId == customer.id }
                    val currentStatus = payRecord?.status ?: "PENDING"
                    val paidAmount = payRecord?.amountPaid ?: 0.0
                    val dueAmount = payRecord?.balanceDue ?: customer.monthlyAmount

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when (currentStatus) {
                                "PAID" -> Color(0xFFF3F9F1)
                                "PARTIALLY_PAID" -> Color(0xFFFFF9F2)
                                else -> Color(0xFFFFF5F5)
                            }
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = when (currentStatus) {
                                "PAID" -> Color(0xFFC5D9B8)
                                "PARTIALLY_PAID" -> Color(0xFFFFD090)
                                else -> Color(0xFFF8BBD0)
                            }
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = customer.name,
                                        fontSize = getScaledFontSize(baseSp = 18, isLarge = isLargeFont),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Sub Rate: ₹${customer.monthlyAmount.toInt()}/month",
                                        fontSize = getScaledFontSize(baseSp = 12, isLarge = isLargeFont),
                                        color = Color.DarkGray
                                    )
                                }

                                // Status text pill badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            when (currentStatus) {
                                                "PAID" -> Color(0xFF00796B)
                                                "PARTIALLY_PAID" -> Color(0xFFFBC02D)
                                                else -> Color(0xFFD32F2F)
                                            }
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = stringResource(
                                            when (currentStatus) {
                                                "PAID" -> "paid"
                                                "PARTIALLY_PAID" -> "partial"
                                                else -> "pending"
                                            }
                                        ),
                                        color = if (currentStatus == "PARTIALLY_PAID") Color.Black else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = Color.Black.copy(alpha = 0.08f))
                            Spacer(modifier = Modifier.height(8.dp))

                            // Payment values row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Paid: ₹${paidAmount.toInt()} | Balance: ₹${dueAmount.toInt()}",
                                        fontSize = getScaledFontSize(baseSp = 14, isLarge = isLargeFont),
                                        fontWeight = FontWeight.Bold,
                                        color = if (dueAmount > 0) Color(0xFFD32F2F) else Color(0xFF00796B)
                                    )
                                    if (payRecord != null && payRecord.notes.isNotEmpty()) {
                                        Text(
                                            text = "Note: ${payRecord.notes}",
                                            fontSize = 11.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }

                                // Large accessible action triggering ledger management Pop-up
                                Button(
                                    onClick = {
                                        mCustomerForPostPay = customer
                                        mAmountInput = if (currentStatus == "PAID") customer.monthlyAmount.toString() else paidAmount.toString()
                                        mNotesInput = payRecord?.notes ?: ""
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("payment_book_button")
                                ) {
                                    Text("Pay Ledger", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Pay Ledger dialog modal
        mCustomerForPostPay?.let { customer ->
            val payDetail = payments.find { it.customerId == customer.id }
            val currentStatus = payDetail?.status ?: "PENDING"

            AlertDialog(
                onDismissRequest = { mCustomerForPostPay = null },
                title = { Text("Record Payment: ${customer.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Fixed Subscription amount: ₹${customer.monthlyAmount.toInt()}",
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = mAmountInput,
                            onValueChange = { mAmountInput = it },
                            label = { Text("Amount Paid (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Visual presets for fast single-tap typing
                        Text("Quick Presets:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { mAmountInput = customer.monthlyAmount.toString() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Full: ₹${customer.monthlyAmount.toInt()}", fontSize = 10.sp, color = Color.White)
                            }
                            Button(
                                onClick = { mAmountInput = (customer.monthlyAmount / 2).toString() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF6C00)),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Half: ₹${(customer.monthlyAmount / 2).toInt()}", fontSize = 10.sp, color = Color.White)
                            }
                        }

                        OutlinedTextField(
                            value = mNotesInput,
                            onValueChange = { mNotesInput = it },
                            label = { Text("Payment Mode / Notes") },
                            placeholder = { Text("Cash, GPay, PhonePe") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Suggestion chips for fast typing
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("GPay", "PhonePe", "Cash", "UPI").forEach { preset ->
                                SuggestionChip(
                                    onClick = { mNotesInput = preset },
                                    label = { Text(preset) }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amt = mAmountInput.toDoubleOrNull() ?: 0.0
                            val limit = customer.monthlyAmount
                            val remaining = limit - amt

                            val finalStatus = when {
                                amt >= limit -> "PAID"
                                amt > 0.0 -> "PARTIALLY_PAID"
                                else -> "PENDING"
                            }

                            viewModel.recordPayment(
                                customerId = customer.id,
                                status = finalStatus,
                                amountPaid = amt,
                                balanceDue = if (remaining < 0.0) 0.0 else remaining,
                                notes = mNotesInput
                            )
                            mCustomerForPostPay = null
                        }
                    ) {
                        Text("Record Pay")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mCustomerForPostPay = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

private fun changeSelectedMonth(viewModel: PetalDropViewModel, currentMonth: String, offsetMonths: Int) {
    try {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val date = sdf.parse(currentMonth) ?: Date()
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, offsetMonths)
        viewModel.selectedMonth.value = sdf.format(cal.time)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// ==========================================
// 5. REPORTS & BACKUPS SCREEN
// ==========================================
@Composable
fun ReportsAndBackupScreen(
    viewModel: PetalDropViewModel,
    customers: List<Customer>,
    deliveries: List<DeliveryRecord>,
    payments: List<PaymentRecord>,
    isLargeFont: Boolean
) {
    var activeSubTab by remember { mutableStateOf("reports") } // reports, backups

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Visual subtab toggle selection bar
        TabRow(selectedTabIndex = if (activeSubTab == "reports") 0 else 1) {
            Tab(selected = activeSubTab == "reports", onClick = { activeSubTab = "reports" }) {
                Text(stringResource("reports"), modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
            Tab(selected = activeSubTab == "backups", onClick = { activeSubTab = "backups" }) {
                Text("Backup Engine", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (activeSubTab == "reports") {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main visual figures cards
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Current Month Summary Stats",
                            fontSize = getScaledFontSize(baseSp = 15, isLarge = isLargeFont),
                            fontWeight = FontWeight.Bold
                        )

                        val totalDel尝试 = deliveries.size
                        val totalSuccessful = deliveries.count { it.status == "DELIVERED" }
                        val totalMissed = deliveries.count { it.status == "NOT_DELIVERED" }
                        val totalSkipped = deliveries.count { it.status == "SKIPPED" }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(stringResource("total_deliveries") + ":", fontWeight = FontWeight.Bold)
                                    Text("$totalDel尝试")
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(stringResource("successful_deliveries") + ":", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                                    Text("$totalSuccessful")
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(stringResource("missed_deliveries") + ":", color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                                    Text("$totalMissed")
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(stringResource("skipped_deliveries") + ":", color = Color(0xFFEF6C00), fontWeight = FontWeight.Bold)
                                    Text("$totalSkipped")
                                }
                            }
                        }
                    }
                }

                // Custom failure-proof canvas-drawn status statistics graph
                item {
                    Column {
                        Text(
                            text = "Collections vs Dues Visual",
                            fontSize = getScaledFontSize(baseSp = 14, isLarge = isLargeFont),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        val sumCollected = payments.sumOf { it.amountPaid }
                        val sumDues = payments.sumOf { it.balanceDue } + customers.filter { it.active && payments.none { p -> p.customerId == it.id } }.sumOf { it.monthlyAmount }

                        CanvasChart(collected = sumCollected, dues = sumDues)
                    }
                }

                // Top paying customer items list display
                item {
                    Column {
                        Text(
                            text = stringResource("top_paying"),
                            fontSize = getScaledFontSize(baseSp = 14, isLarge = isLargeFont),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        val topPaying = customers.sortedByDescending { it.monthlyAmount }.take(4)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                topPaying.forEachIndexed { i, customer ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("${i + 1}. ${customer.name}", fontWeight = FontWeight.Bold)
                                        Text("₹${customer.monthlyAmount.toInt()}/month", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // Action Exports block
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Export Reports & Shares",
                            fontSize = getScaledFontSize(baseSp = 14, isLarge = isLargeFont),
                            fontWeight = FontWeight.Bold
                        )

                        // 1. Customer Directory export (Formatted TXT representation of PDF)
                        Button(
                            onClick = {
                                val txt = viewModel.generateCustomerPdfText(customers)
                                viewModel.shareTextReport("PetalDrop Customer Report", txt)
                                Toast.makeText(context, "Customer report generated!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource("export_pdf_customer"), color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        // 2. Payments ledger export (TXT representation of PDF)
                        Button(
                            onClick = {
                                val txt = viewModel.generatePaymentPdfText(customers, payments)
                                viewModel.shareTextReport("PetalDrop Payment Report", txt)
                                Toast.makeText(context, "Payments report generated!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource("export_pdf_payment"), color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        // 3. Monthly spreadsheet export (.CSV format representing Excel)
                        Button(
                            onClick = {
                                val csv = viewModel.generateMonthlyReportCsv(customers, deliveries, payments)
                                viewModel.shareTextReport("PetalDrop Monthly CSV Export", csv)
                                Toast.makeText(context, "CSV exported!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Icon(Icons.Default.TableChart, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource("export_csv_excel"), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        } else {
            // Backup tab view
            BackupSettingsView(viewModel = viewModel, isLargeFont = isLargeFont)
        }
    }
}

// Custom Zero-dependency failure-proof Chart drawer
@Composable
fun CanvasChart(collected: Double, dues: Double) {
    val total = collected + dues
    val collectedRatio = if (total > 0) (collected / total).toFloat() else 0.5f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, SleekOutline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Drawn Bar geometry
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Collection Rate: ${(collectedRatio * 100).toInt()}%",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1B1C17)
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Custom Draw Row simulation with custom Sleek shapes
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray.copy(alpha = 0.2f))
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(if (collectedRatio > 0.05f) collectedRatio else 0.05f)
                                .background(Color(0xFF88A07A))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(if (1f - collectedRatio > 0.05f) 1f - collectedRatio else 0.05f)
                                .background(Color(0xFFE59CA5))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(Color(0xFF88A07A), CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Collected: ₹${collected.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B1C17))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(Color(0xFFE59CA5), CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Outstanding: ₹${dues.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B1C17))
                    }
                }
            }
        }
    }
}

@Composable
fun BackupSettingsView(viewModel: PetalDropViewModel, isLargeFont: Boolean) {
    val backupCode by viewModel.backupCode.collectAsStateWithLifecycle()
    var inputRestoreCode by remember { mutableStateOf("") }
    
    val context = LocalContext.current

    // Fetch backup string on creation
    LaunchedEffect(Unit) {
        viewModel.refreshBackupCode()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource("backup_header"),
                        fontSize = getScaledFontSize(baseSp = 16, isLarge = isLargeFont),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource("backup_instructions"),
                        fontSize = getScaledFontSize(baseSp = 13, isLarge = isLargeFont),
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Text Field holding backup string ready to copy
                    OutlinedTextField(
                        value = backupCode,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Database Copy Code") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clipData = android.content.ClipData.newPlainText("PetalDrop Database Backup", backupCode)
                            clipboardManager.setPrimaryClip(clipData)
                            Toast.makeText(context, Translations.getString("backup_copied", AppLanguage.ENGLISH), Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource("copy_backup"), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Restore Database Backup",
                        fontSize = getScaledFontSize(baseSp = 16, isLarge = isLargeFont),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inputRestoreCode,
                        onValueChange = { inputRestoreCode = it },
                        label = { Text(stringResource("paste_backup_here")) },
                        placeholder = { Text("Paste database string code...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 8
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (inputRestoreCode.isEmpty()) {
                                Toast.makeText(context, "Enter code to restore", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            viewModel.importBackup(
                                jsonStr = inputRestoreCode,
                                onSuccess = {
                                    Toast.makeText(context, "Data restored successfully!", Toast.LENGTH_SHORT).show()
                                    viewModel.refreshBackupCode()
                                },
                                onError = {
                                    Toast.makeText(context, "Restore failed! Invalid database code format.", Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Backup, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource("restore_backup"), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. ADD / EDIT CUSTOMER SCREEN
// ==========================================
@Composable
fun AddEditCustomerScreen(
    viewModel: PetalDropViewModel,
    isLargeFont: Boolean
) {
    val editTarget = viewModel.selectedCustomerForEdit.value
    val isEditMode = editTarget != null

    // Form variable parameters
    var nameInput by remember { mutableStateOf(editTarget?.name ?: "") }
    var phoneInput by remember { mutableStateOf(editTarget?.phone ?: "") }
    var addressInput by remember { mutableStateOf(editTarget?.address ?: "") }
    var areaInput by remember { mutableStateOf(editTarget?.area ?: "") }
    var flowerInput by remember { mutableStateOf(editTarget?.flowerType ?: "") }
    var monthlyAmountInput by remember { mutableStateOf(editTarget?.monthlyAmount?.toInt()?.toString() ?: "") }
    var notesInput by remember { mutableStateOf(editTarget?.deliveryNotes ?: "") }
    var activeInput by remember { mutableStateOf(editTarget?.active ?: true) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Heading block
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isEditMode) stringResource("edit_customer") else stringResource("add_customer"),
                fontSize = getScaledFontSize(baseSp = 18, isLarge = isLargeFont),
                fontWeight = FontWeight.Bold
            )

            // Cancel trigger button
            IconButton(
                onClick = { viewModel.activeMenuTab.value = "customers" },
                modifier = Modifier.background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), CircleShape)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel")
            }
        }

        // 1. Customer Name Input
        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text(stringResource("cust_name") + " *") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // 2. Phone Input
        OutlinedTextField(
            value = phoneInput,
            onValueChange = { phoneInput = it },
            label = { Text(stringResource("phone_number") + " *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            placeholder = { Text(stringResource("phone_field_desc")) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // 3. Address
        OutlinedTextField(
            value = addressInput,
            onValueChange = { addressInput = it },
            label = { Text(stringResource("address") + " *") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2
        )

        // 4. Area Route Info
        OutlinedTextField(
            value = areaInput,
            onValueChange = { areaInput = it },
            label = { Text(stringResource("area_route") + " *") },
            placeholder = { Text("Indiranagar, Jayanagar") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Area suggestions chips
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("Indiranagar", "Jayanagar", "Koramangala", "Malleshwaram").forEach { area ->
                SuggestionChip(
                    onClick = { areaInput = area },
                    label = { Text(area) }
                )
            }
        }

        // 5. Flower Type Picker choices
        Column {
            OutlinedTextField(
                value = flowerInput,
                onValueChange = { flowerInput = it },
                label = { Text(stringResource("flower_type") + " *") },
                placeholder = { Text("Rose / Jasmime / Mixed") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))
            // Quick zero typing floral presets
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Jasmine / ಮಲ್ಲಿಗೆ", "Rose / ಗುಲಾಬಿ", "Marigold / ಗೆಂಡೆಹೂವು", "Mixed").forEach { flower ->
                    Button(
                        onClick = { flowerInput = flower },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(flower, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 6. Monthlysubscription amount input
        Column {
            OutlinedTextField(
                value = monthlyAmountInput,
                onValueChange = { monthlyAmountInput = it },
                label = { Text(stringResource("monthly_amount") + " *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))
            // Quick presets
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("500", "800", "1000", "1200", "1500").forEach { preset ->
                    Button(
                        onClick = { monthlyAmountInput = preset },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                            contentColor = MaterialTheme.colorScheme.secondary
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("₹$preset", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 7. Delivery Instructions
        OutlinedTextField(
            value = notesInput,
            onValueChange = { notesInput = it },
            label = { Text(stringResource("delivery_notes")) },
            placeholder = { Text("Leave at door, Deliver by 6 AM") },
            maxLines = 2,
            modifier = Modifier.fillMaxWidth()
        )

        // 8. Active Status trigger switch Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Customer Subscription Status",
                    fontWeight = FontWeight.Bold,
                    fontSize = getScaledFontSize(baseSp = 14, isLarge = isLargeFont)
                )
                Text(
                    text = "Inactive customers do not receive daily deliveries.",
                    fontSize = 11.sp,
                    color = Color.DarkGray
                )
            }
            Switch(
                checked = activeInput,
                onCheckedChange = { activeInput = it }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Large high-contrast Confirmation Click trigger
        Button(
            onClick = {
                // Form field validations
                if (nameInput.trim().isEmpty() || phoneInput.trim().isEmpty() || addressInput.trim().isEmpty() || areaInput.trim().isEmpty() || flowerInput.trim().isEmpty() || monthlyAmountInput.trim().isEmpty()) {
                    Toast.makeText(context, "Please fill in all starred (*) fields!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val monthlyAmt = monthlyAmountInput.toDoubleOrNull() ?: 0.0

                val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                if (isEditMode && editTarget != null) {
                    viewModel.updateCustomer(
                        editTarget.copy(
                            name = nameInput.trim(),
                            phone = phoneInput.trim(),
                            address = addressInput.trim(),
                            area = areaInput.trim(),
                            flowerType = flowerInput.trim(),
                            monthlyAmount = monthlyAmt,
                            deliveryNotes = notesInput.trim(),
                            active = activeInput
                        )
                    )
                } else {
                    viewModel.addCustomer(
                        Customer(
                            name = nameInput.trim(),
                            phone = phoneInput.trim(),
                            address = addressInput.trim(),
                            area = areaInput.trim(),
                            flowerType = flowerInput.trim(),
                            monthlyAmount = monthlyAmt,
                            deliveryNotes = notesInput.trim(),
                            startDate = dateStr,
                            active = activeInput
                        )
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("save_customer_button"),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource("save_customer"),
                fontSize = getScaledFontSize(baseSp = 16, isLarge = isLargeFont),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

// Clean launch intent method for WhatsApp shares
private fun sendWhatsAppMessage(context: Context, phone: String, message: String) {
    try {
        val cleanPhone = phone.filter { it.isDigit() }
        val formattedPhone = if (cleanPhone.length == 10) "91$cleanPhone" else cleanPhone
        val uri = "https://api.whatsapp.com/send?phone=$formattedPhone&text=${java.net.URLEncoder.encode(message, "UTF-8")}"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = android.net.Uri.parse(uri)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Could not open WhatsApp. Sharing SMS instead...", Toast.LENGTH_SHORT).show()
        try {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse("sms:${phone}?body=${java.net.URLEncoder.encode(message, "UTF-8")}"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (ex: Exception) {
            Toast.makeText(context, "Failed to send message: ${ex.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

