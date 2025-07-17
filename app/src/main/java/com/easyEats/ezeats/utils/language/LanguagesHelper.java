package com.easyEats.ezeats.utils.language;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.easyEats.ezeats.MyApplication;
import com.easyEats.ezeats.utils.Constants;

import java.util.Locale;


/**
 * Created by osamaMohsen on 1/12/19.
 */

public class LanguagesHelper {

    public static void setSystemLocale(Configuration config, Locale locale) {
        Locale locales = new Locale(Constants.INSTANCE.getDEFAULT_LANGUAGE());
        config.setLocale(locales);
    }


    private static final String TAG = "LanguagesHelper";

    public static void changeLanguage(Context context, String languageToLoad) {
        if (!languageToLoad.equals(Constants.INSTANCE.getLANUAGE_EN()) &&
                !languageToLoad.equals(Constants.INSTANCE.getLANUAGE_AR())) {
            languageToLoad = Constants.INSTANCE.getDEFAULT_LANGUAGE();
        }

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        setSystemLocale(config, locale);
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
//        baseApplication.createConfigurationContext(config);
        MyApplication.Companion.getBaseApplication().createConfigurationContext(config);
        setLanguage(languageToLoad);
    }



//    public static void setLanguage(String language) {
////        SharedPreferences userDetails = TheContentApp.instance.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
////        SharedPreferences.Editor editor = userDetails.edit();
////        editor.putString(LANGUAGE, language);
////        editor.apply();
//    }


    public static void setLanguage(String language) {
        SharedPreferences userDetails = MyApplication.Companion.getBaseApplication().getSharedPreferences(Constants.APP_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.putString(Constants.INSTANCE.getLANGUAGE(), language);
        editor.apply();
    }


    public static String getCurrentLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                Constants.APP_PREFERENCES_NAME,
                Context.MODE_PRIVATE
        );
        String lang = preferences.getString(Constants.INSTANCE.getLANGUAGE(),
                Constants.INSTANCE.getDEFAULT_LANGUAGE());
        return !lang.isEmpty() ? lang : Constants.INSTANCE.getDEFAULT_LANGUAGE();

    }
}