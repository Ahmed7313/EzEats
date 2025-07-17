package com.easyEats.ezeats.ui.main.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.ActivityHomeBinding
import com.easyEats.ezeats.utils.app_updates.InAppUpdateManager
import com.easyEats.ezeats.utils.language.LanguagesHelper
import com.easyEats.ezeats.utils.language.MyContextWrapper
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    // View binding for activity_home.xml
    lateinit var binding: ActivityHomeBinding

    // NavController for managing fragment navigation
    private lateinit var navController: NavController

    // ViewModel for the activity, provided by Hilt
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var inAppUpdateManager: InAppUpdateManager
    private val REQUEST_CODE_UPDATE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        LanguagesHelper.changeLanguage(this,LanguagesHelper.getCurrentLanguage(this))
        LanguagesHelper.changeLanguage(this,LanguagesHelper.getCurrentLanguage(this))
        super.onCreate(savedInstanceState)
        // Initialize the view binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        // Set the root view from the binding as the content view
        setContentView(binding.root)
        // Set the ViewModel for data binding and provide the lifecycle owner
        binding.mainViewModel = mainViewModel

        binding.lifecycleOwner = this // Ensure LiveData updates the UI automatically
        // Set up the Bottom Navigation and NavHostFragment
        binding.navView.itemIconTintList = null
        setUpBottomNavigationWithNavHostFragment()
        handleIntent(intent)
        inAppUpdateManager = InAppUpdateManager(this, REQUEST_CODE_UPDATE)
        inAppUpdateManager.checkForUpdate()

        lifecycleScope.launch {
            mainViewModel.userLocationAddressMutableLiveData.collect {
                Log.d(TAG, "onCreate: ${it?.address}")
                Log.d(TAG, "onCreate: ${it?.lat}")
                Log.d(TAG, "onCreate: ${it?.lng}")
            }
        }
//        val notificationType = intent.getStringExtra("notificationType")
//        if (notificationType != null) {
//            navigateToScreen( notificationType)
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
//            pushNotification()
//        }

    }
//    private fun navigateToScreen(notification: String?) {
//        when (notification) {
//            "OfferScreen" -> {
//
//            }
//            else -> {
//                Log.d("Navigation Kenawy", "Unknown screen: $notification")
//            }
//        }
//    }

    private fun pushNotification() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(TAG, "pushNotification: $token")
            } else {
                Log.e("FCM", "Fetching FCM token failed", task.exception)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val data = HashMap<String, Any>()
            data["isOpenShareLink"] = true
            data["restaurantId"] = intent.data?.lastPathSegment.toString()
            mainViewModel.isOpenShareLink.value = data
        }
    }

    /**
     * Initializes and configures the Bottom Navigation View and the Navigation Host Fragment.
     */
    private fun setUpBottomNavigationWithNavHostFragment() {
        // Retrieve the NavHostFragment from the fragment manager
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Initialize the NavController from the NavHostFragment
        navController = navHostFragment.navController

//        val previousEntry = navController.previousBackStackEntry
//        if(previousEntry?.savedStateHandle?.contains("itemId") == true){
//            previousEntry.savedStateHandle.clearSavedStateProvider("itemId")
//        }
        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, bundle ->
            Log.d(TAG, "setUpBottomNavigationWithNavHostFragment: ${destination.id}")


            when (destination.id) {
                
                R.id.homeScreenFragment, R.id.qrScreenFragment, R.id.profileScreenFragment, R.id.pointScreenFragment -> {
                    binding.navView.visibility = VISIBLE
                    mainViewModel.compoundId = ""
                }
                R.id.percentScreenFragment -> {
                    binding.navView.visibility = VISIBLE
                }

                else -> {
                    binding.navView.visibility = GONE
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Handle the update result
        inAppUpdateManager.handleUpdateResult(requestCode, resultCode)
    }
    override fun attachBaseContext(newBase: Context) {
        LanguagesHelper.changeLanguage(newBase,LanguagesHelper.getCurrentLanguage(newBase))
        LanguagesHelper.changeLanguage(newBase,LanguagesHelper.getCurrentLanguage(newBase))

        val newLocale = Locale(LanguagesHelper.getCurrentLanguage(newBase))

        val context: Context = MyContextWrapper.wrap(newBase, newLocale)

        super.attachBaseContext(context)
//        }
    }

}

