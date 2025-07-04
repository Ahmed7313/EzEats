package com.easyEats.ezeats.ui.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.easyEats.ezeats.ui.compose.theme.AppColor

@Composable
fun AppInputField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "",
    onChange: (String) -> Unit
) {

//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(54.dp) // Match TextField height
//            .border(
//                width = 1.dp,
//                color = AppColor.BlackColor2, // Set the border color
//                shape = RoundedCornerShape(12.dp)
//            )
//            .padding(1.dp) // Padding inside the border
//    ) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .background(AppColor.WhiteColor, shape = RoundedCornerShape(30.dp))
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { onChange(it) },
            placeholder = { Text(text = hint, color = AppColor.GreyColor) },
            modifier = Modifier
                .fillMaxSize(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = AppColor.BlackColor,
                unfocusedTextColor = AppColor.GreyColor,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent, // No underline when focused
                unfocusedBorderColor = Color.Transparent // No underline when unfocused
            )
        )
    }

//    }


}