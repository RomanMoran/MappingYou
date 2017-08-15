package romanmoran.com.mappingyou.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;

import romanmoran.com.mappingyou.R;

/**
 * Created by roman on 25.07.2017.
 */

public class SmsFragment extends BaseFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_sms;
    }

    public static SmsFragment newInstance() {
        Bundle args = new Bundle();
        SmsFragment fragment = new SmsFragment();
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
        inflater.inflate(R.menu.menu_sms, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
