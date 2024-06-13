package com.example.appphongtro.session;
import android.content.Context;
import android.content.SharedPreferences;
public class SessionManagement {
    private static final String USER_ID_PREFS_KEY = "user_id";
    private static final String ROLE_PREFS_KEY = "role";
    private static final String PREFS_NAME = "MyPrefs";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private final Context context;


    public SessionManagement(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserId(int userId) {
        editor.putInt(USER_ID_PREFS_KEY, userId);
        editor.apply();
    }

    public void saveRole(int role) {
        editor.putInt(ROLE_PREFS_KEY, role);
        editor.apply();
    }

    public int getRole () {
        return sharedPreferences.getInt(ROLE_PREFS_KEY, -1);
    }

    public int getUserId() {
        return sharedPreferences.getInt(USER_ID_PREFS_KEY, -1);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
