package com.jsj.permission;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static com.jsj.permission.PermissionManage.PermissionRequestCode.CAMERA_PERMISSION_RequestCode;

public class MainActivity extends AppCompatActivity implements PermissionManage.PermissionResult{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.camera_p:
                PermissionManage.requestPermission(this,Manifest.permission.CAMERA,CAMERA_PERMISSION_RequestCode,this);
                break;
            case R.id.location_p:
                break;
            case R.id.storage_p:
                break;
            case R.id.phone_p:
                break;
            case R.id.photo_p:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManage.PermissionHandle(this,requestCode,permissions,grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onPermissionsRefused(int requestCode) {

    }
}
