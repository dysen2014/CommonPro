package com.dysen.commom_library.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dysen on 2017/10/11.
 *
 * @Info
 */

public class PopWindowUtils {

    public static void handleListView(Context context, RecyclerView recyclerView, List<String> listData){
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

    }

    public static List<String> mockData(){
        List<String> data = new ArrayList<>();
        for (int i=0;i<100;i++){
            data.add("Item:"+i);
        }
        return data;
    }
}
