package com.shepherdxx.celestialmp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shepherdxx.celestialmp.MP_BackgroundService;
import com.shepherdxx.celestialmp.R;
import com.shepherdxx.celestialmp.extras.Constants;
import com.shepherdxx.celestialmp.extras.FragmentListener;
import com.shepherdxx.celestialmp.plailist.PlayListInfo;
import com.shepherdxx.celestialmp.plailist.PlayListTrue;
import com.shepherdxx.celestialmp.plailist.MyTrackInfo;

import java.util.ArrayList;

import static com.shepherdxx.celestialmp.extras.Constants.MP_STARTED;
import static com.shepherdxx.celestialmp.extras.Constants.MP_STOPED;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link FragmentListener}
 * interface.
 */
public class Fragment_Playlist extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT    = "column-count";
    private static final String ARG_ID              = "playlist_id";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private FragmentListener mListener;
    private String Log_Tag = Fragment_Playlist.class.getSimpleName();
    Context mContext;
    RecyclerView recyclerView = null;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Fragment_Playlist() {
    }
    private int playlistId;

    public static Fragment_Playlist newInstance(int columnCount,int playlistId) {
        Fragment_Playlist fragment = new Fragment_Playlist();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_ID, playlistId);
        fragment.setArguments(args);
        Log.i("Fragment_Playlist", "newInstance " + String.valueOf(playlistId));
        return fragment;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Fragment_Playlist newInstance(int columnCount) {
        return newInstance(columnCount,Constants.MP_EMPTY);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            playlistId = getArguments().getInt(ARG_ID);
        }
        setBroadcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            if (playlistId == Constants.MP_EMPTY){
                recyclerView.setAdapter(
                        new RVAdapter_MyPlaylist(mListener, MyTrackInfo.ITEMS));
            }
            else {
                PlayListInfo playListInfo=new PlayListTrue(context).createPlaylist(playlistId);
                try {
                    ArrayList<MyTrackInfo> a = playListInfo.audioTracks;
                    recyclerView.setAdapter(new RVAdapter_MyPlaylist(a, mListener));
                    scrollTo(recyclerView);
                }catch (NullPointerException e){
                    e.printStackTrace();
                    Log.e(Log_Tag,"Ошибка в плейлисте" + playlistId);
                }
            }
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            mListener = (FragmentListener) context;
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

    private void scrollTo(RecyclerView rw){
        if (rw != null) {
            int id = Constants.MP_EMPTY;
            if (currentTrackInfo() != null)
                id = currentTrackInfo().getPlaylistId();
            if (playlistId == id)
                rw.scrollToPosition(currentTrackPos());
        }
    }

    private MyTrackInfo currentTrackInfo() {
        MyTrackInfo track = null;
        if (MP_BackgroundService.hasInstance()) {
            MP_BackgroundService service = MP_BackgroundService.get(getContext());
            track = service.getTrackInfo();
        }
        return track;
    }

    private int currentTrackPos() {
        int position = 0;
        if (MP_BackgroundService.hasInstance()) {
            MP_BackgroundService service = MP_BackgroundService.get(getContext());
            position = service.getmCurPosition();
        }
        return position;
    }

    private void setBroadcastReceiver() {
        BroadcastReceiver MP_start = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String action = intent.getAction();
                    if (action != null)
                        switch (action) {
                            case MP_STARTED:
                                scrollTo(recyclerView);
                                break;
                        }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };
        //on Play
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MP_STARTED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(MP_start, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        scrollTo(recyclerView);
    }
}
