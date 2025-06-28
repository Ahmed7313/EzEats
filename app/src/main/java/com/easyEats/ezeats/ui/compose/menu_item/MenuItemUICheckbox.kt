package com.easyEats.ezeats.ui.compose.menu_item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.HeaderText
import com.daman.edman.screens.components.NormalText
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.Choice
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubOption
import com.easyEats.ezeats.ui.compose.resturant.ChoiceSelection
import com.easyEats.ezeats.ui.compose.theme.AppColor

@Composable
fun MenuItemUICheckBox(
    modifier: Modifier = Modifier,
    item: SubOption,
    selectedChoiceId: List<ChoiceSelection>?,
    onClick: (Choice) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 17.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderText(
            text = item.name ?: "Unknown",
            color = AppColor.BlackColor,
            fontSize = 13,
            fontWeight = FontWeight.Bold,
        )

        MenuItemTag(
            modifier = Modifier,
            menuItemTag = item.required ?: false
        )
    }
    val size = item.choices?.size ?: 0

    item.choices?.forEachIndexed { index, it ->
        if(it?.isHidden == false) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                NormalText(
                    text = it?.name ?: "Unknown",
                    color = AppColor.BlackColor,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13
                )
                Spacer(modifier = Modifier.weight(1f))
                if (it?.price?.toInt() != 0) {
                    NormalText(
                        text = ("(+" + stringResource(R.string.currency) + " " + it?.price + ")"),
                        modifier = Modifier.padding(end = 7.dp),
                        color = AppColor.BlackColor,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        fontSize = 13
                    )
                }

                Checkbox(
                    checked = selectedChoiceId?.any { selection -> selection.id == it.id } == true,
                    onCheckedChange = { selected ->
                        onClick(it!!)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AppColor.ColorPrimary,
                        uncheckedColor = AppColor.GrayColor
                    )
                )

            }
            if (index < size - 1) {
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