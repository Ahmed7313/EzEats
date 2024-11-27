package com.easyEats.ezeats.ui.main.edit_profile

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
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel for handling the logic behind editing a user's profile information, such as name, email, mobile number,
 * birthdate, and profile picture. This ViewModel interacts with the MainUseCase to perform business logic and
 * update the user profile.
 *
 * It manages the data, including image handling, view states, and results of the profile editing process.
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    val mainUseCase: MainUseCase,
    savedStateHandle: SavedStateHandle
) : MainViewModel(mainUseCase) {

    // User's current information passed from the savedStateHandle (e.g., from navigation arguments)
    val user: UserInfoResponse? = savedStateHandle["userInfo"]

    // Fields to store user details, with default values taken from the passed user info
    var fullName = user?.name
    var email = user?.email
    var mobileNumber = user?.mobileNumber
    var birthDate = reformatDate(user?.birthDate?.iso)

    // LiveData to control whether the profile views are editable
    val isViewsEditable = MutableLiveData(false)

    // LiveData to store the Base64 encoded profile image string
    private val _imageBase64 = MutableLiveData<String>()
    val imageBase64: LiveData<String> = _imageBase64

    // LiveData to observe the result of the profile update operation
    private val _editUserResultMutableLiveData = MutableLiveData<Resource<BaseResponse<Any>>>()
    val editUserResultLiveData: LiveData<Resource<BaseResponse<Any>>> get() = _editUserResultMutableLiveData

    /**
     * Sets the Base64 encoded image string to be used for updating the profile picture.
     */
    fun setImageBase64(base64Image: String) {
        _imageBase64.value = base64Image
    }

    /**
     * Marks the views as editable, enabling the user to modify their profile information.
     */
    fun makeViewsEditable() {
        isViewsEditable.value = true
    }

    /**
     * Initiates the request to update the user's profile information.
     * The data to be updated includes full name, email, mobile number, birthdate, and profile picture (Base64 string).
     * Observes the result and updates the LiveData with the success or error status.
     */
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
        // Applies the flow and handles success and error cases
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _editUserResultMutableLiveData.value = Resource.Error("No Internet Connection")
        }, onSuccess = { resource ->
            _editUserResultMutableLiveData.value = resource
        })
    }

    /**
     * Creates a flow that handles the profile update request by interacting with the `userInfoUseCase`.
     * Sends appropriate resource states (loading, success, error, unauthenticated).
     */
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

    /**
     * Reformats the user's birthdate from the ISO format to a more user-friendly format (dd/MM/yyyy).
     *
     * @param dateString The date string in ISO format
     * @return The reformatted date string or an empty string if the input is null
     */
    private fun reformatDate(dateString: String?): String? {
        return if (dateString != null) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date)
        } else ""
    }
}

