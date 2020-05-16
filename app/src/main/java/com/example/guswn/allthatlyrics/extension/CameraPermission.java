package com.example.guswn.allthatlyrics.extension;

import android.Manifest;
import android.content.Context;
import android.util.Log;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class CameraPermission {
    /** 카메라 관련 코드들*/
    Context context;
    private boolean isPermitted = false;
    public  CameraPermission(Context context1){
        this.context = context1;
    }
    public void  isGranted (){
        //TedPermission 라이브러리 -> 카메라 권한 획득

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(Userinfo_Edit.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                isPermitted = true;
                Log.e("1_CameraPermission ","Permission Granted ");
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                // Toast.makeText(Userinfo_Edit.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                isPermitted= false;
                Log.e("2_CameraPermission ","Permission Denied"+ deniedPermissions.toString());
            }
        };
        TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("퍼미션 거부시 ,서비스를 이용 할 수 없습니다\n\n설정에서 퍼미션을 승인하세요 ")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                //카메라 퍼미션
                .check();
        //TedPermission 라이브러리 -> 카메라 권한 획득
    }

    public boolean getIsPermitted() {
        Log.e("3_CameraPermission ",isPermitted+"");
        return isPermitted;
    }
}
