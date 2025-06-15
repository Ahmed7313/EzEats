package com.easyEats.ezeats.ui.compose.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.compose.components.AppInputField
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2
import com.easyEats.ezeats.ui.compose.theme.AppColor.GreyColor

@Composable
fun CartSpecialComment(modifier: Modifier = Modifier) {
    var comment by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.special_comments),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BlackColor2
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check_comment),
                contentDescription = "check_comments"
            )

            Column(modifier = Modifier.padding(4.dp)) {
                Text(
                    text = stringResource(R.string.got_any_extra_comments),
                    color = BlackColor2,
                    fontSize = 14.sp,
                )
                Text(
                    text = stringResource(R.string.type_them_we_there_help_you),
                    color = GreyColor,
                    fontSize = 14.sp,
                )

            }
        }

        Text(
            text = stringResource(R.string.please_make_sure_the_pretezel)
        )

        AppInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            text = comment,
            hint = stringResource(R.string.enter_your_comment)
        ) { }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = BlackColor2.copy(
                alpha = 0.5f
            )
        )
    }


}