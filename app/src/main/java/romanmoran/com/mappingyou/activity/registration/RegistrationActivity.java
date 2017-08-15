package romanmoran.com.mappingyou.activity.registration;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.OnClick;
import romanmoran.com.mappingyou.R;
import romanmoran.com.mappingyou.activity.BaseActivity;
import romanmoran.com.mappingyou.activity.login.LoginActivity;
import romanmoran.com.mappingyou.data.PreferencesData;
import romanmoran.com.mappingyou.data.User;
import romanmoran.com.mappingyou.utility.Constants;
import romanmoran.com.mappingyou.utility.Utility;

public class RegistrationActivity extends BaseActivity implements RegistrationMvp.View {

    public static final String TAG = RegistrationActivity.class.getName() ;

    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.rbPhone)
    RadioButton rbPhone;
    @BindView(R.id.rbMail)
    RadioButton rbMail;
    @BindView(R.id.etNumberOrEmail)
    EditText etNumberOrEmail;
    @BindView(R.id.btnSendCode)
    Button btnSendCode;
    @BindView(R.id.tvSendCodeAgain)
    TextView tvSendCodeAgain;
    @BindView(R.id.viewFlipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.etConfirmationCode)
    EditText etConfirmationCode;
    @BindView(R.id.etNameReg)
    EditText etNameReg;
    @BindView(R.id.etPasswordReg)
    EditText etPassword;
    @BindView(R.id.etPasswordAgain)
    EditText etPasswordAgain;

    private RegistrationPresenter mPresenter;

    private Location coordinates;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_registration;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new RegistrationPresenter(this);
        rg.check(R.id.rbMail);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case(R.id.rbMail):
                    ccp.setVisibility(View.GONE);
                    etNumberOrEmail.setHint(R.string.email);
                    etNumberOrEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);
                    break;
                case(R.id.rbPhone):
                    ccp.setVisibility(View.VISIBLE);
                    etNumberOrEmail.setHint(R.string.input_number);
                    etNumberOrEmail.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
            }
        });

        FusedLocationProviderClient mFusedLocationClient = new FusedLocationProviderClient(RegistrationActivity.this);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.LOCATION_PERMISSIONS_CODE);
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(RegistrationActivity.this, location -> {
            if (location!=null){
                coordinates = location;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.onResume(this);
    }

    @OnClick(R.id.btnSendCode)
    void send(){
        String etInput = etNumberOrEmail.getText().toString();
        if (etInput.isEmpty()){
            showToast(R.string.error_empty);
            return;
        }else {
            viewFlipper.setVisibility(View.VISIBLE);
            timer();
            int id = rg.getCheckedRadioButtonId();
            switch (id) {
                case R.id.rbMail:
                    mPresenter.processMail(etInput);
                    break;
                case R.id.rbPhone:
                    mPresenter.processPhone(ccp.getSelectedCountryCodeAsInt()+etInput);
                    break;
            }
        }
    }

    private void timer() {
        setEnableComponents(false);
        new CountDownTimer(10000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = +millisUntilFinished/1000;
                tvSendCodeAgain.setText(String.format("%s %d",getString(R.string.send_code_again),seconds));
            }

            @Override
            public void onFinish() {
                tvSendCodeAgain.setText("");
                setEnableComponents(true);
            }
        }.start();
    }

    @Override
    public void sendSuccess(User registeredUser) {
        PreferencesData.saveAuthJson(Utility.getStringJsonWithData(registeredUser),true);
        newInstance(this,LoginActivity.class);
        if (coordinates!=null) {
            registeredUser.setCoordinates(coordinates);
        }
        finish();
    }


    @OnClick(R.id.tvConfirmation)
    void confirmation(){
        String code = etConfirmationCode.getText().toString();
        mPresenter.checkCode(code);
    }

    @Override
    public void checkedCode(boolean flag) {
        btnSendCode.setEnabled(false);
        setEnableComponents(false);
        if (flag)
            viewFlipper.showNext();
    }

    @OnClick(R.id.tvGoToRegistration)
    void registration(){
        String name = etNameReg.getText().toString();
        String password = etPassword.getText().toString();
        String passwordAgain = etPasswordAgain.getText().toString();
        String emailOrPhone = etNumberOrEmail.getText().toString();
        boolean isEmail = Utility.validateEmail(emailOrPhone);
        String login = isEmail?emailOrPhone:ccp.getSelectedCountryCodeAsInt()+emailOrPhone;

        mPresenter.registration(name,password,passwordAgain,login,isEmail);
    }



    void setEnableComponents(boolean flag){
        rg.setEnabled(flag);
        etNumberOrEmail.setEnabled(flag);
        rbMail.setEnabled(flag);
        rbPhone.setEnabled(flag);
        ccp.setEnabled(flag);
        btnSendCode.setEnabled(flag);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
