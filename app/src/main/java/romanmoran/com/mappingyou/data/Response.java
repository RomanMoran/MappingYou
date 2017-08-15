package romanmoran.com.mappingyou.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by roman on 24.07.2017.
 */

public class Response {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("error")
    @Expose
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
