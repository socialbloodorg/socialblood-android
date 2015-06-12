package org.socialblood.socialblood;

import android.app.Activity;
import android.content.Context;

import com.parse.Parse;

/**
 * Created by aadeshpatel on 6/11/15.
 */
public class ParseManager {
    private static final String appID = "38SkQipaNtwdF0KttyO7zlIsX07hXX2WQxu1iNJA";
    private static final String clientKey = "S9qfufVVZI4Je0ooBreMXCii3gB7yoOE6NzqO6PC";

    public static void init(Context context) {
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, appID, clientKey);
    }
}
