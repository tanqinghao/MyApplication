package com.example.trans.myapplication.http;

/**
 * Created by Administrator on 2018/7/16.
 */

public interface INet {
    void onFailure(Object f, boolean show);
    void onResponse(Object r0, Object r1);
}
