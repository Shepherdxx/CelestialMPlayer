package com.shepherdxx.celestialmp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shepherdxx.celestialmp.R;
import com.shepherdxx.celestialmp.extras.ControlButtonsExtras;
import com.shepherdxx.celestialmp.extras.ControlPanelButtonListener;


public class Fragment_ControlButtonsDefault
        extends Fragment
{

    private static String Log_Tag= Fragment_ControlButtonsDefault.class.getSimpleName();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivity=activity;
            this.listener = (ControlPanelButtonListener)activity;
        }catch (ClassCastException e){
            e.printStackTrace();
            }
    }



    private ControlPanelButtonListener listener;

    ControlButtonsExtras sbe;
    Activity mActivity;
    Handler myHandler= new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control_buttons, container, false);
        setSBE(view);
        return view;
    }

    void setSBE(View view){
        sbe= ControlButtonsExtras.setUpdateSongTime(mActivity,myHandler,view,listener);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("LifeCycle onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        try {sbe.run();
            sbe.Title();
        }catch (NullPointerException e) {
            e.printStackTrace();
            Log.i(Log_Tag, "onResume sbe.error" );
        }
        System.out.println("LifeCycle onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("LifeCycle onPause");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("LifeCycle onDetach");
    }


}


