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
import androidx.compose.ui.window.Dialog
import com.daman.edman.screens.components.HeaderText
import com.daman.edman.screens.components.NormalText
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.CoverPhoto
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppButtonStroke
import com.easyEats.ezeats.ui.compose.components.AppImage
import com.easyEats.ezeats.ui.compose.components.AppImageCornerTop
import com.easyEats.ezeats.ui.compose.components.AppInputField
import com.easyEats.ezeats.ui.compose.menu_item.MenuItemUICheckBox
import com.easyEats.ezeats.ui.compose.menu_item.MenuItemUIRadio
import com.easyEats.ezeats.ui.compose.theme.AppColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@Composable
fun ShopDifferentDialog(
    modifier: Modifier = Modifier,
    start: () -> Unit,
    dismiss: () -> Unit
) {
    Dialog(

        onDismissRequest = {
            dismiss()
        },
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .background(AppColor.WhiteColor, shape = RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                HeaderText(
                    text = stringResource(R.string.start_new_order),
                    fontSize = 20,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Image(
                    painter = painterResource(R.drawable.img_shop_different),
                    modifier = Modifier
                        .size(150.dp)
                        .padding(top = 13.dp),
                    contentDescription = "img_shop_different"
                )
                NormalText(
                    textAlign = TextAlign.Center,
                    fontSize = 14,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = stringResource(R.string.adding_items)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    AppButtonPrimary(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp),
                        text = stringResource(R.string.start), onClick = {
                            start()
                        })
                    AppButtonStroke(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp),
                        text = stringResource(R.string.cancel), onClick = {
                            dismiss()
                        })

                }
            }
        })

}
