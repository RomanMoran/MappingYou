package romanmoran.com.mappingyou.activity.registration;

import romanmoran.com.mappingyou.base_mvp.BaseMvp;
import romanmoran.com.mappingyou.data.User;

/**
 * Created by roman on 14.08.2017.
 */

public interface RegistrationMvp  {

    interface View extends BaseMvp.View{
        void sendSuccess(User user);

        void checkedCode(boolean flag);
    }

    interface Presenter extends BaseMvp.Presenter{

        void onResume(View view);

        void processMail(String text);

        void processPhone(String text);

        void registration(String name,String password,String passwordAgain,String emailOrPhone,boolean isEmail);

        void checkCode(String code);

    }


}
