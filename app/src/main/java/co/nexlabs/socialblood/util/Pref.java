package co.nexlabs.socialblood.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yemyatmin on 23/3/16.
 */
public class Pref {
  private static Pref pref;
  public String mAuthToken = "auth_token";
  private SharedPreferences mPreference;
  private SharedPreferences.Editor mEditor;
  private Context mContext;

  public Pref(Context context) {
    this.mContext = context;
    mPreference = context.getSharedPreferences(Constants.DEFAULT_SHARE_DATA, 0);
    mEditor = mPreference.edit();
  }

  public static synchronized Pref getInstance(Context context) {
    if (pref == null) {
      pref = new Pref(context);
    }

    return pref;
  }

  public void clearAll() {
    mEditor.clear();
    mEditor.commit();
  }

  public boolean isExist(String key) {
    return mPreference.contains(key);
  }


  public String getAuthToken() {
    return mPreference.getString(mAuthToken, "");
  }

  public void setAuthToken(String authToken) {
    mEditor.putString(mAuthToken, authToken);
    mEditor.commit();
  }

}
