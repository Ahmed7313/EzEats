package com.easyEats.ezeats.data.remote.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.utils.Constants
import com.easyEats.ezeats.utils.Constants.FIRST_NAME
import com.easyEats.ezeats.utils.Constants.LAST_NAME
import com.easyEats.ezeats.utils.Constants.MOBILE_NUMBER
import com.easyEats.ezeats.utils.Constants.NAME
import com.easyEats.ezeats.utils.Resource
import com.parse.ParseInstallation
import com.parse.ParseUser
import java.util.UUID
import javax.inject.Inject
import kotlin.math.log

/**
 * Handles user registration with the backend using Parse.
 */
class AuthWithParsing @Inject constructor() {

    /**
     * Registers a new user with the provided phone number and name.
     *
     * @param phoneNumber The phone number of the user.
     * @param name The full name of the user.
     * @return A [LiveData] object containing the [Resource] of [ParseUser]. The [Resource] could be
     * [Resource.Loading], [Resource.Success], or [Resource.Error] based on the registration result.
     */
    fun register(phoneNumber: String, name: String): LiveData<Resource<ParseUser>> {
        ParseUser.logOut()  // Log out any existing user
        val result = MutableLiveData<Resource<ParseUser>>().apply {
            value = Resource.Loading()
        }
        val uuid = "+2$phoneNumber"
        val user = createUser(name, "+2$phoneNumber", uuid)

        // Check if the user already exists
        val query = ParseUser.getQuery()
        query.whereEqualTo("username", "+2$phoneNumber")
        query.findInBackground { users, e ->
            if (e == null && users.isEmpty()) {
                signUp(user, result, uuid)
            } else {
                result.postValue(Resource.Error("User already exists or error: ${e?.localizedMessage}"))
            }
        }

        return result
    }


    /**
     * Signs up the user in the background and handles installation saving.
     *
     * @param user The [ParseUser] object to be signed up.
     * @param result A [MutableLiveData] object where the result of the sign-up operation will be posted.
     * @param uuid The UUID to associate with the installation.
     */
    private fun signUp(user: ParseUser, result: MutableLiveData<Resource<ParseUser>>, uuid: String) {
        user.signUpInBackground { e ->
            if (e == null) {
                // Sign up successful
                result.postValue(Resource.Success(user))
            } else {
                // Handle sign up failure
                result.postValue(Resource.Error(e.localizedMessage ?: "Sign up failed"))
            }
        }
    }


    /**
     * Logs in a user using the provided phone number.
     *
     * @param phoneNumber The phone number of the user.
     * @return A [LiveData] object containing the [Resource] of [ParseUser]. The [Resource] could be
     * [Resource.Loading], [Resource.Success], or [Resource.Error] based on the login result.
     */
    fun login(phoneNumber: String): LiveData<Resource<ParseUser>> {
        val result = MutableLiveData<Resource<ParseUser>>().apply {
            value = Resource.Loading()
        }
        ParseUser.logInInBackground(phoneNumber, "123456") { user, e ->
            if (e == null && user != null) {
                // Successfully logged in
                updateInstallation(user, result)
                Log.d(TAG, "login: Success")
            } else {
                // Login failed
                Log.d(TAG, "login: Failed")
                result.postValue(Resource.Error(e?.localizedMessage ?: "Login failed."))
            }
        }
        return result
    }

    /**
     * Updates the current installation data including the user and UUID.
     *
     * @param user The [ParseUser] object associated with the installation.
     * @param result A [MutableLiveData] object where the result of the installation update operation will be posted.
     */
    private fun updateInstallation(user: ParseUser, result: MutableLiveData<Resource<ParseUser>>) {
        val installation = ParseInstallation.getCurrentInstallation().apply {
            put("user", user)
            put(Constants.UUID, user.objectId) // You might want to use user.objectId or a unique identifier here
        }
        installation.saveInBackground { ex ->
            if (ex == null) {
                result.postValue(Resource.Success(user))
                Log.d(TAG, "updateInstallation:ex == null ")
            } else {
                result.postValue(Resource.Error(ex.localizedMessage ?: "Failed to update installation."))
            }
        }
    }

    /**
     * Saves the current installation data including the user and UUID.
     *
     * @param user The [ParseUser] object associated with the installation.
     * @param uuid The UUID to be stored in the installation.
     * @param result A [MutableLiveData] object where the result of the installation save operation will be posted.
     */
    private fun saveInstallation(user: ParseUser, uuid: String, result: MutableLiveData<Resource<ParseUser>>) {
        val installation = ParseInstallation.getCurrentInstallation().apply {
            put("user", user)
            put(Constants.UUID, uuid)
        }
        installation.saveInBackground { ex ->
            if (ex == null) {
                Log.d(TAG, "saveInstallation: success")
                Log.d(TAG, "saveInstallation: success${user.sessionToken}")
                Log.d(TAG, "saveInstallation: success${uuid}")
                result.postValue(Resource.Success(user))
            } else {
                Log.d(TAG, "saveInstallation:${ex.localizedMessage } ")
                result.postValue(Resource.Error(ex.localizedMessage ?: "Failed to save installation."))
            }
        }
    }

    /**
     * Creates a [ParseUser] object with the provided name, phone number, and UUID.
     *
     * @param name The full name of the user.
     * @param phoneNumber The phone number of the user.
     * @param uuid The UUID to be set as the username for the user.
     * @return A [ParseUser] object with the specified properties set.
     */
    private fun createUser(name: String, phoneNumber: String, uuid: String): ParseUser {
        val (firstName, lastName) = splitName(name)
        return ParseUser().apply {
            username = phoneNumber
            Log.d(TAG, "createUser: UUID $username")
            put(MOBILE_NUMBER, phoneNumber)
            put(NAME, name)
            put(FIRST_NAME, firstName)
            put(LAST_NAME, lastName ?: "")
            setPassword("123456")
        }
    }

    /**
     * Splits the given full name into first and last names.
     *
     * @param fullName The full name to split.
     * @return A [Pair] containing the first name and last name. Last name is null if only one name is provided.
     */
    private fun splitName(fullName: String): Pair<String, String?> {
        val parts = fullName.trim().split(" ", limit = 2)
        val firstName = parts.getOrElse(0) { "" }
        val lastName = if (parts.size > 1) parts[1] else null
        return Pair(firstName, lastName)
    }
}
