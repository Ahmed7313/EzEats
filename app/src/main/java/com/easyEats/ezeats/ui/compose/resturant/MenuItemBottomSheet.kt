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

val TAG = "MenuItemBottomSheet"

@Composable
fun MenuItemBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    subMenu: SubMenu, // Passing the necessary data
    onClick: (Int,String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var noteText by remember {
        mutableStateOf("")
    }
    var count by remember {
        mutableStateOf(1)
    }
    Log.d(TAG, "MenuItemBottomSheet: visible")
    Log.d(TAG, "MenuItemBottomSheet: ")
    LaunchedEffect(isVisible) {
        if (!isVisible) {
            onDismiss()
        }
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
                .heightIn(min = 100.dp, max = (LocalConfiguration.current.screenHeightDp.dp * 0.85f))) {
                LazyColumn(modifier = Modifier.fillMaxWidth().then(
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
                            modifier = Modifier.fillMaxWidth().height(280.dp)
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
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                    item = item!!,
//                                selectedChoiceId = selectedState[item.id!!],
                                    selectedChoiceId = "false",
                                    onClick = { selectedOption ->
                                        Log.d(TAG, "listenToObservers: ${selectedOption.id}")
//                                    viewModel.selectChoice(item.id!!, selectedOption.id!!)
                                    }
                                )
                            } else if (item?.type == "checkbox") {
                                MenuItemUICheckBox(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                    item = item,
//                                selectedChoiceId = selectedState[item.id!!],
                                    selectedChoiceId = "false",
                                    onClick = { selectedOption ->
//                                    viewModel.selectChoice(item.id!!, selectedOption.id!!)
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
                        modifier = Modifier.size(24.dp).padding(end = 6.dp),
                        onClick = {
                            if (count > 1) {
                                count--
                            }
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
                        modifier = Modifier.size(24.dp).padding(end = 6.dp),
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
                        .background(
                            AppColor.ColorPrimary,
                            shape = RoundedCornerShape(16.dp)
                        ).height(45.dp),
                    onClick = {
                        count++
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
                            text = "${subMenu.price} " + stringResource(R.string.currency),
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
                modifier = Modifier.size(24.dp).padding(end = 6.dp),
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
                modifier = Modifier.size(24.dp).padding(end = 6.dp),
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
                .background(AppColor.ColorPrimary, shape = RoundedCornerShape(16.dp)).height(45.dp),
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
