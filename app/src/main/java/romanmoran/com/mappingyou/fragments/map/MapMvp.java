package romanmoran.com.mappingyou.fragments.map;

import java.util.List;

import romanmoran.com.mappingyou.base_mvp.BaseMvp;
import romanmoran.com.mappingyou.data.Event;
import romanmoran.com.mappingyou.data.Item;
import romanmoran.com.mappingyou.data.User;

/**
 * Created by roman on 15.08.2017.
 */

public interface MapMvp {

    interface View extends BaseMvp.View{

        void startUpdateMap(List<Item>items);

    }

    interface Presenter extends BaseMvp.Presenter{

        void addAllEvents(User event);

        void addAllUsers(User user);

        void addAllFriends(User user);

    }

}
