package com.easyEats.ezeats.ui.compose.resturant

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
        if (isVisible) {
            Log.d(TAG, "MenuItemBottomSheet: show")
            sheetState.show()
        } else {
            Log.d(TAG, "MenuItemBottomSheet: hide")
            sheetState.hide()
        }
    }

    ModalBottomSheet(
        modifier = modifier.fillMaxWidth(),
        dragHandle = {},
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        contentColor = AppColor.backgroundColor,
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {

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
                                if(count > 1) {
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
                            .background(AppColor.ColorPrimary, shape = RoundedCornerShape(16.dp)).height(45.dp),
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
            }
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
