package romanmoran.com.mappingyou.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import romanmoran.com.mappingyou.R;
import romanmoran.com.mappingyou.api_utility.ApiConstants;
import romanmoran.com.mappingyou.base_mvp.BasePresenterImpl;
import romanmoran.com.mappingyou.data.PreferencesData;
import romanmoran.com.mappingyou.data.User;
import romanmoran.com.mappingyou.utility.DebugUtility;
import romanmoran.com.mappingyou.utility.Utility;

/**
 * Created by roman on 14.08.2017.
 */

public class LoginPresenter extends BasePresenterImpl implements LoginMvp.Presenter {

    private LoginMvp.View view;
    private CallbackManager mFbAccessTokenCallback;
    private VKCallback<VKAccessToken> mVkAccessTokenCallback;


    private boolean authorization = false;

    public LoginPresenter(LoginMvp.View view) {
        super(view);
        this.view = view;
        initFbCallback();
        initVkCallBack();
    }

    @Override
    public void onResume(LoginMvp.View view) {
        this.view = view;
        this.baseView = view;
    }

    @Override
    public boolean isSocialActivityResult(int requestCode, int resultCode, Intent data) {
        mFbAccessTokenCallback.onActivityResult(requestCode, resultCode, data);
        if (!VKSdk.onActivityResult(requestCode,resultCode,data,mVkAccessTokenCallback));

        return true;
    }



    @Override
    public void login(String numberOrEmail, String password) {
        boolean isEmail = Utility.validateEmail(numberOrEmail);
        if (numberOrEmail.isEmpty()) {
            onError(R.string.empty_field);
            return;
        }
        if (password.isEmpty()) {
            onError(R.string.empty_password);
            return;
        }
        String sign = Utility.md5(isEmail ? numberOrEmail : Utility.getStringFromResourses(R.string.no_email) + password + Utility.SECRET_KEY);
        User userData = new User();
        userData.setEmail(isEmail ? numberOrEmail : Utility.getStringFromResourses(R.string.no_email));
        userData.setPhNumber(isEmail ? null : numberOrEmail);
        userData.setPass(password);
        userData.setSign(sign);

        getApiClient().authorization(userData)
                .subscribe(this::onNext, this::onError);
    }

    private void initVkCallBack() {
        mVkAccessTokenCallback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(final VKAccessToken res) {
                final VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "bdate,sex,contacts,photo_100,messages"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKList<VKApiUserFull> list = (VKList<VKApiUserFull>) response.parsedModel;
                        VKApiUserFull user = list.get(0);
                        User userData = Utility.getUserFromVk(user);
                        userData.setEmail(res.email);
                        userData.setVkId(res.userId);

                        getApiClient().socauth(userData)
                                .subscribe(LoginPresenter.this::onNext,
                                        LoginPresenter.this::onError);

                    }
                });
                view.showToast("Good");
            }

            @Override
            public void onError(VKError error) {
                String ex = error.toString();
                DebugUtility.logTest(TAG, ex);
                view.showToast(error.toString());
            }
        };
    }

    private void initFbCallback(){
        mFbAccessTokenCallback = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mFbAccessTokenCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess");
                if (AccessToken.getCurrentAccessToken() != null) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken(), (object, response) -> {
                                getApiClient()
                                        .socauth(Utility.getUserFromFB(object))
                                        .subscribe(
                                                LoginPresenter.this::onNext,
                                                LoginPresenter.this::onError
                                        );
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString(ApiConstants.FIELDS, ApiConstants.NECESSARY_FIELDS_FB);
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                DebugUtility.logTest(TAG, "onError ");
                LoginPresenter.this.onError(error.getMessage());
            }
        });
    }

    public void onNext(User user) {
        DebugUtility.logTest(TAG, "onNext loginSuccess");
       // Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        authorization = true;
        user.setSign(user.getId() + Utility.md5(user.getId() + Utility.SECRET_KEY));
        saveData(user,authorization);

        if (view!=null){
            view.loginSuccess(user);
        }
    }


    private void saveData(User userData,boolean authorization) {
        PreferencesData.saveAuthJson(Utility.getStringJsonWithData(userData), authorization);
        Log.d(TAG, "saveData authorization = " + authorization);

    }


}
