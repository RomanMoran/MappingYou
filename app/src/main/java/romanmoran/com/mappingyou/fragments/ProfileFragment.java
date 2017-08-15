package romanmoran.com.mappingyou.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import romanmoran.com.mappingyou.R;
import romanmoran.com.mappingyou.activity.login.LoginActivity;
import romanmoran.com.mappingyou.api_utility.ApiClient;
import romanmoran.com.mappingyou.data.PreferencesData;
import romanmoran.com.mappingyou.data.User;
import romanmoran.com.mappingyou.enums.GENDER;
import romanmoran.com.mappingyou.utility.Utility;

import static romanmoran.com.mappingyou.enums.GENDER.FEMALE;

/**
 * Created by roman on 25.07.2017.
 */

public class ProfileFragment extends BaseFragment {
    private static final String TAG = ProfileFragment.class.getName();
    @BindView(R.id.tvNameProfile)
    TextView tvNameProfile;
    @BindView(R.id.imgProfile)
    ImageView imgProfile;


    private User user;
    private ApiClient apiClient = ApiClient.getInstance();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_profile;
    }

    public static ProfileFragment newInstance(User user) {
        
        Bundle args = new Bundle();
        
        ProfileFragment fragment = new ProfileFragment();
        args.putParcelable(User.TAG,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            user = getArguments().getParcelable(User.TAG);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNameProfile.setText(user.getName());
        GENDER gender = GENDER.fromValue(user.getGener());
        Glide.with(activity)
                .load(user.getPicUrl()==null || user.getPicUrl().isEmpty()?
                        (gender==FEMALE? R.drawable.ic_woman_user_image_stub:R.drawable.ic_man_user_image_stub) :
                        user.getPicUrl())
                .into(imgProfile);
    }

    @Override
    public void onResume() {
        activity.getSupportActionBar().hide();
        super.onResume();
    }

    @Override
    public void onPause() {
        activity.getSupportActionBar().show();
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @OnClick(R.id.tvExitProfile)
    void onExitProfile(){
        Intent intent = new Intent(activity, LoginActivity.class);

        PreferencesData.saveAuth(false);
        startActivity(intent);
        activity.finish();
    }

    @OnClick(R.id.tvDeleteAccount)
    void onDeleteAccount(){
        final String id = user.id;
        final String sign = Utility.md5(user.id+ Utility.SECRET_KEY);
        new AlertDialog.Builder(activity)
                .setTitle((R.string.attention))
                .setMessage((R.string.remove_account))
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        apiClient.remove(user);
                        PreferencesData.saveAuth(false);
                        activity.newInstance(activity,LoginActivity.class);
                    }
                })
                .setNegativeButton("Нет",null)
                .show();


    }

}
