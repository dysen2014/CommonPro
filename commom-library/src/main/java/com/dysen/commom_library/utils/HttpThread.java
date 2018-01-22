package com.dysen.commom_library.utils;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/7/11.
 */

public class HttpThread extends Thread {

    private static final String TAG = "HttpThread";
    public static String jsonData;
    private static final MediaType MEDIA_TYPE_MARKDOWN  = MediaType.parse("application/json; charset=utf-8");

    public static void sendRequestGet(final String url, final Handler handler){
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(url)
//                .url("https://github.com/hongyangAndroid")
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(Call call, Response response) {
                LogUtils.d("http", "sendRequest:"+String.valueOf(response));

                String tmpBody = null;
                JSONObject dataJsonObject = null;
                JSONArray dataJsonArray = null;
                try {
//                    tmpBody = getJsonString(response.body().byteStream(), "UTF-8");
                    tmpBody = response.body().string();
                    LogUtils.v("tmpBody==="+tmpBody);
                    if (tmpBody.startsWith("[")) {
                        dataJsonArray = new JSONArray(tmpBody);
                    } else {
                        dataJsonObject = new JSONObject(tmpBody);
                    }

                    if (tmpBody.contains("[]"))
                        handler.sendEmptyMessage(-100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LogUtils.d("http", "Response completed: "+dataJsonObject);
                Message msg = new Message();
                msg.obj = dataJsonObject;
                handler.sendMessage(msg);
            }
        });
    }

    public static void sendRequestGet(final String url, final String params, final Handler handler, final int warnType){
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(url + params)
//                .url("https://github.com/hongyangAndroid")
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(Call call, Response response) {
                LogUtils.d("http", "sendRequest:"+String.valueOf(response));

                String tmpBody = null;
                JSONObject dataJsonObject = null;
                JSONArray dataJsonArray = null;
                try {
                    tmpBody = getJsonString(response.body().byteStream(), "UTF-8");
                    LogUtils.v("tmpBody==="+tmpBody);
                    if (tmpBody.startsWith("[")) {
                        dataJsonArray = new JSONArray(tmpBody);
                    } else {
                        dataJsonObject = new JSONObject(tmpBody);
                    }

                    if (tmpBody.contains("[]"))
                        handler.sendEmptyMessage(-100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LogUtils.d("http", "Response completed: "+dataJsonObject);
                Message msg = new Message();
                msg.what = warnType;
                msg.obj = dataJsonObject;
                handler.sendMessage(msg);
            }
        });
    }
    public static String getJsonString(InputStream inputStream, String type) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, type);

        int event = parser.getEventType();// 产生第一个事件
        String tmpJsonString = null;
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
                    break;
                case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
                    if ("getJSONReturn".equals(parser.getName())) {// 判断开始标签元素是否是student
                        tmpJsonString = parser.nextText();// 得到student标签的属性值，并设置student的id
                    }
                    break;
                case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
                    break;
            }
            event = parser.next();// 进入下一个元素并触发相应事件
        }
        return tmpJsonString;
    }
    /**
     *  /**
     * 发送请求
     * @param url
     * @param obj
     * @param handler
     */
    public static void sendRequestWithOkHttp(final String url, final JSONObject obj, final Handler handler) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN,  String.valueOf(obj));
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)//请求地址
                            .post(body)//post 请求参数
                            .build();
                    LogUtils.d("http", "sendRequest:"+ url + obj.toString());
                    Response response = client.newCall(request).execute();
//                    LogUtils.d("http", "response:"+String.valueOf(response));
                    String responseData = response.body().string();

                    LogUtils.d("http", "Response completed: " + responseData);

                    Message msg = new Message();
                            msg.obj = responseData;
                    handler.sendMessage(msg);
                    JSONObject json = parseJSON(responseData);
                    if (json.has("array") && json.toString().contains("[]")){
                        handler.sendEmptyMessage(-100);
                    }

                } catch (Exception e) {
                    LogUtils.d("http", e+"\t\tsokect time out");
                    handler.sendEmptyMessage(-1);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static JSONObject parseJSON(String jsonData, String name) throws JSONException {
        if (!TextUtils.isEmpty(jsonData) || jsonData != null){
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject json = jsonObject.getJSONObject(name);
            LogUtils.d("http parse", "jsonObject: " + json.toString());

            return json;
        }else
            return null;
    }

    public static JSONObject parseJSON(String jsonData) throws JSONException {
        if (!TextUtils.isEmpty(jsonData) || jsonData != null){
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject json = null;
            if (jsonObject.has("ResponseParams")) {
                json = jsonObject.getJSONObject("ResponseParams");
                LogUtils.d("http parse", "jsonObject: " + jsonObject.getJSONObject("ResponseParams").toString());
            }else
            return null;
            return json;
        }else
            return null;
    }

    /**
     * 解析 返回单组数据
     * @param jsonData
     * @return
     * @throws JSONException
     */
    public static String parseJSONWithGson(String jsonData) throws JSONException{
        if (!TextUtils.isEmpty(jsonData) || jsonData != null){
            JSONObject jsonObject = null;

                jsonObject = new JSONObject(jsonData);
            String jsonArray = null;

                jsonArray = jsonObject.getJSONObject("ResponseParams").getJSONArray("array").toString();

            LogUtils.d("http parse", "jsonObject: " + jsonArray);

            return jsonArray;
        }else
            return null;
    }

    /**
     * 解析 返回多组数据
     * @param jsonData
     * @param handler
     * @return
     * @throws JSONException
     */
    public static String parseJSONWithGson(String jsonData, Handler handler) throws JSONException {
        if (!TextUtils.isEmpty(jsonData) || jsonData != null) {
            JSONObject jsonObject = new JSONObject(jsonData);
            String jsonArrayHeader = null;
            String jsonArray = null;

                    jsonArrayHeader = jsonObject.getJSONObject("ResponseParams").getJSONArray("header").toString();
                    LogUtils.d("http parse", jsonArrayHeader+"===============json parse==============");

                    jsonArray = jsonObject.getJSONObject("ResponseParams").getJSONArray("array").toString();
                    LogUtils.d("http parse", "===============json parse==============" +  jsonArray);


            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("header", jsonArrayHeader);
            bundle.putString("data", jsonArray);
            msg.setData(bundle);
            handler.sendMessage(msg);
            return jsonArrayHeader;
        } else
            return null;
    }

    /**
     * json字符串 转成实体类
     * @param <T>
     * @param jsonData
     * @return
     */
    public static <T> List<T> parseList(String jsonData, Class<T> cls) {

        if (!TextUtils.isEmpty(jsonData) || jsonData != null) {
            Gson gson = new Gson();
            List<T> list = new ArrayList<T>();
            JsonArray arry = new JsonParser().parse(jsonData).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
            return list ;

        } else
            return null;
    }

    private String responseData = null;
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * post请求方式
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public String sendRequestWithOkHttp(final String url, final JSONObject json) throws IOException {

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json.toString());
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).post(body).build();
                    Log.d(TAG, "request: "+request);
                    Response response = client.newCall(request).execute();
                    //Log.d(TAG, "body: "+response.body());
                    responseData = response.body().string();
                    Log.d(TAG, "responseData: "+responseData);

                    //parseJSONWithGson(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        return responseData;
    }

    /**
     * url请求
     * @param url
     * @return
     * @throws IOException
     */
    public String sendRequestWithOkHttp(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Log.d(TAG, "request: "+request);
        Response response = client.newCall(request).execute();
        //Log.d(TAG, "body: "+response.body());
        String responseData = response.body().string();
        Log.d(TAG, "responseData: "+responseData);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public static void sendRequestWithOkHttp(final String url, final JSONObject jsonObject, final Handler handler, final int index) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN,  String.valueOf(jsonObject));
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)//请求地址
                            .post(body)//post 请求参数
                            .build();
                    LogUtils.d("http", "sendRequest:"+ url + jsonObject.toString());
                    Response response = client.newCall(request).execute();
//                    LogUtils.d("http", "response:"+String.valueOf(response));
                    String responseData = response.body().string();

                    LogUtils.d("http", "Response completed: " + responseData);
                    JSONObject json = parseJSON(responseData);
                    if (json.has("array") && json.toString().contains("[]")){
                        handler.sendEmptyMessage(-100);
                    }
                    Message msg = new Message();
                             msg.obj = responseData;
                            msg.what = index;

                    handler.sendMessage(msg);
                } catch (Exception e) {
                    LogUtils.d("http", "sokect time out");
                    handler.sendEmptyMessage(-1);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
