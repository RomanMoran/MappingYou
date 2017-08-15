package romanmoran.com.mappingyou.activity.registration;

import android.util.Log;

import romanmoran.com.mappingyou.R;
import romanmoran.com.mappingyou.base_mvp.BasePresenterImpl;
import romanmoran.com.mappingyou.data.Response;
import romanmoran.com.mappingyou.data.User;
import romanmoran.com.mappingyou.utility.Utility;

/**
 * Created by roman on 14.08.2017.
 */

public class RegistrationPresenter extends BasePresenterImpl implements RegistrationMvp.Presenter {

    private RegistrationMvp.View view;
    private String rndCode;

    public RegistrationPresenter(RegistrationMvp.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onResume(RegistrationMvp.View view) {
        this.baseView = view;
        this.view = view;
    }

    @Override
    public void processMail(String email) {
        if (!Utility.validateEmail(email)) {
            view.showToast(R.string.incorrect_email);
            return;
        }
        String sign = Utility.md5(email+Utility.SECRET_KEY);

        User user = new User();
        rndCode = Utility.rndCode();

        user.setEmail(email);
        user.setText(rndCode);
        user.setSign(sign);

        getApiClient().sendEmailCode(user);

    }

    @Override
    public void processPhone(String phoneNumberWithCode) {
        if (!Utility.validatePhoneNumber(phoneNumberWithCode)){
            view.showToast(R.string.incorrect_phone);
            return;
        }
        String sign = Utility.md5(phoneNumberWithCode+Utility.SECRET_KEY);

        User user = new User();
        rndCode = Utility.rndCode();

        user.setDestination_address(phoneNumberWithCode);
        user.setMessage(rndCode);
        user.setSign(sign);

        getApiClient().sendSmsCode(user);
    }


    @Override
    public void checkCode(String code) {
        if (!code.equals(rndCode)){
            view.showToast(R.string.incorrect_code);
            view.checkedCode(false);
            return;
        }
        view.checkedCode(true);
    }

    @Override
    public void registration(String name, String password, String passwordAgain,String login,boolean isEmail) {
        if (name.isEmpty()) {
            view.showToast(R.string.empty_name);
            return;
        }
        if (!password.equals(passwordAgain)) {
            view.showToast(R.string.dont_much_password);
            return;
        }

        login = isEmail?login:Utility.getStringFromResourses(R.string.no_email);

        User registeredUser = new User();
        registeredUser.setName(name);
        registeredUser.setEmail(login);
        registeredUser.setPhNumber(!isEmail?login:null);
        registeredUser.setPass(password);
        registeredUser.setSign(Utility.md5(login+password+Utility.SECRET_KEY));

        getApiClient().registration(registeredUser)
                .compose(Utility.applySchedulers())
                .subscribe(this::onNext,this::onError);
    }


    public void onNext(Response response) {
        Log.d(TAG,"id = "+response.getId());
        if(response.getError()==null) {
            User registeredUser = new User();
            registeredUser.setId(response.getId());
            view.sendSuccess(registeredUser);
        }else {
            view.showToast(response.getError());
        }
    }


    @Override
    public void onError(Throwable t) {
        view.showToast(t.getMessage());
    }

}
