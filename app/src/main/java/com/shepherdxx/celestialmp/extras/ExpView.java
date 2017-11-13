package com.shepherdxx.celestialmp.extras;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shepherdxx.celestialmp.R;
import com.shepherdxx.celestialmp.plailist.PlayListInfo;

/**
 * Created by Shepherdxx on 08.11.2017.
 */

public class ExpView extends RelativeLayout {

    public ExpView(Context context) {
        this(context,null);
    }

    public ExpView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_view_children, this, true);
        setupChildren();
    }


    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private ImageView mImageView;

    public static ExpView inflate(ViewGroup parent) {
        return (ExpView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
    }

    private void setupChildren() {
        mTitleTextView = (TextView) findViewById(R.id.item_titleTextView);
        mDescriptionTextView = (TextView) findViewById(R.id.item_descriptionTextView);
        mImageView = (ImageView) findViewById(R.id.item_imageView);
    }

    public void setItem(PlayListInfo item) {
        mTitleTextView.setText(item.getName());
        String descript = "Pl №"+String.valueOf(item.getPlaylistId());
        mDescriptionTextView.setText(descript);
        mImageView.setImageResource(R.drawable.celestial_logo);
    }
}
