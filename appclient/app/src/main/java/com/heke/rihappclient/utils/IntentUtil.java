package com.heke.rihappclient.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** 页面跳转控制类
 * Created by wgmhx on 2017/4/21.
 */

public final class IntentUtil {
    public static void newIntent(Context from_act, Class<?> to_act) {
        newIntent(from_act, to_act, null, 0, false);
    }

    public static void newIntent(Context from_act, Class<?> to_act, HashMap<String, Object> hashMaps) {
        newIntent(from_act, to_act, hashMaps, 0, false);
    }

    public static void newIntentForResult(Activity from_act, Class<?> to_act, int code) {
        newIntent(from_act, to_act, null, code, true);
    }

    /**
     * @param from_act  Context对象
     * @param to_act    目标页
     * @param hashMaps  携带参数
     */
    public static void newIntent(Context from_act, Class<?> to_act, HashMap<String, Object> hashMaps, int code, boolean isResult) {
        Intent intent = new Intent(from_act, to_act);
        if (null != hashMaps) {
            if (hashMaps.size() > 0) {
                for (Map.Entry<String, Object> entry : hashMaps.entrySet()) {
                    if (entry.getValue() instanceof Integer) {
                        intent.putExtra(entry.getKey(), (Integer) entry.getValue());
                    } else if (entry.getValue() instanceof Long) {
                        intent.putExtra(entry.getKey(), (Long) entry.getValue());
                    } else if (entry.getValue() instanceof Boolean) {
                        intent.putExtra(entry.getKey(), (Boolean) entry.getValue());
                    } else if(entry.getValue() instanceof String){
                        intent.putExtra(entry.getKey(), (String) entry.getValue());
                    }else if(entry.getValue() instanceof Serializable){
                        intent.putExtra(entry.getKey(), (Serializable)  entry.getValue());
                    }
                }
            }
        }
        from_act.startActivity(intent);

    }

    /**
     * 打电话
     */
    public static void call(Context context, String phone) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 发短信
     *
     * @param activity  Activity
     * @param number    电话
     * @param message   内容
     */
    public static void sendMessage(Activity activity, String number, String message) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
        sendIntent.putExtra("sms_body", message);
        activity.startActivity(sendIntent);
    }

}

