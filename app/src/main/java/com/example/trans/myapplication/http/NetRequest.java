package com.example.trans.myapplication.http;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/*import hua.yong.an.kang.R;
import hua.yong.an.kang.Utils.Constants;
import hua.yong.an.kang.Utils.Utils;
import hua.yong.an.kang.main.CusApplication;*/
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/16.
 */

public class NetRequest {
    private final String TAG = NetRequest.class.getName();
    protected Map<String,Object> params = new HashMap<>();
    private final String DOMIAN = "http://172.16.108.8/HYAK/";
    private OkHttpClient okHttpClient;
    private String token;
    public String json;
    private INet iNet;
    public NetRequest(INet iNet){
        this.iNet = iNet;
        okHttpClient = new OkHttpClient();
    }


    public void DownLoadFile(String URL) {
        Request request = new Request.Builder().url(DOMIAN + URL).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    File file = new File(Environment.getExternalStorageDirectory() + "文件名");
                    fileOutputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void postEcg(String parameters){
        Log.i(TAG,"----------------------postEcg");
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("hexdata", parameters);
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url("http://192.168.3.3:8080/api")
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                json  = response.body().string();
                Log.i(TAG, json);
            }
        });
    }
    public void getRequest(String URL, LinkedHashMap<String,String> params) {
        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");

        for (int i=0;i<params.size();i++ ) {
            String value=values.next();
//            try {
//                value= URLEncoder.encode(values.next(),"utf-8");
//            }catch (Exception e){
//                e.printStackTrace();
//            }

            stringBuffer.append(keys.next()+"="+value);
            if (i!=params.size()-1) {
                stringBuffer.append("&");
            }
        }
        Log.i(TAG, "GETurl="+DOMIAN + URL+stringBuffer.toString());
        Request.Builder requestBuilder = new Request.Builder().url(DOMIAN + URL+stringBuffer.toString());
        requestBuilder.method("GET", null);
        Call call = okHttpClient.newCall(requestBuilder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handleFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResposnse(response.body().string());
            }
        });
    }
    public void postRequest(String URL, Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        try {
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    builder.add(key, params.get(key).toString());
                }
            }
        }catch (NullPointerException n){
            return;
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(DOMIAN + URL)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handleFailure(e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResposnse(response.body().string());
            }
        });
    }

    private void handleResposnse(String jsonString){
        String infoCode = "";
        JSONObject obj = null;
        String json = jsonString;
        Log.i(TAG,"onResponse="+json);
        try {
            obj = new JSONObject(json);
            String code = obj.getString("code");
//            if (Integer.parseInt(code)== Constants.HTTP_USRER_NOT_EXIST){
//                infoCode = "用户（手机号）不存在";
//            }else if (Integer.parseInt(code)== Constants.HTTP_SMS_SEND_FAIL){
//                infoCode = "短信发送失败";
//            }else if (Integer.parseInt(code)== Constants.HTTP_SMS_CODE_ERROR){
//                infoCode = "验证码错误";
//            }else {
               infoCode = code;
//            }
            Object o = new JSONTokener(obj.getString("data")).nextValue();
            iNet.onResponse(infoCode,o);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    private void handleFailure(String error){
        String errorInfo = error;
        Log.i(TAG,"onFailure="+errorInfo);
        boolean show=true;
        iNet.onFailure(errorInfo,show);
    }
    public void UploadFile(String URL,String filename) {
        File file = new File(filename);
        MediaType MEDIATYPE = MediaType.parse("text/plain; charset=utf-8");
        RequestBody requestBody = RequestBody.create(MEDIATYPE, file);
        Request request = new Request.Builder().url(DOMIAN + URL)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
//                Log.e(TAG,e.getMessage());
            }
        });
    }

//    public static String encryptMD5(String data) {
//        try {
//            if (data == null) {
//                throw new RuntimeException("数据不能为NULL");
//            }
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(data.getBytes());
//            byte[] resultBytes = md5.digest();
//            String resultString = Utils.formatMsgContent(resultBytes);
//            return resultString;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static String md5Encode(String inStr) throws Exception {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    public static String shaEncode(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }




//    LinkedHashMap<String, String> params = new LinkedHashMap<>();
//        params.put(getString(R.string.sign_key),app.sharedPref.getString(getString(R.string.sign_key),""));
//        params.put("requestTime",String.valueOf(System.currentTimeMillis()/1000));
//        params.put(getString(R.string.mid_key),app.sharedPref.getString(getString(R.string.mid_key),""));
//        new NetRequest(new INet() {
//        @Override
//        public void onFailure(Object f, boolean show) {
//        }
//
//        @Override
//        public void onResponse(Object r0, Object r1) {
//        }
//    }).getRequest("home1/info",params);







}
