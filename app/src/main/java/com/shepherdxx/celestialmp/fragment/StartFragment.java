package com.shepherdxx.celestialmp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.shepherdxx.celestialmp.PreService;
import com.shepherdxx.celestialmp.R;
import com.shepherdxx.celestialmp.extras.Constants;
import com.shepherdxx.celestialmp.extras.Debug;
import com.shepherdxx.celestialmp.extras.FragmentListener;
import com.shepherdxx.celestialmp.extras.PopUpToast;
import com.shepherdxx.celestialmp.plailist.MyPlayListAdapter;
import com.shepherdxx.celestialmp.plailist.MyPlayListAdapter.OnViewClickListener;
import com.shepherdxx.celestialmp.plailist.PlayListInfo;
import com.shepherdxx.celestialmp.plailist.PlayListTrue;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListener} interface
 * to handle interaction events.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment
        extends Fragment
        implements OnViewClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentListener mListener;

    public StartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static StartFragment newInstance() {
        return newInstance(null,null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    Activity activity;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_start, container, false);
        mainView.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(getContext());
                d.setTitle(R.string.new_playlist);
                View view = getLayoutInflater().inflate(R.layout.dialog_window,null);
                final EditText et = view.findViewById(R.id.dialog_et);
                view.findViewById(R.id.dialog_create).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = et.getText().toString().trim();
                        popUpToast= new PopUpToast(getContext());
                        if (text.isEmpty()) popUpToast.setMessage("Введите название");
                        else{
                            addNewPlaylist(text);
                        popUpToast.setMessage(text);
                        d.dismiss();}
                    }
                });
                view.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.setContentView(view);
                d.show();
            }
        });

        mainView.findViewById(R.id.button_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, Debug.class));
            }});
        try {activity=getActivity();
            findView(mainView);
        }catch (IllegalArgumentException e){e.printStackTrace();}
        return mainView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            mListener = (FragmentListener) context;
            this.context=context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    String Log_tag = StartFragment.class.getSimpleName();
    PopUpToast popUpToast;

    @Override
    public void onItemClick(View v, final PlayListInfo obj, int position) {
        mListener.onPlaylistClick(obj);
        getActivity().startService(
                PreService.startBGService(
                        getContext(),
                        (int)obj.getPlaylistId(),
                        0)
                );
        Log.i(Log_tag,String.valueOf(position));
    }

    /**
     * Список плейлистов
     */
    ListView plScroll;
    ArrayList<PlayListInfo> infoList=new ArrayList<>();
    MyPlayListAdapter myPlayListAdapter;
    void findView(View v){
        plScroll = v.findViewById(R.id.play_list_lw);
        if (infoList.isEmpty()) {
            infoList.add(PlayListInfo.All());
            infoList.add(PlayListInfo.Cache());
            morePlaylist(infoList);
            getAndroidPlaylist(infoList);
        }
        setMyAdapter(infoList);
    }

    public void setMyAdapter(final ArrayList<PlayListInfo> pLI) {

        myPlayListAdapter = new MyPlayListAdapter(context,pLI);
        myPlayListAdapter.setListener(this);
        plScroll.setAdapter(myPlayListAdapter);

    }

    private void morePlaylist(ArrayList<PlayListInfo> PLI){
//        Cursor cursor = PlayList.queryPlaylists(getContext());
//        if (cursor != null) {
//            cursor.moveToFirst();
//            do{
//                long id = cursor.getLong(0);
//                String name= cursor.getString(1);
//                PlayListInfo playListInfo=new PlayListInfo(id,name);
//                PLI.add(playListInfo);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
    }

    /////  //    //   //////
    //      //  //    //   //
    ////      //      //////
    //      //  //    //
    ////  //      //  //

    public static long hash63(String str) {
        if (str == null)
            return 0;

        long hash = 0;
        int len = str.length();
        for (int i = 0; i < len ; i++) {
            hash = 31*hash + str.charAt(i);
        }
        return (hash < 0 ? hash*-1 : hash);
    }


    public void addNewPlaylist(String newPlaylist) {
        ContentResolver resolver = getContext().getContentResolver();
        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Audio.Playlists.NAME, newPlaylist);
        values.put(MediaStore.Audio.Playlists._ID, hash63(newPlaylist));
        Log.i("HashTitle"," " + hash63(newPlaylist));
        resolver.insert(Constants.PLAYLIST_URI, values);
    }

    public void getAndroidPlaylist(ArrayList<PlayListInfo> PLI) {
        try {Cursor cursor = PlayListTrue.getAndroidPlaylistCursor(getContext(), Constants.PLAYLIST_URI);
            if (cursor != null) {
                cursor.moveToFirst();
                try {
                    do {
                        long id = cursor.getLong(0);
                        String name = cursor.getString(1);
                        Log.i("getAndroidPlaylist", name + File.pathSeparator + id);
                        PlayListInfo playListInfo = new PlayListInfo(id, name);
                        PLI.add(playListInfo);
                    } while (cursor.moveToNext());
                    cursor.close();
                } catch (CursorIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    Log.i(Log_tag, "getAndroidPlaylist failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new PopUpToast(activity).setMessage("Диман поменяй телефон");
        }
    }


}
