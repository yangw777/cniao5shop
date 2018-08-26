package com.cniao5shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cniao5shop.bean.User;
import com.cniao5shop.http.OkHttpHelper;
import com.cniao5shop.http.SpotsCallBack;
import com.cniao5shop.msg.LoginRespMsg;
import com.cniao5shop.utils.DESUtil;
import com.cniao5shop.utils.ToastUtils;
import com.cniao5shop.widget.ClearEditText;
import com.cniao5shop.widget.CnToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @ViewInject(R.id.toolbar)
    private CnToolbar mToolbar;
    @ViewInject(R.id.etxt_phone)
    private ClearEditText mEtxtPhone;
    @ViewInject(R.id.etxt_pwd)
    private ClearEditText mEtxtPwd;

    @ViewInject(R.id.txt_toReg)
    private TextView mTxtReg;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewUtils.inject(this);

        initToolBar();
    }

    private void initToolBar(){


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginActivity.this.finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @OnClick(R.id.btn_login)
    public void login(View view){
        String phone = mEtxtPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        String pwd = mEtxtPwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.show(this, "请输入密码");
            return;
        }

        Map<String, Object> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));

        okHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallBack<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                CniaoApplication application =  CniaoApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                if(application.getIntent() == null){
                    setResult(RESULT_OK);
                    finish();
                }else{
                    application.jumpToTargetActivity(LoginActivity.this);
                    finish();
                }


            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }

    /**
     * 跳转到注册页面
     * @param v
     */
    @OnClick(R.id.txt_toReg)
    public void register(View v) {
        startActivityForResult(new Intent(this, RegActivity.class),1);
//        startActivity(new Intent(this, RegActivity.class));
    }
}
