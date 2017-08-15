package romanmoran.com.mappingyou.api_utility;

/**
 * Created by roman on 03.08.2017.
 */

public class ApiConstants {
    public static final String HEADER = "Content-Type: application/x-www-form-urlencoded";

    public static final String POST_AUTH = "auth";
    public static final String POST_NEW_USER = "NewUser";
    public static final String POST_SEND_EMAIL_CODE = "sendCode";
    public static final String POST_SEND_SMS_CODE = "sendSmsCode";
    public static final String POST_SEND_SMS_SOCAUTH = "socauth";
    public static final String POST_SEND_SMS_UPD_USER = "UpdUser";

    public static final String GET_USER = "getuser?";
    public static final String GET_NEAR_USER = "getNearUser?";
    public static final String GET_NEAR_EVENT = "getNearEvent?";
    public static final String GET_CONT_MAP_USER = "getContMapUser?";
    public static final String GET_COORD = "setcoord";
    public static final String GET_REMOVE = "remove";

    public static final String NECESSARY_FIELDS_FB = "id,name,birthday,gender,email";
    public static final String FIELDS = "fields";


    public static final String FIELD_API_SOCIAL_NETWORK = "network";
    public static final String FIELD_API_SOCIAL_NETWORK_LOGIN = "token";
    public static String FIELD_API_FCM_TOKEN = "fcm_token";
}
