package com.tamic.linegridview;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer;
import com.squareup.picasso.Picasso;
import com.tamic.linegridview.base.BaseResponse;
import com.tamic.linegridview.model.PluginData;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private PluginAdapter mPluginAdapter;

    private PluginGridView mPluginsView;

    private BaseGridView mPluginsView2;

    private List<PluginConfigModle> mModles;

    private Picasso mPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        ViewGroup inflate = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_main, null);


        mContext = MainActivity.this;
        mPicasso = Picasso.with(mContext);
        init();
        inflate.addView(mPluginsView);

        setContentView(inflate);
        mPluginsView.refreshViews();




    }

    private void init() {

        mModles = loadPluginData();
        mPluginAdapter = new PluginAdapter(mContext, mPicasso, mModles);
        mPluginsView = new PluginGridView(mContext, mPluginAdapter);
        mPluginsView.setIsDividerEnable(true);
        mPluginsView.setColCount(3);
       // mPluginsView.setAdapter(mPluginAdapter);*/
        //mPluginsView = new PluginGridView(mContext, mPluginAdapter);
        //mPluginsView.setColumn(3);
        mPluginAdapter.notifyDataSetChanged();



    }


    private ArrayList<PluginConfigModle> loadPluginData() {

        return (ArrayList<PluginConfigModle>) getPlugins();


    }


    /**
     * 模拟数据
     */
    public List<PluginConfigModle> getPlugins() {

        String result=  getFromAssets(mContext, "plugin.json");

        TypeReference<BaseResponse<PluginData>> type = new TypeReference<BaseResponse<PluginData>>(){};
        if (TextUtils.isEmpty(result)) {
            throw new NullPointerException( "result == null");
        }
        BaseResponse<PluginData> response = JSON.parseObject(result, type);

        Log.d(">>>>>>>>>>", response.getData().getList().toString()+"");
        Log.d(">>>>>>>>>>", response.getData().getList().size()+"");



        return response.getData().getList();
    }



    // 读取assets文件夹的文件
    public static String getFromAssets(Context context, String fileName) {
        BufferedReader reader = null;
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(in));

            char[] buf = new char[1024];
            int count = 0;
            StringBuffer sb = new StringBuffer(in.available());
            while ((count = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, count);
                sb.append(readData);
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(reader);
        }

        return null;

    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
