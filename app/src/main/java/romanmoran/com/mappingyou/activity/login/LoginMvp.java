package romanmoran.com.mappingyou.activity.login;

import android.content.Intent;

import romanmoran.com.mappingyou.base_mvp.BaseMvp;
import romanmoran.com.mappingyou.data.User;

/**
 * Created by roman on 14.08.2017.
 */

public interface LoginMvp {

    interface View extends BaseMvp.View{

        void loginSuccess(User user);

    }

    interface Presenter extends BaseMvp.Presenter{

        void login(String numberOrEmail,String password);

        void onResume(View view);

        boolean isSocialActivityResult(int requestCode, int resultCode, Intent data);

    }
}
