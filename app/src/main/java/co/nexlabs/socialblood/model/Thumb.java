package co.nexlabs.socialblood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by myozawoo on 3/14/16.
 */

public class Thumb implements Serializable {

    @SerializedName("url")
    @Expose
    private Object url;

    /**
     * @return The url
     */
    public Object getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(Object url) {
        this.url = url;
    }
}