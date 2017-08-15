package romanmoran.com.mappingyou.base_mvp;

import romanmoran.com.mappingyou.api_utility.ApiClient;
import romanmoran.com.mappingyou.utility.DebugUtility;

/**
 * Created by roman on 14.08.2017.
 */

public class BasePresenterImpl implements BaseMvp.Presenter, BaseMvp.InteractionFinishedListener {
    public static final String TAG = BasePresenterImpl.class.getName();
    protected BaseMvp.View baseView;

    private ApiClient apiClient = ApiClient.getInstance();

    public BasePresenterImpl(BaseMvp.View view) {
        this.baseView = view;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    protected boolean isExistsView() {
        return baseView != null;
    }

    @Override
    public void onDestroy() {
        baseView = null;
    }

    @Override
    public void onError(String text) {
        DebugUtility.logTest(TAG, "onError text");
        if (baseView != null) {
            baseView.setProgressIndicator(false);
            baseView.showToast(text);
        }
    }

    @Override
    public void onError(int textId) {
        DebugUtility.logTest(TAG, "onError textId");
        if (baseView != null) {
            baseView.setProgressIndicator(false);
            baseView.showToast(textId);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable != null) {
            onError(throwable.getMessage());
        }
    }

    @Override
    public void onComplete() {
        setProgressIndicator(false);
    }


    @Override
    public void setProgressIndicator(boolean active) {
        if (baseView != null)
            baseView.setProgressIndicator(active);
    }


}
