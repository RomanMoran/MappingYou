package romanmoran.com.mappingyou.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;

import romanmoran.com.mappingyou.R;

/**
 * Created by roman on 25.07.2017.
 */

public class FriendsFragment extends BaseFragment {
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_friends;
    }

    public static FriendsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_friends,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
