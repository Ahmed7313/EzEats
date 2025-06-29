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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
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
import com.easyEats.ezeats.domain.main.model.response.compound.CompoundResult
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


@Composable
fun CompoundBottomSheet(
    modifier: Modifier = Modifier,
    list: List<CompoundResult>,
    selected: String,
    click: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val TAG = "CompoundBottomSheet"
    val sheetState = rememberModalBottomSheetState()
    var selectedObjectId by remember {
        mutableStateOf(selected)
    }

    ModalBottomSheet(
        modifier = modifier.fillMaxWidth().height(412.dp),
        dragHandle = {},
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        contentColor = AppColor.backgroundColor,
        content = {
           Box{
               Column(
                   modifier = Modifier
                       .fillMaxWidth().padding(bottom = 40.dp)
               ) {
                   LazyColumn(
                       modifier = Modifier
                           .fillMaxWidth()
                   ) {
                       item {
                           HeaderText(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .padding(top = 20.dp, start = 16.dp, end = 16.dp),
                               textAlign = TextAlign.Center,
                               text = stringResource(R.string.welcome_to_sahel),
                               fontSize = 24,
                               fontWeight = FontWeight.Bold,
                           )

                           NormalText(
                               textAlign = TextAlign.Center,
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .padding(start = 16.dp, end = 16.dp, top = 3.dp),
                               text = stringResource(R.string.pick_compound_from_list),
                               fontSize = 16,
                               fontWeight = FontWeight.Normal,
                               color = AppColor.BlackColor2
                           )
                       }

                       list.let { it ->
                           itemsIndexed(it) { index,item ->

                               Row(
                                   modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                   verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                   NormalText(
                                       text = item.name.toString(),
                                       modifier = Modifier.padding(end = 7.dp),
                                       color = AppColor.BlackColor,
                                       fontWeight = FontWeight.Normal,
                                       textAlign = TextAlign.Center,
                                       fontSize = 13
                                   )

                                   RadioButton(
                                       selected = item.objectId == selectedObjectId,
                                       onClick = {
                                           selectedObjectId = item.objectId.toString()
                                       },
                                       colors = androidx.compose.material3.RadioButtonDefaults.colors(
                                           selectedColor = AppColor.ColorPrimary,
                                           unselectedColor = AppColor.GrayColor
                                       )
                                   )

                               }

                               if (index < it.size - 1) {
                                   HorizontalDivider(
                                       modifier = Modifier
                                           .fillMaxWidth()
                                           .padding(horizontal = 16.dp, vertical = 8.dp),
                                       color = AppColor.GrayColor,
                                       thickness = 1.dp
                                   )
                               }

                           }
                       }
                   }
               }
               TextButton(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(20.dp)
                       .align(Alignment.BottomCenter)
                       .background(
                           if (selectedObjectId.isNotEmpty()) AppColor.ColorPrimary else AppColor.GreyColor,
                           shape = RoundedCornerShape(16.dp)
                       )
                       .height(45.dp),
                   enabled = selectedObjectId.isNotEmpty(),
                   onClick = {
                       click(selectedObjectId)
                   }) {
                   HeaderText(
                       text = stringResource(R.string.select_compound),
                       fontSize = 16,
                       color = AppColor.WhiteColor,
                       fontWeight = FontWeight.Normal
                   )
               }
           }

            Spacer(modifier = Modifier.height(16.dp))
        },
        onDismissRequest = {
            onDismiss()
        }
    )
}