package com.cniao5shop;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.cniao5shop.bean.User;
import com.cniao5shop.utils.UserLocalData;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/7/28
 * @Description: ${todo}
 */

public class CniaoApplication extends Application {

    private User user;

    private Intent intent;

    private static  CniaoApplication mInstance;

    public static  CniaoApplication getInstance(){
        if(null == mInstance)
            mInstance = new CniaoApplication();
        return  mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        initUser();

        Fresco.initialize(this);
    }

    private void initUser(){

        this.user = UserLocalData.getUser(this);
    }

    public User getUser(){

        return user;
    }

    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user =null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);


    }

    public String getToken(){

        return  UserLocalData.getToken(this);
    }

    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent(){
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){
        context.startActivity(intent);
        this.intent = null;
    }
}
