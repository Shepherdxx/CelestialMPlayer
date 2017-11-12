package com.shepherdxx.celestialmp.extras;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shepherdxx.celestialmp.MP_BackgroundService;
import com.shepherdxx.celestialmp.R;

import java.util.concurrent.TimeUnit;

import static com.shepherdxx.celestialmp.MP_BackgroundService.mPlayer;
import static com.shepherdxx.celestialmp.extras.Constants.MP_BACK;
import static com.shepherdxx.celestialmp.extras.Constants.MP_EMPTY;
import static com.shepherdxx.celestialmp.extras.Constants.MP_FOWARD;
import static com.shepherdxx.celestialmp.extras.Constants.MP_NEXT;
import static com.shepherdxx.celestialmp.extras.Constants.MP_PAUSE;
import static com.shepherdxx.celestialmp.extras.Constants.MP_RADIO;
import static com.shepherdxx.celestialmp.extras.Constants.MP_SD_U;
import static com.shepherdxx.celestialmp.extras.Constants.MP_STARTED;
import static com.shepherdxx.celestialmp.extras.Constants.MP_STOPED;
import static com.shepherdxx.celestialmp.extras.Constants.MP_TOWARD;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Shepherdxx on 26.09.2017.
 */

public class ControlButtonsExtras implements
        Runnable
    ,View.OnClickListener
{
//
//    public ControlButtonsExtras(
//            Activity activity,
//            Handler myHandler,
//            ImageButton btPlay,
//            SeekBar seekBar,
//            TextView fTime,
//            TextView sTime,
//            ControlPanelButtonListener listener
//
//    ){
//        this.activity = activity;
//        this.seekBar=seekBar;
//        this.sTime=sTime;
//        this.fTime = fTime;
//        this.myHandler = myHandler;
//        this.btPlay = btPlay;
//        if (seekBar!=null) seekBarListen(seekBar);
//        this.listener=listener;
//        setBroadcastReceiver();
//    }



    private View view;
    private int mType;
    private SeekBar seekBar;
    private Handler myHandler;
    private TextView sTime;
    private TextView fTime;
    private TextView rTitle;
    private Activity activity;
    private boolean oneTimeOnly = true;
    private ControlPanelButtonListener listener;
    private ImageButton btPlay;
    private String Log_Tag=ControlButtonsExtras.class.getSimpleName();
    private long startTime, finalTime;

//    //    Отображение времени
//    public static ControlButtonsExtras setUpdateSongTime(Activity activity,
//                                                         final Handler myHandler,
//                                                         final ImageButton btPlay,
//                                                         final SeekBar seekBar,
//                                                         final TextView fTime,
//                                                         final TextView sTime,
//                                                         final ControlPanelButtonListener listener
//                                                 ) {
//
//        return new ControlButtonsExtras(activity,myHandler,btPlay,seekBar,fTime,sTime,listener);
//    }
//
//    public static ControlButtonsExtras setUpdateSongTime(Activity activity,
//                                                         final Handler myHandler,
//                                                         final ImageButton btPlay,
//                                                         final SeekBar seekBar,
//                                                         final TextView fTime
//    ) {
//        //encoded song listener
//        return new ControlButtonsExtras(activity,myHandler,btPlay,seekBar,fTime,null,null);
//    }
//
//    public static ControlButtonsExtras setUpdateSongTime(Activity activity,
//                                                         final Handler myHandler,
//                                                         final ImageButton btPlay,
//                                                         final ControlPanelButtonListener listener
//    ) {
//        //radio control panel
//        return new ControlButtonsExtras(activity,myHandler,btPlay,null,null,null,listener);
//    }


    //Поведение ползунка
    private void seekBarListen(SeekBar seekBar){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayer.seekTo(seekBar.getProgress());
            }
        });
    }


    @Override
    public void run() {
        setVisibility();
        if (mPlayer!=null) {
            startTime = mPlayer.getCurrentPosition();
            finalTime = mPlayer.getDuration();
                OnAirEvent();
            if (finalTime <= 0 || startTime < 0) {
                if (seekBar!=null) seekBar.setVisibility(View.INVISIBLE);
                if (fTime != null) fTime.setVisibility(View.INVISIBLE);
                if (sTime != null) sTime.setVisibility(View.INVISIBLE);
            } else {
                seekBarProgress();
            }
        }

        if (Build.VERSION.SDK_INT >= 21) {
//            Log.i("encodedPlay",String.valueOf(activity.isFinishing())+ File.pathSeparator +
//                            String.valueOf(activity.isChangingConfigurations())+ File.pathSeparator+
//                    String.valueOf(activity.requestVisibleBehind(true))+ File.pathSeparator +
//                    String.valueOf(activity.isDestroyed()));
            if (!activity.requestVisibleBehind(true))myHandler.removeCallbacks(this);
            else myHandler.postDelayed(this, 500);
        }else{
//            Log.i("encodedPlay",String.valueOf(activity.isFinishing())+ File.pathSeparator +
//                    String.valueOf(activity.isChangingConfigurations())+ File.pathSeparator);
            if (activity.isFinishing())myHandler.removeCallbacks(this);
            myHandler.postDelayed(this, 500);
        }
    }

    private void seekBarProgress(){
        if (seekBar != null) {
            if (startTime < finalTime) {
                startTime = mPlayer.getCurrentPosition();
                long mins = MILLISECONDS.toMinutes(startTime);
                long secs = MILLISECONDS.toSeconds(startTime) -
                        TimeUnit.MINUTES.toSeconds(MILLISECONDS.toMinutes(startTime));
                seekBar.setProgress((int) startTime);
                if (sTime != null) sTime.setText(String.format("%d:%d", mins, secs));
                Log.i("seekBarInit", String.format("%d:%d", mins, secs));
            }
        }
    }

    private void OnAirEvent() {
        try{
            if (mPlayer.getRequest()){
//                setLL_Visibility();
                oneTimeOnly=false;
            }
            if (mPlayer.isPlaying()) {
                btPlay.setImageResource(R.drawable.ic_pause_circle_outline_black_48dp);
                if (listener!=null) listener.toolBarButton(true);
            }else{
                btPlay.setImageResource(R.drawable.ic_play_circle_outline_black_48dp);
                if (listener!=null) listener.toolBarButton(false);
            }
        }catch (
                NullPointerException e){e.printStackTrace();}
//        if (seekBar!=null && seekBar.getRootView().getVisibility()== View.GONE){
//            seekBar.getRootView().setVisibility(View.VISIBLE);
//            mPlayer.setRequest(true);
//        }
    }

//    private void setLL_Visibility(){
//        if (visibilityCheck){visibilityCheck=false;
//            try {
//                findView();
////                if (mPlayer != null) {
////                    LL.setVisibility(View.VISIBLE);
////                    Log.i(Log_Tag, "LL_Visibility == VISIBLE");
////                } else {
////                    LL.setVisibility(View.GONE);
////                    Log.i(Log_Tag, "LL_Visibility == GONE");
////                }
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//                Log.i(Log_Tag, "setLL_Visibility is failed");
//            }
//        }
//    };

    public void Title () {
        oneTimeOnly=true;
        try {
        if (mPlayer!=null) {
            String SongTitle = mPlayer.getSongName();

            if (oneTimeOnly && listener != null) {
                listener.getTrackName(SongTitle);
                rTitle.setText(SongTitle);
            }
            long finalTime = mPlayer.getDuration();
            long min = MILLISECONDS.toMinutes(finalTime);
            long sec = MILLISECONDS.toSeconds(finalTime) -
                    TimeUnit.MINUTES.toSeconds(MILLISECONDS.toMinutes(finalTime));
            oneTimeOnly = false;
            seekBar.setMax((int) finalTime);
            fTime.setText(String.format("%d:%d", min, sec));
            Log.i("seekBarInit", String.format("%d:%d", min, sec));
            Log.i("Title", SongTitle);
        }
        }catch (NullPointerException e){e.printStackTrace();}
    }

    public void stop(){
        myHandler.removeCallbacks(this);OnAirEvent();
    }

    private void setBroadcastReceiver(){
        BroadcastReceiver MP_start = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    switch (intent.getAction()){
                        case MP_STARTED:
                            Log.i(Log_Tag,"BroadcastReceiver recived MP_STARTED");
//                        Title ();
                            visibilityCheck=true;
                            mType=MP_Type();
                            run();
                            Title();
                            break;
                        case MP_STOPED:
                            Log.i(Log_Tag, "BroadcastReceiver recived MP_STOPED");
                            stop();
                            visibilityCheck=true;
                            break;
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        };
        //on Play
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MP_STARTED);
        intentFilter.addAction(MP_STOPED);
        LocalBroadcastManager.getInstance(activity).registerReceiver(MP_start, intentFilter);
    }

    public boolean visibilityCheck=true;

    public ControlButtonsExtras(
            Activity activity,
            Handler myHandler,
            View outerView,
            ControlPanelButtonListener listener

    ){
        this.activity = activity;
        this.myHandler = myHandler;
        this.mType=MP_Type();
        this.view=outerView;
        this.listener=listener;
        setBroadcastReceiver();
        findView();
    }


    //Test
    public static ControlButtonsExtras setUpdateSongTime(Activity activity,
                                                         final Handler myHandler,
                                                         final View view,
                                                         final ControlPanelButtonListener listener
    ) {
        //test control panel
        return new ControlButtonsExtras(activity,myHandler,view,listener);
    }

    private ImageButton
//            btPlay,
            btFw, btTw, btNext, btPrev;
//    private TextView sTime, fTime;
    private LinearLayout LL;
    private int MPState;

    private void findView() {
        //Они будут на всех панелях
        LL = view.findViewById(R.id.fr_control_layout);

        btNext = view.findViewById(R.id.fr_next_song);
        btPrev = view.findViewById(R.id.fr_back_song);
        btPlay = view.findViewById(R.id.fr_pausePlay);

        btPlay.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btPrev.setOnClickListener(this);

        seekBar = view.findViewById(R.id.fr_sb_seekBar);

        btFw = view.findViewById(R.id.fr_forward);
        btTw = view.findViewById(R.id.fr_toward);

        btFw.setOnClickListener(this);
        btTw.setOnClickListener(this);

        sTime = view.findViewById(R.id.fr_st_time);
        fTime = view.findViewById(R.id.fr_tl_time);

        setVisibility();
    }

    private void setVisibility() {
        if (visibilityCheck) {
            visibilityCheck = false;
            try {
                switch (mType) {
                    case MP_RADIO:
                        LL.setVisibility(View.VISIBLE);
                        Log.i(Log_Tag, "LL_Visibility == VISIBLE");

                        btFw.setVisibility(View.GONE);
                        btTw.setVisibility(View.GONE);
                        sTime.setVisibility(View.GONE);
                        fTime.setVisibility(View.GONE);

                        seekBar.setVisibility(View.GONE);
                        seekBar = null;

                        rTitle = view.findViewById(R.id.radio_title);
                        rTitle.setVisibility(View.VISIBLE);
                        Log.i(Log_Tag, "findView" + String.valueOf(sTime.getVisibility())
                                + " " + String.valueOf(fTime.getVisibility()));
                        break;

                    case MP_SD_U:
                        LL.setVisibility(View.VISIBLE);
                        Log.i(Log_Tag, "LL_Visibility == VISIBLE");
                        break;

                    case MP_EMPTY:
                        LL.setVisibility(View.GONE);
                        Log.i(Log_Tag, "LL_Visibility == GONE");
                        break;
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.i(Log_Tag, "setLL_Visibility is failed");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mPlayer!=null){
            switch (v.getId()) {
                case R.id.fr_pausePlay:
                    MPState = MP_PAUSE;
                    break;
                case R.id.fr_forward:
                    MPState = MP_FOWARD;
                    break;
                case R.id.fr_toward:
                    MPState = MP_TOWARD;
                    break;
                case R.id.fr_next_song:
                    MPState = MP_NEXT;
                    break;
                case R.id.fr_back_song:
                    MPState = MP_BACK;
                    break;
            }
            activity.startService(CPIntent(MPState));
        }else toastMessage("Нечего воспроизводить");
    }

    private Intent CPIntent(int state){
        Intent i= new Intent(activity,MP_BackgroundService.class);;
        Bundle Bondiana = new Bundle();
        Bondiana.putInt("MPType", 0);
        Bondiana.putInt("MPState",state);
        i.putExtra("Bundle", Bondiana);
        return i;
    }

    private PopUpToast toast;
    //Всплывающие сообщения
    void toastMessage(String cToast) {
        toast=new PopUpToast(activity.getBaseContext());
        toast.setMessage(cToast);
    }
    void toastMessage() {
        toast.cancel();
    }


    private int MP_Type(){
        int type;
        if (mPlayer!=null){
            type = mPlayer.getMP_Type()==0? MP_SD_U:mPlayer.getMP_Type();
            Log.i(Log_Tag, "MP_Type "+ String.valueOf(mPlayer.getMP_Type()));
        }
        else type= MP_EMPTY;
        return type;
    }

}
