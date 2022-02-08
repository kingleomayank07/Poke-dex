package com.android.pokedex.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.pokedex.BuildConfig;
import com.android.pokedex.R;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Helper class for device
 * related information
 */
final public class Device {

  static final public String NO_CARRIER = "NO_CARRIER";

  /**
   * gets the name of the device
   * Eg: MotoX
   *
   * @return {@link String} device name
   */
  static final synchronized public String getDeviceName() {
    String manufacturer = Build.MANUFACTURER;
    String model = Build.MODEL;
    if (model.startsWith(manufacturer)) {
      return capitalize(model);
    } else {
      return capitalize(manufacturer) + " " + model;
    }
  }

//  public static String getDeviceId(Context c) {
//    return InstallationIdPreference.getDeviceId(c);
//  }

  static final synchronized public String getDeviceManufacturer() {
    String manufacturer = Build.MANUFACTURER;
    return capitalize(manufacturer);
  }

  static final synchronized public String getDeviceModel() {
    String model = Build.MODEL;
    return capitalize(model);
  }

  static private String capitalize(String s) {
    if (s == null || s.length() == 0) {
      return "";
    }
    char first = s.charAt(0);
    if (Character.isUpperCase(first)) {
      return s;
    } else {
      return Character.toUpperCase(first) + s.substring(1);
    }
  }

  /**
   * @param c {@link Context}
   * @return {@link FormFactor} of the device
   */
  final static public FormFactor getDeviceType(Context c) {
    boolean isTablet = c.getResources().getBoolean(R.bool.isTablet);
    if (isTablet) {
      return FormFactor.ANDROID_TABLET;
    } else {
      return FormFactor.ANDROID_PHONE;
    }
  }

  /**
   * @param c {@link Context}
   * @return {@link boolean} true if tablet false otherwise
   */
  final static public boolean isTablet(Context c) {
    return c.getResources().getBoolean(R.bool.isTablet);
  }

  /**
   * Get the OS version
   *
   * @return {@link String} OS Version
   */
  final static public String getOsVersion() {
    return Build.VERSION.RELEASE;
  }

  /**
   * Get the app version
   *
   * @return {@link String} App version
   */
  final static public String getAppVersion() {
    return BuildConfig.VERSION_NAME;
  }

  /**
   * Get the app version code
   *
   * @return App version code
   */
  final static public long getAppVersionCode() {
    return BuildConfig.VERSION_CODE;
  }

  /**
   * Get current {@link Locale} on Device.
   * <br />
   * This may not update as soon as user
   * changes {@link Locale}
   *
   * @param c {@link Context}
   * @return {@link Locale}
   */
  final static public Locale getDeviceLocale(Context c) {
    return c.getResources().getConfiguration().locale;
  }

  public static String getCountryCode(Context c) {
    String countryCode;
    try {
      final TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
      if (tm == null) return null;
      countryCode = tm.getSimCountryIso();
      if (countryCode != null && countryCode.length() == 2) { // SIM country code is available
        return countryCode.toUpperCase(Locale.US);
      } else if (tm.getPhoneType()
          != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
        countryCode = tm.getNetworkCountryIso();
        if (countryCode != null && countryCode.length() == 2) { // network country code is available
          return countryCode.toUpperCase(Locale.US);
        }
      }
    } catch (Exception e) {
      return null;
    }
    if (TextUtils.isEmpty(countryCode)) {
      countryCode = Device.getDeviceLocale(c).getCountry();
    }
    return countryCode;
  }

  /**
   * Gets the carrier name
   *
   * @param c {@link Context}
   * @return {@link String} Carrier name
   */
  final static public String getCarrier(Context c) {
    Object systemService = c.getSystemService(Context.TELEPHONY_SERVICE);
    if (null != systemService) {
      if (systemService instanceof TelephonyManager) {
        return ((TelephonyManager) systemService).getNetworkOperatorName();
      }
    }
    return NO_CARRIER;
  }

  /**
   * Get device's default TimeZone
   *
   * @return {@link TimeZone}
   */
  final static public TimeZone getDeviceTimeZone() {
    return TimeZone.getDefault();
  }

  public static final float getDeviceDensity(Context mContext) {
    DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
    float density = dm.density;
    Log.d("Screen density= ", "" + density);
    return density;
  }

  public static String getInstallationId(Context c) {
    return "";
  }

  /**
   * FormFactor
   */
  public enum FormFactor {
    ANDROID_PHONE,
    ANDROID_TABLET
  }
}
