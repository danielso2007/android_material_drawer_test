package br.com.daniel.oliveira.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import br.com.daniel.oliveira.activity.R;

public class Utils {

    public static void setPref(Context c, String pref, String val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putString(pref, val);
        e.commit();
    }

    public static String getPref(Context c, String pref, String val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(pref, val);
    }

    public static void setPref(Context c, String pref, boolean val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putBoolean(pref, val);
        e.commit();
    }

    public static boolean getPref(Context c, String pref, boolean val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(pref, val);
    }

    public static void delPref(Context c, String pref) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.remove(pref);
        e.commit();
    }

    public static void setPref(Context c, String pref, int val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putInt(pref, val);
        e.commit();

    }

    public static int getPref(Context c, String pref, int val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getInt(pref, val);
    }

    public static void setPref(Context c, String pref, long val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putLong(pref, val);
        e.commit();
    }

    public static long getPref(Context c, String pref, long val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getLong(pref, val);
    }

    public static ContextThemeWrapper GetDialogContext(Activity act) {
        ContextThemeWrapper themedContext;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            themedContext = new ContextThemeWrapper(act, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        } else {
            themedContext = new ContextThemeWrapper(act, android.R.style.Theme_Light_NoTitleBar);
        }
        return themedContext;
    }

    public static void SendBroadcast(Activity activity, String action) {
        LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent(action));
    }

    public static void RegisterBroadcast(Activity activity, BroadcastReceiver broadcastReceiver, String action) {
        LocalBroadcastManager.getInstance(activity).registerReceiver(broadcastReceiver, new IntentFilter(action));
    }

    public static void UnregisterBroadcast(Activity activity, BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(broadcastReceiver);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void SetDeviceLanguage(Context ctx, String lan) {
        Locale locale = new Locale(lan);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        ctx.getApplicationContext().getResources().updateConfiguration(config, null);
    }

    public static Bitmap DisplayBase64(String base64_img, String base64_type) {
        Bitmap decodedByte = null;
        try {
            base64_img = base64_img.replace(base64_type, Constant.EMPTY);
            String encodedImage = base64_img;
            // Debug.e("encodedImage", encodedImage);
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decodedByte;
    }

    public static String GetKM(String dis) {
        DecimalFormat distance = new DecimalFormat("0.00");
        return distance.format(Double.valueOf(dis) / 1000);
    }

    public static String GetDecimalFormat(Double value) {
        return new DecimalFormat("#,##,##,##0.00").format(value);
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static View GetCustomView(Activity c, int res) {
        View v = c.getLayoutInflater().inflate(res, null);
        return v;
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    public static boolean isInternetConnected(Context mContext) {
        boolean outcome = Boolean.FALSE;

        try {
            if (mContext != null) {
                ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        outcome = Boolean.TRUE;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        outcome = Boolean.TRUE;
                    }
                } else {
                    // not connected to the internet
                    outcome = Boolean.FALSE;
                }
            }
        } catch (Exception e) {
            Log.e(Constant.TAG, e.getMessage());
        }

        return outcome;
    }

    public static float random(float min, float max) {
        return (float) (min + (Math.random() * ((max - min) + 1)));
    }

    public static int random(int min, int max) {
        return Math.round((float) (min + (Math.random() * ((max - min) + 1))));
    }

    public static void hideKeyBoard(Context c, View v) {
        InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static int getDeviceWidth(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.widthPixels;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        return 480;
    }

    public static int getDeviceWidth(Context context, int percentage) {
        int width = getDeviceWidth(context);
        float actualWidth = (width * percentage) / 100;

        return (int) actualWidth;
    }

    public static int getDeviceHeight(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.heightPixels;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        return 800;
    }

    public static void sendExceptionReport(Exception e) {
        e.printStackTrace();
        Log.e(Constant.TAG, e.getMessage());
        try {
            // TODO: Enviar ao servidor???
        } catch (Exception e1) {
            e1.printStackTrace();
            Log.e(Constant.TAG, e1.getMessage());
        }
    }

    public static String getAndroidId(Context c) {
        String aid;
        try {
            aid = Constant.EMPTY;
            aid = Settings.Secure.getString(c.getContentResolver(), "android_id");

            if (aid == null) {
                aid = "No DeviceId";
            } else if (aid.length() <= 0) {
                aid = "No DeviceId";
            }
        } catch (Exception e) {
            e.printStackTrace();
            aid = "No DeviceId";
        }

        return aid;
    }

    public static String getAppVersion(Context c) {
        try {
            return c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(Constant.TAG, e.getMessage());
        }
        return "Undefined";
    }

    public static int getAppVersionCode(Context c) {
        try {
            return c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(Constant.TAG, e.getMessage());
        }
        return 0;

    }

    public static String getPhoneModel(Context c) {
        try {
            return android.os.Build.MODEL;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        return Constant.EMPTY;
    }

    public static String getPhoneBrand(Context c) {
        try {
            return android.os.Build.BRAND;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        return Constant.EMPTY;
    }

    public static String getPhoneBrandModel(Context c) {
        try {
            return getPhoneBrand(c) + Constant.BLANK + getPhoneModel(c);
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        return Constant.EMPTY;
    }

    public static String getOsVersion(Context c) {
        try {
            return android.os.Build.VERSION.RELEASE;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        return Constant.EMPTY;
    }

    public static int getOsAPIlevel(Context c) {
        try {
            return android.os.Build.VERSION.SDK_INT;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        return 0;
    }

    public static Date parseTimeUTCtoDefault(String time, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern,
                Locale.getDefault());
        try {

            sdf.setTimeZone(TimeZone.getTimeZone(Constant.UTC));
            Date d = sdf.parse(time);
            sdf = new SimpleDateFormat(pattern, Locale.getDefault());
            sdf.setTimeZone(Calendar.getInstance().getTimeZone());

            return sdf.parse(sdf.format(d));

        } catch (Exception e) {
            Log.e(Constant.TAG, e.getMessage());
        }

        return new Date();
    }

    public static String parseCalendarFormat(Calendar c, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(c.getTime());
    }

    public static String parseTime(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(new Date(time));
    }

    public static Date parseTime(String time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            Log.e(Constant.TAG, e.getMessage());
        }

        return new Date();
    }

    public static String parseTime(String time, String fromPattern, String toPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromPattern, Locale.getDefault());
        try {
            Date d = sdf.parse(time);
            sdf = new SimpleDateFormat(toPattern, Locale.getDefault());
            return sdf.format(d);
        } catch (Exception e) {
            Log.e(Constant.TAG, e.getMessage());
        }

        return Constant.EMPTY;
    }

    public static String nullSafe(String content) {
        if (content == null) {
            return Constant.EMPTY;
        }

        return content;
    }

    public static boolean isSDcardMounted() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return true;
        }

        return false;
    }

    private static ProgressDialog dialog;

    public static void showDialog(final Activity c, final String title, final String message) {

        try {
            c.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    dialog = new ProgressDialog(c);
                    dialog.setTitle(title);
                    dialog.setMessage(message);
                    dialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(Constant.TAG, e.getMessage());
        }
    }

    public static void showProgressDialog(final Activity c, final String title, final String message) {

        try {
            c.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    dialog = new ProgressDialog(c);
                    dialog.setTitle(title);
                    dialog.setMessage(message);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(Constant.TAG, e.getMessage());
        }
    }

    public static void closeProgressDialog() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(Constant.TAG, e.getMessage());
        }
    }

    public static void showToastLong(Context c, String Message) {
        Toast.makeText(c, Message, Toast.LENGTH_LONG).show();
    }

    public static void showToastLong(Context c, int Message) {
        Toast.makeText(c, Message, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(Context c, String Message) {
        Toast.makeText(c, Message, Toast.LENGTH_SHORT).show();
    }

    public static void showToastShort(Context c, int Message) {
        Toast.makeText(c, Message, Toast.LENGTH_SHORT).show();
    }

    public static String toMD5(String string) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(Constant.MD5);
            md5.update(string.getBytes(Constant.UTF8));
            byte[] md5bytes = md5.digest();
            StringBuffer hexmd5 = new StringBuffer();

            for (byte b : md5bytes) {
                if ((b & 0xFF) < 0x10)
                    hexmd5.append(Constant.ZERO);
                hexmd5.append(Integer.toHexString(b & 0xFF));
            }
            return hexmd5.toString();
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }
        return Constant.EMPTY;
    }

    public static String implode(ArrayList<String> data) {
        try {
            String devices = Constant.EMPTY;
            for (String iterable_element : data) {
                devices = devices + Constant.COMMA + iterable_element;
            }

            if (devices.length() > 0 && devices.startsWith(Constant.COMMA)) {
                devices = devices.substring(1);
            }

            return devices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Constant.EMPTY;
    }

    public static void generateNotification(Context context, String title, String message, Intent notificationIntent, int id) {

        int icon = R.mipmap.ic_launcher;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent intent = null;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), icon);

        if (notificationIntent != null) {
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context).setContentTitle(Constant.EMPTY + title)
                .setContentText(Constant.EMPTY + message).setSmallIcon(icon)
                .setLargeIcon(bitmap).setTicker(Constant.EMPTY + title)
                .setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true);

        if (notificationIntent != null) {
            notificationBuilder.setContentIntent(intent);
        }

        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(Constant.EMPTY + title).bigText(Constant.EMPTY + message));

        Notification notification = notificationBuilder.build();
        notificationManager.notify(id, notification);
    }
}
