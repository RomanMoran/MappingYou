package romanmoran.com.mappingyou.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import romanmoran.com.mappingyou.R;
import romanmoran.com.mappingyou.activity.registration.RegistrationActivity;
import romanmoran.com.mappingyou.base_mvp.BaseMvp;
import romanmoran.com.mappingyou.data.User;
import romanmoran.com.mappingyou.fragments.ContactsFragment;
import romanmoran.com.mappingyou.fragments.FriendsFragment;
import romanmoran.com.mappingyou.fragments.map.MapFragment;
import romanmoran.com.mappingyou.fragments.ProfileFragment;
import romanmoran.com.mappingyou.fragments.SmsFragment;
import romanmoran.com.mappingyou.utility.Constants;
import romanmoran.com.mappingyou.utility.ProgressDialog;
import romanmoran.com.mappingyou.utility.Utility;

/**
 * Created by roman on 22.07.2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseMvp.View {

    private static final String TAG = BaseActivity.class.getName();

    protected ActionBar mActionBar;

    private Dialog waitingDialog;


    public static void newInstance(Context context, final Class<? extends AppCompatActivity> activityClass){
        newInstance(context,activityClass,false);
    }

    public void showProfileFragment(User user){
        String tag = RegistrationActivity.TAG;
        ProfileFragment fragment = ProfileFragment.newInstance(user);
        showFragment(R.id.fragment_container,fragment,tag,false);
    }

    public void showMapFragment(User user) {
        String tag = MapFragment.TAG;
        MapFragment fragment = MapFragment.newInstance(user);
        showFragment(R.id.fragment_container, fragment, tag, false);
    }

    public void showContactsFragment(){
        String tag = ContactsFragment.TAG;
        ContactsFragment fragment = ContactsFragment.newInstance();
        showFragment(R.id.fragment_container,fragment,tag,false);
    }

    public void showSmsFragment(){
        String tag = RegistrationActivity.TAG;
        SmsFragment fragment = SmsFragment.newInstance();
        showFragment(R.id.fragment_container,fragment,tag,false);
    }

    public void showFriendsFragment(){
        String tag = RegistrationActivity.TAG;
        FriendsFragment fragment = FriendsFragment.newInstance();
        showFragment(R.id.fragment_container,fragment,tag,false);
    }


    public void showFragment(int container, Fragment fragment, String tag, boolean addToBackStack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(container,fragment,tag);
        if (addToBackStack) ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }


    public static void newInstance(Context context, final Class<? extends AppCompatActivity> activityClass,boolean clearBackStack){
        if (activityClass == context.getClass()){
            return;
        }
        Intent intent = new Intent(context,activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (clearBackStack) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityCompat.startActivity(context,intent,null);
    }


    public abstract int getLayoutResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

    }

    protected void catchError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.LOCATION_PERMISSIONS_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.necessary_geoloc, Toast.LENGTH_SHORT).show();
                    statusCheck();

                }
                return;
            }

        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Utility.buildAlertMessageNoGps(this);
        }
    }

    public void setProgressIndicator(final boolean active) {
        runOnUiThread(() ->   {
                if (active) {
                    //if (waitingDialog == null)
                    waitingDialog = ProgressDialog.getWaitDialog(this, false);
                    waitingDialog.show();

                } else {
                    ProgressDialog.closeDialog(waitingDialog);
                }
        });
    }





}
