package com.easyEats.ezeats.ui.compose.address

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.AppTextHeader
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.request.edit_user_info.UserInfoRequest
import com.easyEats.ezeats.domain.main.model.response.user_info.UserInfoResponse
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppInputTextLabelTop
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.main.edit_profile.EditProfileViewModel
import com.easyEats.ezeats.ui.main.restaurant.CartViewModel
import com.easyEats.ezeats.utils.Resource
import kotlinx.coroutines.launch


@Composable
fun UpdateProfileDialog(
    modifier: Modifier = Modifier,
    editProfileViewModel: EditProfileViewModel,
    viewModel: CartViewModel,
    userInfoResult: UserInfoResponse,
    onDismiss: () -> Unit,
    onUpdate: () -> Unit,
) {
    val TAG = "UmbrellaDialog"
    val sheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    var email by remember {
        mutableStateOf(userInfoResult.email?.ifBlank { "" } ?: "")
    }

    var name by remember {
        mutableStateOf(userInfoResult.name?.ifBlank { "" } ?: "")
    }


    ModalBottomSheet(
        modifier = modifier
            .fillMaxWidth(),
        dragHandle = {},
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        contentColor = AppColor.backgroundColor,
        content = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 28.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                    ) {
                        AppTextHeader(
                            text = stringResource(R.string.update_your_profile),
                            color = AppColor.BlackColor2,
                            fontSize = 20,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if(userInfoResult.name.isNullOrEmpty() == true)
                    AppInputTextLabelTop(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 25.dp),
                        value = name,
                        label = stringResource(R.string.full_name),
                        hint = "${stringResource(R.string.enter)} ${stringResource(R.string.full_name)}"
                    ) {
                        name = it
                    }


                    if(userInfoResult.email.isNullOrEmpty() == true)
                    AppInputTextLabelTop(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 25.dp),
                        value = email,
                        label = stringResource(R.string.email),
                        hint = "${stringResource(R.string.enter)} ${stringResource(R.string.email)}"
                    ) {
                        email = it
                    }



                    AppButtonPrimary(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        text = stringResource(R.string.confirm)
                    ) {
                        if (name.trim().isEmpty()) {
                            Toast.makeText(
                                context,
                                "${context.getText(R.string.enter)} ${context.getText(R.string.full_name)}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@AppButtonPrimary
                        }
                        if (email.trim().isEmpty()) {
                            Toast.makeText(
                                context,
                                "${context.getText(R.string.enter)} ${context.getText(R.string.email)}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@AppButtonPrimary
                        }
                        scope.launch {
                            editProfileViewModel.createUserInfoResultRequest(UserInfoRequest(
                                name = name,
                                email = email
                            )).collect {
                                if(it is Resource.Success){
                                    viewModel._showLoading.value = false
                                    onUpdate()
                                }else if(it is Resource.Loading){
                                    viewModel._showLoading.value = true
                                }else{
                                    viewModel._showLoading.value = false
                                }
                            }
                        }
                    }
                }
//                LoadingView(addressViewModel.showLoading.collectAsState().value)
            }
        },
        onDismissRequest = {
            onDismiss()
        }
    )
}
