package com.shepherdxx.celestialmp.plailist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.shepherdxx.celestialmp.extras.ExpView;
import com.shepherdxx.celestialmp.extras.PopUpToast;

import java.util.List;

/**
 * Created by Shepherdxx on 09.11.2017.
 */

public class MyPlayListAdapter extends
        ArrayAdapter<PlayListInfo>
{

    private OnViewClickListener mListener;

    public void setListener(OnViewClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnViewClickListener {
        void onItemClick(View v, PlayListInfo obj, int position);
    }

    public MyPlayListAdapter(@NonNull Context context, @NonNull List<PlayListInfo> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(final int position,final View convertView, final ViewGroup parent) {
        ExpView itemView = (ExpView)convertView;
        if (null == itemView)
            itemView = ExpView.inflate(parent);
        itemView.setItem(getItem(position));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    mListener.onItemClick(convertView,getItem(position),position);
                }else {
                   new PopUpToast(parent.getContext()).setMessage("Забыл");
                }
            }
        });
        return itemView;
    }

}
