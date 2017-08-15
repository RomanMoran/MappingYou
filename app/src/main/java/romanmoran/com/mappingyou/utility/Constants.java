package romanmoran.com.mappingyou.utility;

import android.provider.ContactsContract;

import com.vk.sdk.VKScope;

/**
 * Created by roman on 02.08.2017.
 */

public class Constants {
    public static final String DATE_FORMAR_VK = "dd.mm.yyyy";
    public static final String DATE_FORMAR_FB = "dd/mm/yyyy";
    public static final String DATE_FORMAR_NECESSARY = "yyyy-dd-mm";
    public static final int LOCATION_PERMISSIONS_CODE = 100;
    public static final String USER_DATA_EXTRA = "USER_DATA_EXTRA";
    public static final String NEGATIVE_90 = "-90";
    public static final String NEGATIVE_180 = "-180";
    public static final String POSITIVE_90 = "90";
    public static final String POSITIVE_180= "180";
    public static final String PERMISSION_FB_LOG_LIST []={"public_profile", "user_birthday", "email"} ;
    public static String[] SCOPE_VK = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL, VKScope.EMAIL};


    public static final String SELECTION =
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";


    public static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            };


}
