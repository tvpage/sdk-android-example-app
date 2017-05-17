package com.tvpage.demo.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.tvpage.demo.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

/**
 * Created by MTPC on 9/29/2016.
 */

public class CommonUtils {

    public static String VIDEO_GALLERY_SOURCE_KEY = "VIDEO_GALLERY_SOURCE_KEY";
    public static String VIDEO_GALLERY_SOURCE_VALUE = "VIDEO_GALLERY_SOURCE_VALUE";

    public static final String PARCABLE_VIDEO_MODEL_KEY = "PARCABLE_VIDEO_MODEL_KEY";

    public static final int NUMBER_OF_RESULT_TO_RETURN = 5;

    public static String getDateFromTimestamp(long timeStamp, String dateFormat) {

        try {
            DateFormat sdf = new SimpleDateFormat(dateFormat);
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getCurrentTime(String format) {
        String currentTime = "";

        if (format != null) {
            long currentMilli = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentMilli);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            currentTime = simpleDateFormat.format(calendar.getTime());

            return currentTime;
        } else {
            return currentTime;
        }

    }

    public static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static int getCurrentHour() {
        int hour = 0;
        Calendar cal = Calendar.getInstance(); //Create Calendar-Object
        cal.setTime(new Date());               //Set the Calendar to now
        hour = cal.get(Calendar.HOUR_OF_DAY); //Get the hour from the calendar

        return hour;
    }

    public static int getCurrentMinute() {
        int minutes = 0;
        Calendar cal = Calendar.getInstance(); //Create Calendar-Object
        cal.setTime(new Date());               //Set the Calendar to now
        minutes = cal.get(Calendar.MINUTE); //Get the hour from the calendar

        return minutes;
    }

    public static int getCurrentSecond() {
        int second = 0;
        Calendar cal = Calendar.getInstance(); //Create Calendar-Object
        cal.setTime(new Date());               //Set the Calendar to now
        second = cal.get(Calendar.SECOND); //Get the hour from the calendar

        return second;
    }

    public static String getCurrentTimeInDesiredFormat(String formatToGet) {
        try {
            String str = "";
            //return 24 hour format as kk is used
            SimpleDateFormat sdf = new SimpleDateFormat(formatToGet);
            str = sdf.format(new Date());
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void makeToast(String message, Context mContext) {
        try {
            Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sout(String object) {
        // System.out.println("" + object);
    }

    public static int getPixelValue(Context context, int dp) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, resources.getDisplayMetrics());
    }

    public static String convertTimeToTwelveHFmt(String timeToConvert) {
        try {
            final String time = timeToConvert;

            try {
                final SimpleDateFormat sdf24H = new SimpleDateFormat("kk:mm");
                final SimpleDateFormat sdf12H = new SimpleDateFormat("hh:mm a");
                final Date dateObj = sdf24H.parse(time);

                // Log.d("con t: ","cov t: "+sdf12H.format(dateObj));
                return sdf12H.format(dateObj);
            } catch (final ParseException e) {
                e.printStackTrace();
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static boolean isInternetConnected(Context context) {
        boolean isConnected = false;
        try {

            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            return isConnected;
        } catch (Exception e) {
            e.printStackTrace();
            return isConnected;
        }

    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static String getStringText(Context context, int textToget) {
        String textToReturn = "";
        try {
            textToReturn = context.getResources().getString(textToget);
            return textToReturn;
        } catch (Exception e) {
            e.printStackTrace();
            return textToReturn;
        }

    }

    public static Drawable getDrawables(Context context, int drawables) {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return context.getResources().getDrawable(drawables, null);
            } else {
                return context.getResources().getDrawable(drawables);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Date convertTimeToDate(String times) {
        Date dtToReturn = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("kk:mm");
            try {
                Date date = format.parse(times);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                dtToReturn = calendar.getTime();

                return dtToReturn;
            } catch (ParseException e) {

                e.printStackTrace();
                return dtToReturn;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return dtToReturn;
        }
    }


    public static void sout(Object object) {
        try {
            System.out.println("" + object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static OkHttpClient getOkHttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);


        return new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)
                .build();
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


    public static PackageInfo appVersionNameandCode(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return pInfo;
    }

    public static void cancelAllNotifications(Context context) {
        try {
            // Clear all notification

            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImageGlide(Context context, String image, ImageView imageView) {
        try {
            Glide.with(context).
                    load(image).
                    crossFade().
                    placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImageGlideProductDetails(Context context, String image, ImageView imageView) {
        try {
            Glide.with(context).
                    load(image).
                    crossFade().
                    placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImageGlideProduct(Context context, String image, ImageView imageView) {
        try {
            Glide.with(context).
                    load(image).
                    crossFade()
                    .override(300, 300).
                    placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImageGlideProductDetail(Context context, String image, ImageView imageView) {
        try {
            Glide.with(context).
                    load(image).
                    crossFade().
                    placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fitCenter()
                    .dontAnimate()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
