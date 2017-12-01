package com.shepherdxx.celestialmp.fragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class RVAdapter_MyPlaylist extends RecyclerView.Adapter<RVAdapter_MyPlaylist.ViewHolder> {

    private String Log_Tag= RVAdapter_MyPlaylist.class.getSimpleName();
    private final List<MyTrackInfo> mValues;
    private final List<MyTrackInfo.DummyItem> mDValues;
    private final FragmentListener mListener;
    boolean dummy;

    public RVAdapter_MyPlaylist(FragmentListener listener, List<MyTrackInfo.DummyItem> items) {
        this.dummy= true;
        mDValues = items;
        mListener = listener;
        mValues = null;
    }

    public RVAdapter_MyPlaylist(List<MyTrackInfo> items, FragmentListener listener) {
        this.dummy = false;
        mValues = items;
        mListener = listener;
        mDValues = null;
        Log.i("Tag","RVAdapter_MyPlaylist created");
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
//            Log.i("Tag","onBindViewHolder not dummy");
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(String.valueOf(position+1));
            holder.mContentView.setText(mValues.get(position).getTitle());
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    if (dummy){
                        mListener.onTrackClick(holder.mDItem);}
                        else mListener.onTrackClick(holder.mItem,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dummy)
            return  mDValues.size();
        else{
//            Log.i("Tag","not dummy getItemCount "+ mValues.size());
            return  mValues.size();}
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mIdView;
        private final TextView mContentView;
        private MyTrackInfo mItem;
        private MyTrackInfo.DummyItem mDItem;

        private ViewHolder(View view) {
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
