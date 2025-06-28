package com.easyEats.ezeats.ui.compose.resturant

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.HeaderText
import com.daman.edman.screens.components.NormalText
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.CoverPhoto
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu
import com.easyEats.ezeats.ui.compose.components.AppImage
import com.easyEats.ezeats.ui.compose.components.AppImageCornerTop
import com.easyEats.ezeats.ui.compose.components.AppInputField
import com.easyEats.ezeats.ui.compose.menu_item.MenuItemUICheckBox
import com.easyEats.ezeats.ui.compose.menu_item.MenuItemUIRadio
import com.easyEats.ezeats.ui.compose.theme.AppColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

val TAG = "MenuItemBottomSheet"

@Composable
fun MenuItemBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    subMenu: SubMenu, // Passing the necessary data
    calculateTotal: (Int,Double,String,HashMap<String, List<ChoiceSelection>>) -> Unit
) {
    var subTotal by remember {
        mutableStateOf(0.0)
    }






    val selected = remember { mutableStateOf(HashMap<String, List<ChoiceSelection>>()) }

    Log.d(TAG, "MenuItemBottomSheet size: ${selected.value.size}")


    var isEnableAdd by remember { mutableStateOf(false) }

    fun checkEnableAdd() : Boolean {

        subMenu.subOptions?.forEach {
            if(it?.type == "radio"){
                if(!selected.value.containsKey(it.id)){
                    return false
                }
//                val selections = selected.value[it.id]
//                // التأكد من أن القائمة تحتوي على عنصر محدد بالـ id الخاص بـ option
//                if (selections.isNullOrEmpty() ) {
//                    return false
//                }
            }
        }
        return true
    }
    LaunchedEffect(Unit) {
        isEnableAdd = checkEnableAdd()
    }
    LaunchedEffect(selected.value) {
        isEnableAdd = checkEnableAdd()
    }



    val sheetState = rememberModalBottomSheetState()
    var noteText by remember {
        mutableStateOf("")
    }
    var count by remember {
        mutableIntStateOf(1)
    }

    Log.d(TAG, "MenuItemBottomSheet: visible")
    Log.d(TAG, "MenuItemBottomSheet: ")
    LaunchedEffect(isVisible) {
        if (!isVisible) {
            onDismiss()
        }
    }

    Log.d(TAG, "MenuItemBottomSheet: $isEnableAdd")

    fun getSelectedRadio(id: String): String {
        return selected.value[id]?.firstOrNull()?.id ?: ""
    }

    fun getSelectedCheckbox(id: String): List<ChoiceSelection> {
        return selected.value[id] ?: emptyList()
    }

    fun getTotal() : Double{
        return count *(subTotal + (subMenu.price ?: 0.0))
    }

    ModalBottomSheet(
        modifier = modifier.then(
            if (!subMenu.subOptions.isNullOrEmpty()) {
                Modifier.fillMaxSize()
            } else {
                Modifier.fillMaxWidth()
            }
        ),
        dragHandle = {},
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        contentColor = AppColor.backgroundColor,
        content = {
            Column( modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    min = 100.dp,
                    max = (LocalConfiguration.current.screenHeightDp.dp * 0.85f)
                )) {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (!subMenu.subOptions.isNullOrEmpty()) {
                            Modifier.weight(1f)
                        } else {
                            Modifier
                        }
                    )) {
                    item {
                        AppImageCornerTop(
                            imageUrl = subMenu.coverPhoto?.url.toString(),
                            cornerRadius = 16,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                        )

                        HeaderText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, start = 16.dp, end = 16.dp),
                            text = subMenu.name ?: "",
                            fontSize = 24,
                            fontWeight = FontWeight.Bold,
                        )

                        NormalText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 3.dp),
                            text = subMenu.description ?: "",
                            fontSize = 16,
                            fontWeight = FontWeight.Normal,
                            color = AppColor.BlackColor2
                        )
                    }
                    subMenu.subOptions?.let { it ->
                        items(it) { item ->
                            if (item?.type == "radio") {
                                MenuItemUIRadio(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    item = item!!,
                                    selectedChoiceId = getSelectedRadio(item.id!!),
//                                    selectedChoiceId = "false",
                                    onClick = { selectedOption ->
//                                        ids = selectedOption.id ?: ""
                                        Log.d(TAG, "listenToObservers: ${selectedOption.id}")
                                        val currentMap = HashMap(selected.value)
                                        val previousSelection = currentMap[item.id!!]?.firstOrNull()
// Subtract previous selection price if exists
                                        if (previousSelection != null) {
                                            subTotal -= previousSelection.price
                                        }
                                        subTotal += selectedOption.price!!

                                        currentMap[item.id!!] = listOf(ChoiceSelection(selectedOption.id!!,selectedOption.price!!))
                                        selected.value = currentMap
                                        Log.d(TAG, "Radio selected: ${selectedOption.id}")
//                                        calculateTotal(count,subTotal,noteText,selected.value)
                                    }
                                )
                            } else if (item?.type == "checkbox") {
                                MenuItemUICheckBox(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    item = item,
                                    selectedChoiceId = getSelectedCheckbox(item.id!!),
                                    onClick = { selectedOption ->
                                        val currentMap = HashMap(selected.value)
                                        val currentList = currentMap[item.id!!]?.toMutableList() ?: mutableListOf()

                                        if (currentList.any { it.id == selectedOption.id }) {
                                            currentList.removeAll { it.id == selectedOption.id }
                                            subTotal -= selectedOption.price!!
                                        } else {
                                            currentList.add(ChoiceSelection(selectedOption.id!!, selectedOption.price!!))
                                            subTotal += selectedOption.price!!
                                        }

                                        currentMap[item.id!!] = currentList
                                        selected.value = currentMap
                                        Log.d(TAG, "Checkbox selected: ${selectedOption.id}")
//                                        calculateTotal(count,subTotal,noteText,selected.value)
//                                    addOrRemove(item.id!!, selectedOption.id!!)
                                    }
                                )
                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_note),
                                modifier = Modifier.size(18.dp),
                                contentDescription = "note"
                            )

                            HeaderText(
                                modifier = Modifier.padding(start = 8.dp),
                                text = stringResource(R.string.any_special_request),
                                fontSize = 14
                            )

                            Spacer(modifier = Modifier.weight(1f))
                            NormalText(
                                text = stringResource(R.string.add_note),
                                fontSize = 12,
                                color = AppColor.BlackColor2
                            )
                        }


                        AppInputField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp)
//                                    .padding(start = 8.dp, end = 8.dp)
                            ,
                            text = noteText,
                            onChange = {
                                noteText = it
                            })
                    }
                }
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            BorderStroke(1.dp, AppColor.GreyColor),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 6.dp),
                        enabled = count > 1,
                        onClick = {
                            if (count > 1) {
                                count--
//                                calculateTotal(count,subTotal,noteText,selected.value)
                            }
                        }) {
                        Image(
                            painter = painterResource(R.drawable.ic_minus),
                            contentDescription = "ic_minus"
                        )
                    }

                    NormalText(
                        text = count.toString(),
                        fontSize = 16,
                        color = AppColor.BlackColor2,
                        textAlign = TextAlign.Center
                    )

                    IconButton(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 6.dp),
                        onClick = {
                            count++
                        }) {
                        Image(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = "ic_plus"
                        )
                    }
                }

                Spacer(modifier = Modifier.width(5.dp))

                TextButton(
                    modifier = Modifier
                        .weight(2f)
                        .background(
                            if (isEnableAdd) AppColor.ColorPrimary else AppColor.GreyColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .height(45.dp),
                    enabled = isEnableAdd,
                    onClick = {
                        if(isEnableAdd)
                            calculateTotal(count,subTotal,noteText,selected.value)

                    }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        HeaderText(
                            text = stringResource(
                                R.string.add_item
                            ),
                            fontWeight = FontWeight.Normal,
                            fontSize = 16,
                            color = AppColor.WhiteColor,

                            )

                        Spacer(modifier = Modifier.weight(1f))

                        HeaderText(
                            text = "${getTotal()} " + stringResource(R.string.currency),
                            fontSize = 16,
                            color = AppColor.WhiteColor,
                            fontWeight = FontWeight.Normal
                        )

                    }
                }

            }

            Spacer(modifier = Modifier.height(16.dp))
        },
        onDismissRequest = {
            onDismiss()
        }
    )
}
@Preview(showSystemUi = true)
@Composable
fun MenuItemBottomSheetPreview() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .border(
                    BorderStroke(1.dp, AppColor.GreyColor),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 6.dp),
                onClick = {

            }) {
                Image(
                    painter = painterResource(R.drawable.ic_minus),
                    contentDescription = "ic_minus"
                )
            }

            NormalText(
                text = "1",
                fontSize = 16,
                color = AppColor.BlackColor2,
                textAlign = TextAlign.Center
            )

            IconButton(
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 6.dp),
                onClick = {

                }) {
                Image(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = "ic_plus"
                )
            }
        }

        Spacer(modifier = Modifier.width(5.dp))

        TextButton(
            modifier = Modifier
                .weight(2f)
                .background(AppColor.ColorPrimary, shape = RoundedCornerShape(16.dp))
                .height(45.dp),
            onClick = {

            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                HeaderText(
                    text = stringResource(
                        R.string.add_item
                    ),
                    fontWeight = FontWeight.Normal,
                    fontSize = 16,
                    color = AppColor.WhiteColor,

                )

                Spacer(modifier = Modifier.weight(1f))

                HeaderText(
                    text = "123 " + stringResource(R.string.currency),
                    fontSize = 16,
                    color = AppColor.WhiteColor,
                    fontWeight = FontWeight.Normal
                )

            }
        }

    }
}
