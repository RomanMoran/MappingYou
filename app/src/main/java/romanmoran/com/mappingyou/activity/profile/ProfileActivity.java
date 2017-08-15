package romanmoran.com.mappingyou.activity.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import romanmoran.com.mappingyou.R;
import romanmoran.com.mappingyou.activity.BaseActivity;
import romanmoran.com.mappingyou.api_utility.ApiClient;
import romanmoran.com.mappingyou.data.PreferencesData;
import romanmoran.com.mappingyou.data.User;
import romanmoran.com.mappingyou.utility.Constants;

public class ProfileActivity extends BaseActivity {

    private static final String TAG = ProfileActivity.class.getName();
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    private View mCustomView;
    private TextView tvBar;
    private User user;

    private ApiClient apiClient = ApiClient.getInstance();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_profite;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"Profile Activity onCreate");

        LayoutInflater mInflater = LayoutInflater.from(this);
        bottomBar.setDefaultTab(R.id.tab_maps);

        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

        tvBar = ButterKnife.findById(mCustomView.getRootView(),R.id.titleBar);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        user = (User) (getIntent().getParcelableExtra(Constants.USER_DATA_EXTRA));

        if (user!=null){
            apiClient.setCoord(user);
            initBottomBar();
        }
    }

    private void initBottomBar() {
        bottomBar.setOnTabSelectListener(tabId ->  {
                switch (tabId){
                    case R.id.tab_friends:
                        //bottomBar.getCurrentTab().setBackground(getDrawable(R.drawable.ic_friends_tabbar_active));
                        tvBar.setText(R.string.friends);
                        showFriendsFragment();
                        break;
                    case R.id.tab_contact:
                        tvBar.setText(R.string.contacts);
                        showContactsFragment();
                        break;
                    case R.id.tab_sms:
                        tvBar.setText(R.string.sms);
                        showSmsFragment();
                        break;
                    case R.id.tab_maps:
                        tvBar.setText("");
                        showMapFragment(user);
                        break;
                    case R.id.tab_profile:
                        getSupportActionBar().hide();
                        tvBar.setText("");
                        showProfileFragment(user);
                        break;
                }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"Profile Activity onPause");

        super.onPause();
    }

    @Override
    protected void onStop() {
        saveData();
        Log.d(TAG,"Profile Activity onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"Profile Activity onDestroy");
        super.onDestroy();
    }

    private void saveData() {
        PreferencesData.saveLastCoordinates(0,0,0,0);
    }


    @Override
    public void showToast(String text) {

    }

    @Override
    public void showToast(int text) {

    }
}
