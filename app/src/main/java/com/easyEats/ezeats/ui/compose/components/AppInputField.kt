package com.easyEats.ezeats.ui.compose.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.easyEats.ezeats.ui.compose.theme.AppColor

@Composable
fun AppInputField(modifier: Modifier = Modifier,text: String,hint: String = "",onChange: (String) -> Unit){

    OutlinedTextField(
        value = text,
        onValueChange = { onChange(it) },
        placeholder = { Text(text = hint, color = AppColor.GreyColor) },
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp), // Adjust height as needed
        shape = RoundedCornerShape(12.dp), // Rounded corners
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = AppColor.BlackColor2,
        )
    )
}