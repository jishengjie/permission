package com.jsj.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Jackie
 * on 2017/5/16
 * email 1224717356@qq.com
 */

public class PermissionManage {

    private static SharedPreferences preferences;

    /**
     * 权限请求RequestCode
     */
    public class PermissionRequestCode{
        public static final int CAMERA_PERMISSION_RequestCode=10;
    }
    /**
     * 请求权限
     *
     * @param activity
     * @param permission
     * @param requestCode
     * @param permissionResult
     */
    public static void requestPermission(Activity activity,String permission,int requestCode,PermissionResult permissionResult){
        if(preferences==null){
            preferences = activity.getSharedPreferences("permission_sf", Context.MODE_PRIVATE);
        }
        if(ActivityCompat.checkSelfPermission(activity, permission)!= PackageManager.PERMISSION_GRANTED){
            //权限未允许
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)){
                //系统是否还可以弹出权限获取提示框，因为第二次及之后再请求权限的时候，会给用户一个CheckBox,如果用户勾选了就不会再弹框了,此方法返回值就是false
                ActivityCompat.requestPermissions(activity,new String[]{permission},requestCode);
                Log.e("shouldShowRequest","为真");
            }else{
                //不再弹出权限请求框的时候及部分品牌的手机默认直接就是返回FALSE
                if(!preferences.getBoolean(permission,false)) {
                    preferences.edit().putBoolean(permission,true).commit();//权限请求框是否会弹出
                    ActivityCompat.requestPermissions(activity,new String[]{permission},requestCode);
                }else{//跳转设置界面让用户手动开启权限
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package",activity.getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        localIntent.setAction(Intent.ACTION_VIEW);
                        localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                        localIntent.putExtra("com.android.settings.ApplicationPkgName",activity.getPackageName());
                    }
                    activity.startActivity(localIntent);
                }
                Log.e("shouldShowRequest","为假");
            }
            Log.e("checkSelfPermission","未授权");
        }else{
            permissionResult.onPermissionsGranted(requestCode);
        }
    }

    /**
     * 收到系统回调之后处理
     *
     * @param permissionResult
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void PermissionHandle(PermissionResult permissionResult,int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        boolean granted=true;
        if(permissions.length>1){
            for(int i=0;i<grantResults.length;i++){
                granted=granted&&(grantResults[i]==PackageManager.PERMISSION_GRANTED);
                Log.e("permissionsResult",permissions[i].toString()+"  "+((grantResults[i]==PackageManager.PERMISSION_GRANTED)?"授权通过":"授权不通过"));
            }
        }else{
            granted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
            Log.e("permissionsResult",permissions[0].toString()+"  "+((grantResults[0]==PackageManager.PERMISSION_GRANTED)?"授权通过":"授权不通过"));
        }
        if(granted)
            permissionResult.onPermissionsGranted(requestCode);
        else
            permissionResult.onPermissionsRefused(requestCode);
    }

    public interface PermissionResult{
        /**
         * 请求的运行时权限被授权
         * @param requestCode
         */
        void onPermissionsGranted(int requestCode);

        /**
         * 请求的运行时权限被拒绝
         * @param requestCode
         */
        void onPermissionsRefused(int requestCode);
    }
}
