package romanmoran.com.mappingyou.fragments;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import romanmoran.com.mappingyou.R;
import romanmoran.com.mappingyou.adapter.ContactsAdapter;
import romanmoran.com.mappingyou.data.PreferencesData;
import romanmoran.com.mappingyou.data.User;
import romanmoran.com.mappingyou.utility.Constants;

/**
 * Created by roman on 25.07.2017.
 */

public class ContactsFragment extends BaseFragment {

    public final static String TAG = ContactsFragment.class.getName();

    // Defines a variable for the search string
    private String mSearchString = "";
    // Defines the array to hold values that replace the ?
    private String[] mSelectionArgs = {"%" + mSearchString +"%"};

    @BindView(R.id.rvContacts)
    RecyclerView mContactsList;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_contacts;
    }

    public static ContactsFragment newInstance() {
        Bundle args = new Bundle();

        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //mContactsList.setAdapter(contactsAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestContacts();

    }

    @Override
    public void onStop() {
        PreferencesData.saveAuth(true);
        super.onStop();
    }

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private void requestContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            showContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts();
            } else {
                Log.e("Permissions", "Access denied");
            }
        }
    }

    private void showContacts(){
        // Initializes a loader for loading the contacts
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                // Starts the query
                return new CursorLoader(
                        getActivity(),
                        ContactsContract.Contacts.CONTENT_URI,
                        Constants.PROJECTION,
                        Constants.SELECTION,
                        mSelectionArgs,
                        null);
            }
            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor c) {
                // Put the result Cursor in the adapter for the ListView
                int mNameColIdx = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
                int mIdColIdx = c.getColumnIndex(ContactsContract.Contacts._ID);
                List<User> users = new ArrayList<User>();
                for (int i = 0; i < c.getCount(); i++) {
                    c.moveToPosition(i);
                    String contactName = c.getString(mNameColIdx);
                    long contactId = c.getLong(mIdColIdx);

                    User user = new User();
                    user.setName(contactName);
                    user.setUri(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId));
                    users.add(user);
                }
                Collections.sort(users, (o1, o2) -> {
                    int compare = o1.getName().compareToIgnoreCase(o2.getName());
                    if (compare != 0) {
                        return compare;
                    }
                    return o1.getName().compareToIgnoreCase(o2.getName());
                });
                mContactsList.setAdapter(new ContactsAdapter(users));
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {
                // TODO do I need to do anything here?
            }
        });
    }




}
