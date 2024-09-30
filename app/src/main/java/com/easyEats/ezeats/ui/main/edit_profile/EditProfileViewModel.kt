package com.easyEats.ezeats.ui.main.edit_profile

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.edit_user_info.UserInfoRequest
import com.easyEats.ezeats.domain.main.model.response.user_info.UserInfoResponse
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    val mainUseCase: MainUseCase,
    savedStateHandle: SavedStateHandle
) :
    MainViewModel(mainUseCase) {
    val user: UserInfoResponse? = savedStateHandle["userInfo"]

    var fullName = user?.name
    var email = user?.email
    var mobileNumber = user?.mobileNumber
    var birthDate = user?.birthDate

    private val _imageBase64 = MutableLiveData<String>()
    val imageBase64: LiveData<String> = _imageBase64

    private val _editUserResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<Any>>>()
    val editUserResultLiveData: LiveData<Resource<BaseResponse<Any>>> get() = _editUserResultMutableLiveData


    fun handleImageResult(uri: Uri?, contentResolver: ContentResolver) {
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
            val base64Image = convertToBase64(bitmap)
            _imageBase64.value = base64Image
        }
    }

    private fun convertToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    fun editUserInfoRestaurantResult() {
        val flow = createUserInfoResultRequest(
            UserInfoRequest(
                name = fullName,
                email = email,
                mobileNumber = mobileNumber,
                profilePicture = imageBase64.value,
                birthDate = birthDate
            )
        )
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _editUserResultMutableLiveData.value =
                Resource.Error("No Internet Connection")
        }, onSuccess = { resource ->
            _editUserResultMutableLiveData.value = resource
        })
    }


    private fun createUserInfoResultRequest(userInfoRequest: UserInfoRequest): Flow<Resource<BaseResponse<Any>>> =
        channelFlow {
            send(Resource.Loading())
            val result = mainUseCase.userInfoUseCase.editUserInfo(userInfoRequest)
            result.handleResponse(onSuccess = {
                send(Resource.Success(result))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }

}