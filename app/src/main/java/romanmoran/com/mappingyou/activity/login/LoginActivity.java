package romanmoran.com.mappingyou.activity.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.vk.sdk.VKSdk;

import org.json.JSONException;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import romanmoran.com.mappingyou.R;
import romanmoran.com.mappingyou.activity.BaseActivity;
import romanmoran.com.mappingyou.activity.profile.ProfileActivity;
import romanmoran.com.mappingyou.activity.registration.RegistrationActivity;
import romanmoran.com.mappingyou.data.PreferencesData;
import romanmoran.com.mappingyou.data.User;
import romanmoran.com.mappingyou.utility.Constants;
import romanmoran.com.mappingyou.utility.Utility;

public class LoginActivity extends BaseActivity implements LoginMvp.View {

    private static final String TAG = LoginActivity.class.getName();

    private LoginPresenter mPresenter;

    @BindView(R.id.btnFb)
    Button btnFb;
    @BindView(R.id.imgForgot)
    ImageView imgForgot;
    @BindView(R.id.etLogin)
    EditText etLogin;
    @BindView(R.id.etPassword)
    EditText etPassword;

    private boolean authorization = false;

    private String jsonFormatAuth;

    private Location mLocation;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.checkInternet(LoginActivity.this);
        mPresenter = new LoginPresenter(this);
    }


    private void goToAccount() {
        loadData();
        if (authorization) {
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            try {
                User userData = Utility.getUserFromJsonString(jsonFormatAuth);
                intent.putExtra(Constants.USER_DATA_EXTRA, userData);
                startActivity(intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.onResume(this);

        FusedLocationProviderClient mFusedLocationClient = new FusedLocationProviderClient(LoginActivity.this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.LOCATION_PERMISSIONS_CODE);
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(LoginActivity.this,
                location -> { if(location!=null) mLocation=location; });
        goToAccount();
    }

    @OnClick(R.id.btnVk)
    void vkLog() {
        VKSdk.login(this, Constants.SCOPE_VK);
    }

    @OnClick(R.id.btnFb)
    void fbLog() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(Constants.PERMISSION_FB_LOG_LIST));
    }

    @OnClick(R.id.tvRegister)
    void tvReg() {
        newInstance(this, RegistrationActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mPresenter.isSocialActivityResult(requestCode,resultCode,data))
            super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick(R.id.btnEnter)
    void btnEnter() {
        String stringNumberOrEmail = etLogin.getText().toString();
        String password = etPassword.getText().toString();
        mPresenter.login(stringNumberOrEmail,password);
    }



    @Override
    public void loginSuccess(User user) {
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        intent.putExtra(Constants.USER_DATA_EXTRA, user);
        startActivity(intent);
        finish();
    }


    private void loadData() {
        authorization = PreferencesData.getAuth();
        jsonFormatAuth = PreferencesData.getJson();
        Log.d(TAG, "loadData oauth = " + authorization);
        Log.d(TAG, "loadData jsonFormatAuth = " + jsonFormatAuth);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null)
            mPresenter.onDestroy();
    }
}
