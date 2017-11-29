package com.shepherdxx.celestialmp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shepherdxx.celestialmp.R;
import com.shepherdxx.celestialmp.extras.Constants;
import com.shepherdxx.celestialmp.plailist.PlayerTrackInfo;
import com.shepherdxx.celestialmp.plailist.RadioBD;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class Fragment_Playlist extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Fragment_Playlist() {
    }
    static int Type;

    public static Fragment_Playlist newInstance(int columnCount,int MP_Type) {
        Type=MP_Type;
        Fragment_Playlist fragment = new Fragment_Playlist();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        Log.i("Fragment_Playlist", "newInstance " + String.valueOf(MP_Type));
        return fragment;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Fragment_Playlist newInstance(int columnCount) {
        return newInstance(columnCount,0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if (Type== Constants.MP_RADIO){
                RadioBD radioBD=new RadioBD();
            recyclerView.setAdapter(new MyPlaylistRecyclerViewAdapter(radioBD.RadioList(), mListener));}
            else recyclerView.setAdapter(new MyPlaylistRecyclerViewAdapter(PlayerTrackInfo.ITEMS, mListener,true));

        }
        return view;
    }


    Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(PlayerTrackInfo.DummyItem item);
        void onListFragmentInteraction(PlayerTrackInfo item, int position);
    }
}
