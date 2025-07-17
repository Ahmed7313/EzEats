package com.easyEats.ezeats.utils.language

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.easyEats.ezeats.MyApplication
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MySharedPreferences @Inject constructor(val context: Context) {
    val PREFERENCES_NAME = "app-sharedPreferences"
    val LANGUAGE = "language"
    val DEFAULT_LANGUAGE = "ar"
    fun setSystemLocale(config: Configuration, locale: Locale?) {
        config.setLocale(locale)
    }

    fun changeLanguage(languageToLoad: String) {
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        setSystemLocale(config, locale)
        context.createConfigurationContext(config)
        MyApplication.baseApplication.createConfigurationContext(config)
        //        TheContentApp.appContext.createConfigurationContext(config);
//        TheContentApp.instance.createConfigurationContext(config);
//        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageToLoad));
        setLanguage(languageToLoad)
    }

    private fun getPrefereces() : SharedPreferences{
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }


    fun setKey(key: String,value:String){
        getPrefereces().edit().putString(key,value).apply()
    }

    fun getKey(key: String) : String{
        val value = getPrefereces().getString(key,"")
        value?.let {
            return value
        }
        return ""
    }

    fun setLanguage(language: String) {

//        SharedPreferences userDetails = TheContentApp.instance.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = userDetails.edit();
//        editor.putString(LANGUAGE, language);
//        editor.apply();
    }


    fun getCurrentLanguage(currentContext: Context? = context): String {

        val preferences = if (currentContext != null) currentContext.getSharedPreferences(
            PREFERENCES_NAME,
            Context.MODE_PRIVATE
        ) else context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        if (preferences.getString(LANGUAGE, "")?.isNotEmpty() == true) return preferences.getString(
            LANGUAGE,
            DEFAULT_LANGUAGE
        )!!
        else setLanguage(DEFAULT_LANGUAGE)
        return DEFAULT_LANGUAGE
    }
}