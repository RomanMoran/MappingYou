package romanmoran.com.mappingyou.fragments.map;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import romanmoran.com.mappingyou.base_mvp.BasePresenterImpl;
import romanmoran.com.mappingyou.data.Event;
import romanmoran.com.mappingyou.data.Item;
import romanmoran.com.mappingyou.data.User;

/**
 * Created by roman on 15.08.2017.
 */

public class MapPresenter extends BasePresenterImpl implements MapMvp.Presenter {

    private MapMvp.View view;
    private List<Item> itemsList = new ArrayList<>();

    public MapPresenter(MapMvp.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public void addAllEvents(User event) {
        setProgressIndicator(true);
        itemsList = null;

        getApiClient().nearEvents(event)
                .subscribe(this::onNextEvent,this::onError,this::onComplete);
    }

    @Override
    public void addAllFriends(User user) {
        setProgressIndicator(true);
        getApiClient().contMapUser(user)
                .subscribe(this::onNextUser,this::onError,this::onComplete);
    }

    @Override
    public void addAllUsers(User user) {
        setProgressIndicator(true);
        itemsList = null;
        getApiClient().nearUsers(user)
                .subscribe(this::onNextUser,this::onError,this::onComplete);
    }

    private void onNextEvent(List<Event> events) {
        itemsList = new ArrayList<>();
        Log.d(TAG,"onNext");
        for (int i = 0; i <events.size() ; i++) {
            events.get(i).setmPosition(events.get(i).getLatLang());

            itemsList.add(events.get(i));
        }
    }

    private void onNextUser(List<User> users) {
        itemsList = new ArrayList<>();
        Log.d(TAG,"onNext");
        for (int i = 0; i <users.size() ; i++) {
            users.get(i).setmPosition(users.get(i).getLatLang());
            itemsList.add(users.get(i));
        }
    }


    public void onComplete() {
        Log.d(TAG,"onComplete");
        setProgressIndicator(false);
        view.startUpdateMap(itemsList);
    }


}
