package com.shepherdxx.celestialmp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.shepherdxx.celestialmp.PreService;
import com.shepherdxx.celestialmp.R;
import com.shepherdxx.celestialmp.extras.FragmentListener;
import com.shepherdxx.celestialmp.extras.PopUpToast;
import com.shepherdxx.celestialmp.medialibrary.MediaLibrary;
import com.shepherdxx.celestialmp.plailist.MyPlayListAdapter;
import com.shepherdxx.celestialmp.plailist.MyPlayListAdapter.OnViewClickListener;
import com.shepherdxx.celestialmp.plailist.PlayListInfo;
import com.shepherdxx.celestialmp.playlist_imp.PlayList;

import java.io.File;
import java.util.ArrayList;

import static com.shepherdxx.celestialmp.extras.Constants.MP_EMPTY;


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
                            addnewPlaylist(text);
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
                        1)
                );
        Log.i(Log_tag,String.valueOf(position));
    }

    /**
     * Список плейлистов
     */
    ListView plScroll;
    ArrayAdapter plAdapter;
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
        Cursor cursor = PlayList.queryPlaylists(getContext());
        if (cursor != null) {
            if (cursor.moveToNext()){
                long id = cursor.getLong(0);
                String name= cursor.getString(1);
                PlayListInfo playListInfo=new PlayListInfo(id,name);
                PLI.add(playListInfo);
            }
            cursor.close();
        }
    }

    /////  //    //   //////
    //      //  //    //   //
    ////      //      //////
    //      //  //    //
    ////  //      //  //


    Uri uri= MediaStore.Audio.Playlists.getContentUri("external");
    public void addnewPlaylist(String newplaylist) {
        ContentResolver resolver = getContext().getContentResolver();
        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Audio.Playlists.NAME, newplaylist);
        resolver.insert(uri, values);
    }

    public void getAndroidPlaylist(ArrayList<PlayListInfo> PLI) {
        Cursor cursor = getandroidPlaylistcursor(getContext());
        if (cursor != null) {
            if (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                Log.i("getAndroidPlaylist", name + File.pathSeparator + id);
                PlayListInfo playListInfo = new PlayListInfo(id, name);
                PLI.add(playListInfo);
            }
            cursor.close();
        }
    }

    public Cursor getandroidPlaylistcursor(Context context) {
        ContentResolver resolver = context.getContentResolver();
        final String id = MediaStore.Audio.Playlists._ID;
        final String name = MediaStore.Audio.Playlists.NAME;
        final String[] columns = { id, name };
        final String criteria = null;
        return  resolver.query(uri, columns, criteria, null,
                name + " ASC");

    }
}
