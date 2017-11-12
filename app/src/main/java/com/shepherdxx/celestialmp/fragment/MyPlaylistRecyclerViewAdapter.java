package com.shepherdxx.celestialmp.fragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shepherdxx.celestialmp.R;
import com.shepherdxx.celestialmp.plailist.PlayerTrackInfo;
import com.shepherdxx.celestialmp.fragment.Fragment_Playlist.OnListFragmentInteractionListener;
import com.shepherdxx.celestialmp.fragment.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPlaylistRecyclerViewAdapter extends RecyclerView.Adapter<MyPlaylistRecyclerViewAdapter.ViewHolder> {

    String Tag= MyPlaylistRecyclerViewAdapter.class.getSimpleName();
    private final List<PlayerTrackInfo> mValues;
    private final List<PlayerTrackInfo.DummyItem> mDValues;
    private final OnListFragmentInteractionListener mListener;
    boolean dummy;
//    private final List<DummyItem> mValues;
//    private final List<PlayerTrackInfo> mTValues;


//    public MyPlaylistRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener,boolean dummy) {
//        this.dummy= dummy;
//        mValues = items;
//        mListener = listener;
//        mTValues = null;
//    }

    public MyPlaylistRecyclerViewAdapter(List<PlayerTrackInfo.DummyItem> items, OnListFragmentInteractionListener listener,boolean dummy) {
        this.dummy= dummy;
        mDValues = items;
        mListener = listener;
        mValues = null;
    }

    public MyPlaylistRecyclerViewAdapter(List<PlayerTrackInfo> items, OnListFragmentInteractionListener listener) {
        this.dummy = false;
        mValues = items;
        mListener = listener;
        mDValues = null;
        Log.i("Tag","MyPlaylistRecyclerViewAdapter created");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (dummy){
            holder.mDItem = mDValues.get(position);
            holder.mIdView.setText(mDValues.get(position).id);
            holder.mContentView.setText(mDValues.get(position).content);
        }
        else {
            Log.i("Tag","onBindViewHolder not dummy");
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(String.valueOf(position));
            holder.mContentView.setText(mValues.get(position).getmRadio());
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    if (dummy){
                        mListener.onListFragmentInteraction(holder.mDItem);}
                        else mListener.onListFragmentInteraction(holder.mItem,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dummy)
            return  mDValues.size();
        else{
            Log.i("Tag","not dummy getItemCount "+ mValues.size());
            return  mValues.size();}
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public PlayerTrackInfo mItem;
        public PlayerTrackInfo.DummyItem mDItem;
//        public DummyItem mItem;
//        public PlayerTrackInfo mpItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
