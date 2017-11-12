package com.shepherdxx.celestialmp.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shepherdxx.celestialmp.R;
import com.shepherdxx.celestialmp.extras.PopUpToast;
import com.shepherdxx.celestialmp.plailist.MyPlayListAdapter;
import com.shepherdxx.celestialmp.plailist.MyPlayListAdapter.OnViewClicklListener;
import com.shepherdxx.celestialmp.plailist.PlayListInfo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment
        extends Fragment
        implements OnViewClicklListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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

        try {activity=getActivity();
            findView(mainView);
        }catch (IllegalArgumentException e){e.printStackTrace();}
        return mainView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
//    @Override
//    public void onItemClick(ImageView im, View v, PlayListInfo obj, int position) {
//
//        popUpToast=new PopUpToast(activity.getBaseContext());
//        popUpToast.setMessage(obj.getName() +  " " +String.valueOf(obj.getPlaylistId()) );
//    }

    @Override
    public void onItemClick(View v, PlayListInfo obj, int position) {
        Log.i(Log_tag,String.valueOf(position));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
        }
       setMyAdapter(infoList);
    }

    public void setMyAdapter(final ArrayList<PlayListInfo> pLI) {

        myPlayListAdapter = new MyPlayListAdapter(context,pLI,this);

        plScroll.setAdapter(myPlayListAdapter);

    }


}
