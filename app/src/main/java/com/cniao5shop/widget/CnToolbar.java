package com.cniao5shop.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;

import com.cniao5shop.R;


/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/7/23
 * @Description: ${todo}
 */

public class CnToolbar extends Toolbar {

    private LayoutInflater mInflater;

    private View mView;
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mRightButton;

    public CnToolbar(Context context) {
        this(context, null);
    }

    public CnToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CnToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        initView();
        setContentInsetsRelative(10, 10);

        if(attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CnToolbar, defStyleAttr, 0);

            final Drawable rightIcon = a.getDrawable(R.styleable.CnToolbar_rightButtonIcon);
            if (rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }

            boolean isSearchView = a.getBoolean(R.styleable.CnToolbar_isShowSearchView, false);

            if(isSearchView){
                showSearchView();
                hideTitleView();
            }

            CharSequence rightButtonText = a.getText(R.styleable.CnToolbar_rightButtonText);
            if(rightButtonText !=null){
                setRightButtonText(rightButtonText);
            }

            a.recycle();
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setRightButtonIcon(Drawable icon){
        if(null != mRightButton){
            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }
    }

    public void setRightButtonIcon(int icon){
        setRightButtonIcon(getResources().getDrawable(icon));
    }

    public void setRightButtonOnClickListener(OnClickListener listener){
        mRightButton.setOnClickListener(listener);
    }

    public void setRightButtonText(CharSequence text){
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }

    public void setRightButtonText(int id){
        setRightButtonText(getResources().getString(id));
    }

    public Button getRightButton(){
        return this.mRightButton;
    }

    private void initView() {

        if(null == mView){
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);
            mTextTitle = mView.findViewById(R.id.toolbar_title);
            mSearchView = mView.findViewById(R.id.toolbar_searchview);
            mRightButton = mView.findViewById(R.id.toolbar_rightButton);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_HORIZONTAL);
            addView(mView, lp);
        }

    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();

        if(mTextTitle != null){
            mTextTitle.setText(title);
            showTitleView();
        }
        mTextTitle.setVisibility(VISIBLE);
    }

    public  void showSearchView(){
        if(mSearchView !=null)
            mSearchView.setVisibility(VISIBLE);

    }


    public void hideSearchView(){
        if(mSearchView !=null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView(){
        if(mTextTitle !=null)
            mTextTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }

}
