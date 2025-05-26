package com.daman.edman.screens.components

import android.os.Build
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.large
import java.util.*

@Composable
fun DatePickerView(
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }

    // Get today's date
    val today = Calendar.getInstance()

    // Show the date picker dialog when the view is clicked
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { year, month, day ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(year, month, day)
                }
                val formattedDate = "${day}/${month + 1}/${year}"
                selectedDate = formattedDate
                onDateSelected(formattedDate)
                showDatePicker = false
            },
            minDate = today.timeInMillis
        )
    }

    Surface(
        modifier = modifier.fillMaxWidth().clickable {
            showDatePicker = true
        },
        shape = RoundedCornerShape(large),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(modifier.fillMaxWidth().padding(16.dp)) {
            HeaderText(
                text = "اختر التاريخ",
                color = Color.Gray,
                fontSize = 12
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NormalText(text = if (selectedDate.isEmpty()) "DD/MM/YYYY" else selectedDate)
                Icon(
                    painter = painterResource(id = R.drawable.calendar_icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun DatePickerDialog(
    onDateSelected: (Int, Int, Int) -> Unit,
    minDate: Long
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            onDateSelected(year, month, dayOfMonth)
        },
        currentYear,
        currentMonth,
        currentDay
    )

    // Set minimum date to today
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        datePickerDialog.datePicker.minDate = minDate
    }

    datePickerDialog.show()
}

@Preview(showBackground = true)
@Composable
fun DatePickerViewPreview() {
    DatePickerView(
        onDateSelected = { date -> println("Selected date: $date") }
    )
}
