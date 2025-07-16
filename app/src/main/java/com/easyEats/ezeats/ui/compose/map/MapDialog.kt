package com.easyEats.ezeats.ui.compose.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.easyEats.ezeats.ui.compose.theme.AppColor


@Composable
fun MapDialog(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel,
    addressId: Int = -1,
    onResult: (Double, Double, String) -> Unit,
    onDismiss: () -> Unit,
) {
    val TAG = "MapDialog"
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )
    var selectedaddressId by remember {
        mutableStateOf(addressId)
    }
    var showLoading by remember {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(600.dp),
        dragHandle = {},
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        contentColor = AppColor.backgroundColor,
        content = {
            MapViewUI(
                modifier = Modifier.fillMaxSize(),
                mapViewModel,
                onResult = onResult,
                dismiss = onDismiss)
        },
        onDismissRequest = {
            onDismiss()
        }
    )
}