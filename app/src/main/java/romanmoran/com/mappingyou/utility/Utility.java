package romanmoran.com.mappingyou.utility;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.facebook.internal.ImageRequest;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.gson.Gson;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.util.VKUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import romanmoran.com.mappingyou.BuildConfig;
import romanmoran.com.mappingyou.MyApplication;
import romanmoran.com.mappingyou.R;
import romanmoran.com.mappingyou.data.User;

/**
 * Created by roman on 22.07.2017.
 */

public class Utility {

    public static final String TAG = Utility.class.getName();
    public static final String SECRET_KEY = "Globus100";

    public static <T> FlowableTransformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static void hashCodeFb(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    BuildConfig.APPLICATION_ID,
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static String getStringFromDouble(Double number) {
        return number != null ? String.format("%.4f", number).toString().replaceAll(",", ".") : "0.0";
    }

    public static Double getDoubleFromString(String number) {
        if (number != null)
            return (!number.isEmpty() && !number.equals("null")) ? Double.parseDouble(number) : 0.0f;
        return 0.0;
    }


    public static void fingerprintVK(Context context) {
        String[] fingerprints = VKUtil.getCertificateFingerprint(context, context.getPackageName());
        for (int i = 0; i < fingerprints.length; i++) {
            Log.d("TAG", fingerprints[i]);
        }
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static boolean validateEmail(String str) {
        String regExp =
                "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern pattern = Pattern.compile(regExp);
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

    public static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;

    }

    public static String rndCode() {
        Random rnd = new Random();
        return String.valueOf(rnd.nextInt(10000));
    }


    public static BitmapDescriptor getBitmap(View someView) {
        someView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        someView.layout(0, 0, someView.getMeasuredWidth(), someView.getMeasuredHeight());
        someView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(someView.getMeasuredWidth(), someView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        someView.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(returnedBitmap);
    }

    public static void checkInternet(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            new AlertDialog.Builder(context)
                    .setTitle((R.string.attention))
                    .setMessage((R.string.internet_error))
                    .setPositiveButton("OK", null).show();
            return;
        }
    }

    public static void buildAlertMessageNoGps(final Context context) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage(R.string.gps_permission)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        ActivityCompat.startActivity(context, new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), null);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public static String getStringJsonWithData(User userData) {
        return new Gson().toJson(userData);
    }

    public static User getUserFromJsonString(String jsonFormatAuth) throws JSONException {
        return new Gson().fromJson(jsonFormatAuth, User.class);
    }

    public static boolean checkLocationPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static String getStringFromResourses(int id) {
        return MyApplication.getInstance().getString(id);
    }

    public static User getUserFromFB(JSONObject object) {
        User userData = new User();

        if (object != null) {
            String profileImageUrl = ImageRequest.getProfilePictureUri(object.optString("id"), 500, 500).toString();
            String gender;
            if ((gender = object.optString("gender")) != null) {
                if (!TextUtils.isEmpty(gender) && gender.length() > 1) {
                    gender = (gender.substring(0, 1).equals("f") ? "w" : "m");
                }
            }
            String id = object.optString("id");
            String name = object.optString("name");
            String birth = object.optString("birthday");
            try {
                SimpleDateFormat input = new SimpleDateFormat(Constants.DATE_FORMAR_FB, Locale.getDefault());
                SimpleDateFormat output = new SimpleDateFormat(Constants.DATE_FORMAR_NECESSARY, Locale.getDefault());
                Date parsed = input.parse(birth);
                birth = output.format(parsed);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String email = object.optString("email");
            userData.setEmail(email);
            userData.setName(name);
            userData.setGener(gender);
            userData.setPicUrl(profileImageUrl);
            userData.setFbId(id);
            userData.setDate(birth);
            userData.setSign(Utility.md5(id + Utility.SECRET_KEY));//4fc7f011fa3c1af5c49ceafc93de3e82
            Log.d(TAG, "id = " + id);
            Log.d(TAG, "sign = " + Utility.md5(id + Utility.SECRET_KEY));

        }
        return userData;
    }

    public static User getUserFromVk(VKApiUserFull user) {
        String name = user.first_name;
        String gener = "";
        if (user.sex == VKApiUserFull.Sex.FEMALE) gener = "w";
        if (user.sex == VKApiUserFull.Sex.MALE) gener = "m";
        String date = user.bdate;
        try {
            SimpleDateFormat input = new SimpleDateFormat(Constants.DATE_FORMAR_VK, Locale.getDefault());
            SimpleDateFormat output = new SimpleDateFormat(Constants.DATE_FORMAR_NECESSARY, Locale.getDefault());
            Date parsed = input.parse(date);
            date = output.format(parsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String picUrl = user.photo_100;
        String phone = user.mobile_phone;
        String token = VKAccessToken.currentToken().accessToken;
        String sign = (Utility.md5(user.getId() + Utility.SECRET_KEY));

        User userData = new User();
        userData.setName(name);
        userData.setGener(gener);

        userData.setDate(date);
        userData.setPicUrl(picUrl);
        userData.setPhNumber(phone);
        userData.setToken(token);
        userData.setSign(sign);
        return userData;
    }
}
