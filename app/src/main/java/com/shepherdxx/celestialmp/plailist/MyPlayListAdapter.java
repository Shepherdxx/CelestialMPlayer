package com.shepherdxx.celestialmp.plailist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;

import com.shepherdxx.celestialmp.R;

import java.util.ArrayList;

/**
 * Created by Shepherdxx on 09.11.2017.
 */

public class MyPlayListAdapter extends
        ArrayAdapter<String> {

    public MyPlayListAdapter(@NonNull Context context, @NonNull ArrayList<PlayListInfo> playListInfoArray,OnViewClicklListener listener ) {
        this (context,playListInfoArray);
        setmOnViewClicklListener(listener);
    }

    public MyPlayListAdapter(@NonNull Context context, @NonNull ArrayList<PlayListInfo> playListInfoArray ) {
        this (context, R.layout.adapter_view,R.id.song_name);
        addAll(pl_scroll(playListInfoArray));
    }

    public MyPlayListAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.context=context;
    }

    Context context;
    private ArrayList<PlayListInfo> scroll;
    private ArrayList<String> pl_scroll(ArrayList<PlayListInfo> playListInfoArray){
        scroll=playListInfoArray;
        ArrayList<String> pl_names=new ArrayList<>();
        for (int i=0; i<playListInfoArray.size();i++){
            pl_names.add(playListInfoArray.get(i).getName());
        }
        return pl_names;
    };

    public OnViewClicklListener getmOnViewClicklListener() {
        return mOnViewClicklListener;
    }

    public void setmOnViewClicklListener(OnViewClicklListener mOnViewClicklListener) {
        this.mOnViewClicklListener = mOnViewClicklListener;
    }

    OnViewClicklListener mOnViewClicklListener;


    public interface OnViewClicklListener{
        void onItemClick(View v, PlayListInfo obj, int position);
    }


    @Override
    public void addAll(String... items) {
        super.addAll(items);
    }
}
