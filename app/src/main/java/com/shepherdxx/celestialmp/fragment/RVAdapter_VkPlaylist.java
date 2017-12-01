package com.shepherdxx.celestialmp.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shepherdxx.celestialmp.R;
import com.shepherdxx.celestialmp.extras.FragmentListener;
import com.shepherdxx.celestialmp.plailist.MyTrackInfo;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MyTrackInfo.DummyItem} and makes a call to the
 * specified {@link FragmentListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RVAdapter_VkPlaylist extends RVAdapter_MyPlaylist {

    public RVAdapter_VkPlaylist(FragmentListener listener, List<MyTrackInfo.DummyItem> items) {
        super(listener, items);
    }

    public RVAdapter_VkPlaylist(List<MyTrackInfo> items, FragmentListener listener) {
        super(items, listener);
    }



}
