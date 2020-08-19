package marumaru.v01.kingofmemorization;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserSharedPreference {

    static final String PREF_USER_ID = "userId";

    static SharedPreferences getSharedPreferences(Context context){

        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserId(String userId, Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_ID, userId);
        editor.commit();
    }

    public static String getUserId(Context context){
        return getSharedPreferences(context).getString(PREF_USER_ID, "");
    }

    public static void removeUserId(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear(); //저장된 데이터 모두 삭제 로그아웃 위함
        editor.commit();
    }

}
