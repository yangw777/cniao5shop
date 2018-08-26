package com.cniao5shop.http;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/7/27
 * @Description: ${todo}
 */

public abstract class BaseCallback<T> {
    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallback()
    {
        mType = getSuperclassTypeParameter(getClass());
    }

    public  abstract void onBeforeRequest(Request request);


    public abstract void onFailure(Request request, Exception e);

    public abstract void onResponse(Response response);


    public abstract void onSuccess(Response response, T t);

    public abstract void onError(Response response, int code, Exception e);

    /**
     * Token 验证失败。状态码401,402,403 等时调用此方法
     * @param response
     * @param code

     */
    public abstract void onTokenError(Response response, int code);
}
