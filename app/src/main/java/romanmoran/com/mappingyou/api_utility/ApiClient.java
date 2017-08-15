package romanmoran.com.mappingyou.api_utility;

import java.util.List;

import io.reactivex.Flowable;
import romanmoran.com.mappingyou.data.Event;
import romanmoran.com.mappingyou.data.Response;
import romanmoran.com.mappingyou.data.User;
import romanmoran.com.mappingyou.utility.Constants;
import romanmoran.com.mappingyou.utility.RetrofitUtils;
import romanmoran.com.mappingyou.utility.Utility;

/**
 * Created by roman on 14.08.2017.
 */

public class ApiClient {
    private static final String TAG = ApiClient.class.getName();
    private static ApiClient instance;

    private ApiService mApiService = RetrofitUtils.getInstance().getApiService();


    protected ApiClient() {

    }

    public static ApiClient getInstance() {
        if (instance == null) instance = new ApiClient();
        return instance;
    }

    public Flowable<User> socauth(User user){
        return mApiService.socauth(user)
                .flatMap(response -> mApiService.getUserData(response.getId(),
                        Utility.md5(response.getId() + Utility.SECRET_KEY)))
                .compose(Utility.applySchedulers());

    }

    public Flowable<User> authorization(User user){
        return mApiService.authorization(user)
                .flatMap(response ->mApiService.getUserData(response.getId(), Utility.md5(response.getId() + Utility.SECRET_KEY)))
                .compose(Utility.applySchedulers());
    }

    public Flowable<List<User>> nearUsers(User user){
        return mApiService.getNearUser(user.getId(),
                Constants.NEGATIVE_90,
                Constants.NEGATIVE_180,
                Constants.POSITIVE_90,
                Constants.POSITIVE_180,
                Utility.md5(Constants.NEGATIVE_90+Constants.NEGATIVE_180
                        + Utility.SECRET_KEY))
                .compose(Utility.applySchedulers());
    }

    public Flowable<List<Event>> nearEvents(User event){
        return mApiService.getNearEvent(event.getId(),
                Constants.NEGATIVE_90,
                Constants.NEGATIVE_180,
                Constants.POSITIVE_90,
                Constants.POSITIVE_180,
                Utility.md5(Constants.NEGATIVE_90+Constants.NEGATIVE_180
                        + Utility.SECRET_KEY))
                .compose(Utility.applySchedulers());
    }


    public Flowable<List<User>> contMapUser(User user){
        return mApiService.getContMapUser(user.getId(),Utility.md5(user.getId()+ Utility.SECRET_KEY))
                .compose(Utility.applySchedulers());
    }


    public Flowable<Response> setCoord(User user){
        return mApiService.setCoord(user.getId(),user.getX(),user.getY(),Utility.md5(user.id+Utility.SECRET_KEY))
                .compose(Utility.applySchedulers());
    }

    public Flowable<Response> remove(User user){
       return mApiService.remove(user.getId(),user.getSign())
                .compose(Utility.applySchedulers());
    }


    public Flowable<Response>sendEmailCode(User user){
        return mApiService.sendEmailCode(user)
                .compose(Utility.applySchedulers());
    }

    public Flowable<Response> sendSmsCode(User user){
        return mApiService.sendSmsCode(user)
                .compose(Utility.applySchedulers());
    }

    public Flowable<Response> registration(User user){
        return mApiService.registration(user)
                .compose(Utility.applySchedulers());
    }
}
