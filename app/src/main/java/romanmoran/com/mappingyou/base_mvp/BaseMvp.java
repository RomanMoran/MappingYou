package romanmoran.com.mappingyou.base_mvp;

/**
 * Created by roman on 14.08.2017.
 */

public interface BaseMvp {

    interface View{

        void showToast(String text);

        void showToast(int text);

        void setProgressIndicator(boolean active);

    }

    interface Presenter{
        void onDestroy();
    }

    interface InteractionFinishedListener {
        void onError(String text);

        void onError(int textId);

        void onError(Throwable throwable);

        void onComplete();

        void setProgressIndicator(boolean active);
    }

}
