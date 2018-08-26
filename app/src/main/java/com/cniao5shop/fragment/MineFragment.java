package com.cniao5shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cniao5shop.CniaoApplication;
import com.cniao5shop.Contants;
import com.cniao5shop.LoginActivity;
import com.cniao5shop.R;
import com.cniao5shop.bean.User;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/7/20
 * @Description: ${todo}
 */

public class MineFragment extends BaseFragment {

    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;
    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }


    @Override
    public void init() {
        User user = CniaoApplication.getInstance().getUser();
        showUser(user);

    }

    @OnClick(value = {R.id.img_head, R.id.txt_username})
    public void toLogin(View view){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, Contants.REQUEST_CODE);
    }

    /**
     * 退出登录
     * @param view
     */
    @OnClick(R.id.btn_logout)
    public void loginOut(View view){
        CniaoApplication.getInstance().clearUser();
        showUser(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        User user = CniaoApplication.getInstance().getUser();
        showUser(user);

    }

    private void showUser(User user){
        if(null != user){
            mTxtUserName.setText(user.getUsername());
            Picasso.with(getActivity()).load(user.getLogo_url()).into(mImageHead);
        }else{
            mTxtUserName.setText(R.string.to_login);
        }
    }
}
