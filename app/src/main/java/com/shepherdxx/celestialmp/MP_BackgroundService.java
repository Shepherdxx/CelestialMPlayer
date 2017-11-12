package com.shepherdxx.celestialmp;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.Toast;

import com.shepherdxx.celestialmp.extras.PopUpToast;

import java.io.File;
import java.io.IOException;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.widget.Toast.makeText;
import static com.shepherdxx.celestialmp.MP_MediaPlayer.LOG_TAG;
import static com.shepherdxx.celestialmp.extras.Constants.BUNDLE;
import static com.shepherdxx.celestialmp.extras.Constants.DEFAULT_R_M;
import static com.shepherdxx.celestialmp.extras.Constants.MP_BACK;
import static com.shepherdxx.celestialmp.extras.Constants.MP_FOWARD;
import static com.shepherdxx.celestialmp.extras.Constants.MP_HTTP;
import static com.shepherdxx.celestialmp.extras.Constants.MP_NEXT;
import static com.shepherdxx.celestialmp.extras.Constants.MP_PAUSE;
import static com.shepherdxx.celestialmp.extras.Constants.MP_PLAY;
import static com.shepherdxx.celestialmp.extras.Constants.MP_RADIO;
import static com.shepherdxx.celestialmp.extras.Constants.MP_RAW;
import static com.shepherdxx.celestialmp.extras.Constants.MP_SD;
import static com.shepherdxx.celestialmp.extras.Constants.MP_SD_U;
import static com.shepherdxx.celestialmp.extras.Constants.MP_STARTED;
import static com.shepherdxx.celestialmp.extras.Constants.MP_STOP;
import static com.shepherdxx.celestialmp.extras.Constants.MP_STOPED;
import static com.shepherdxx.celestialmp.extras.Constants.MP_TOWARD;
import static com.shepherdxx.celestialmp.extras.Constants.SERVICE_START;


public class MP_BackgroundService
        extends Service
        implements MediaPlayer.OnCompletionListener
        ,SharedPreferences.OnSharedPreferenceChangeListener
    ,AudioManager.OnAudioFocusChangeListener
    ,OnPreparedListener
{

    String MP_LOG_TAG = "BackgroundService";
    public static MP_MediaPlayer mPlayer;
    int MPType, MPState;
    String MPData;

    Context context = this;
    Activity a;
    int mCurCheckPosition;
    String[] SongPath;
    String[] SongTitle;
    Bundle Bondiana;
    BroadcastReceiver mNoiseReceiver;
    IntentFilter nF;

    SharedPreferences sharedPreferences;
    public static String rMode;
    public static int soundVolume;
    public String REPEAT_MODE;
    public String VOLUME;
    //обозначение AudioManager
    private AudioManager am;
    boolean mAudioFocusGranted;

    private void setup_sPref(){
        sharedPreferences = getDefaultSharedPreferences(this);
        REPEAT_MODE = getResources().getString(R.string.key_player_repeat_mode);
            rMode = sharedPreferences.getString(REPEAT_MODE, DEFAULT_R_M);
        VOLUME = getResources().getString(R.string.key_apps_volume);
            soundVolume = mSoundVolume (VOLUME);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        Log.i("setup_sPref",sharedPreferences.getAll().toString()+REPEAT_MODE+ File.pathSeparator + rMode);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i("onSPChanged", "yes" + File.pathSeparator + key);
        String[] keys= new String[]{REPEAT_MODE,VOLUME};
        if (key.equals(keys[0]))
        {
            rMode = sharedPreferences.getString(key, DEFAULT_R_M);
            setLoop();
            Log.i("onSPChanged", "rMode" + File.pathSeparator + rMode);
        }
        if (key.equals(keys[1]))
        {
            soundVolume = mSoundVolume (key);
            setVolume();
            Log.i("onSPChanged", "soundVolume" + File.pathSeparator + soundVolume);
        }
    }

    private int mSoundVolume(String key){
        int Volume = sharedPreferences.getInt(key, 50);
        int value = 50;
        try {
            value = Volume;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            sharedPreferences.edit().putInt(key, value).apply();
        } finally {
            return value;
        }
    }

    public static void setVolume(){
        final int MAX_VOLUME = 101;
        final float volume = (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math
                .log(MAX_VOLUME)));
        try {
            mPlayer.setVolume(volume, volume);
        }catch (NullPointerException e){e.printStackTrace();}
    }

    private void setLoop(){
        if (rMode.equals(getString(R.string.repeat))) mPlayer.setLooping(true); else mPlayer.setLooping(false);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    boolean wasReceive=false;

   private void setBroadcastReceiver(){
       mNoiseReceiver = new BroadcastReceiver() {
           @Override
           public void onReceive(Context context, Intent intent) {
               switch (intent.getAction()){
                   case android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                       //pause the music
                       makeText(context, "Слышь! Уши подбери!", Toast.LENGTH_LONG).show();
                       if(mPlayer.isPlaying()){onAir();
                           ResumeState=true;
                           wasReceive=true;}
                       break;
                   case android.media.AudioManager.ACTION_HEADSET_PLUG:
                       if(intent.getIntExtra("state",0)==1&&wasReceive){
                           wasReceive=false;
                           makeText(context, "Ухи подобраны!", Toast.LENGTH_LONG).show();
                           if(ResumeState)onAir();}
                       break;

               }

           }
       };
       //on Play
       nF = new IntentFilter();
       nF.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
       nF.addAction(AudioManager.ACTION_HEADSET_PLUG);
       registerReceiver(mNoiseReceiver, nF);
   }
    private MediaSessionCompat.Callback callback;

    private void registerCallback(){ callback= new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            registerReceiver(mNoiseReceiver, nF);
        }

        @Override
        public void onStop() {
            mNoiseReceiver.clearAbortBroadcast();
            unregisterReceiver(mNoiseReceiver);
        }
    };}


    @Override
    public void onCreate() {
        super.onCreate();

        setup_sPref();
        am = (AudioManager) getSystemService(AUDIO_SERVICE);

        //остановка музыки когда выпал наушник
        setBroadcastReceiver();
        registerCallback();
    }

    private void reqAudioFocus(){
        // Request audio focus for play back
        int result = am.requestAudioFocus(this,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioFocusGranted = true;
            mPlayer.start();
            callback.onPlay();
            ResumeState=false;
            intent = new Intent(MP_STARTED);
        } else if (result == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
            mAudioFocusGranted = false;
            toastMessage("Some Problem");
            // take appropriate action
        }
    }

//    ConnectivityManager cm;

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            boolean IntentCheck = intent.hasExtra(BUNDLE);

            Log.i("getIntent", IntentCheck +" " + String.valueOf(mCurCheckPosition));
            if (IntentCheck) {
                Bondiana = intent.getBundleExtra(BUNDLE);
                if (Bondiana.getInt("MPType")!=0)
                    MPType = Bondiana.getInt("MPType");
                MPState = Bondiana.getInt("MPState");
            }
//            else MPState= SERVICE_START;
            if (mPlayer == null) {
                Log.i(MP_LOG_TAG,"mPlayer created");
                mPlayer = new MP_MediaPlayer();
            }

            Log.i(MP_LOG_TAG,mPlayer.toString()+ " " + String.valueOf(MPState));
            switch (MPState) {
                case SERVICE_START:
                    mCurCheckPosition = Bondiana.getInt("MPData",1);
                    Log.i(MP_LOG_TAG,"service started");
                    getArrays();
                    getMPData(mCurCheckPosition);
                    Create(MPType,MPData, null);
                    break;
                case MP_STOP:
                    if (mPlayer.isPlaying())onAir();
                    break;
                case MP_PAUSE:
                    onAir();
                    break;
                case MP_FOWARD:
                    ForwardScript();
                    break;
                case MP_TOWARD:
                    TowardScript();
                    break;
                case MP_NEXT:
                    NextSong();
                    break;
                case MP_BACK:
                    prevSong();
                    break;
                case MP_PLAY:
                    mCurCheckPosition = Bondiana.getInt("MPData");
                    getArrays();
                    getMPData(mCurCheckPosition);
                    Create(MPType,MPData, this);
                    break;
            }
            Log.i("Intent", MPData + " gained");
        }
        return START_STICKY;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // убираем всплывающие сообщения
        toastMessage();

        // закрываем проигрыватель
        releaseMediaPlayer();

        // дерегистрируем (выключаем) BroadcastReceiver
        unregisterReceiver(mNoiseReceiver);

        // Unregister OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

    }

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        Log.i("onAudioFocusChange",String.valueOf(focusChange));
        switch (focusChange){
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.i("onAudioFocusChange","AUDIOFOCUS_LOSS_TRANSIENT");
                ResumeState=true;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.i("onAudioFocusChange","AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                ResumeState=true;
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                Log.i("onAudioFocusChange","AUDIOFOCUS_GAIN");
                if(ResumeState&!isOnAir()){onAir();
                    ResumeState=false;}
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                onAir();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                Log.i("onAudioFocusChange","AUDIOFOCUS_LOSS");
                if(isOnAir()){onAir();
                    ResumeState=true;}
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
//                releaseMediaPlayer();
                break;
        }
    }

    @Override
    public void onLowMemory() {
        stopSelfResult(START_REDELIVER_INTENT);
    }


    private void prevSong() {
        boolean b = (mPlayer.isPlaying());
        boolean resume = true;
        if (mCurCheckPosition - 1 >= 0) {
            mCurCheckPosition = mCurCheckPosition - 1;
        } else resume = false;
        getMPData(mCurCheckPosition);
        OnPreparedListener onPreparedListener = b&&resume? this:null;
        Create(MPType,MPData, onPreparedListener);
    }

    private void NextSong() {
        int NextPosition = (sharedPreferences.getString(REPEAT_MODE, DEFAULT_R_M)
                .equals(getResources().getString(R.string.repeat_all)))? 1:mCurCheckPosition;
        boolean b = (mPlayer.isPlaying());
        mCurCheckPosition = (mCurCheckPosition + 1) >= SongPath.length ? NextPosition : mCurCheckPosition + 1;
        getMPData(mCurCheckPosition);
        OnPreparedListener onPreparedListener = b? this:null;
        Create(MPType,MPData, onPreparedListener);
    }

    private void getMPData(int Position) {
        if (Position>SongPath.length){mCurCheckPosition = 0 ;Position=mCurCheckPosition;}
        MPData = SongPath[Position];
        Log.i("getMPData", String.valueOf(mCurCheckPosition));
    }

    private void getArrays(){
        SongPath = Bondiana.getStringArray("SongPath");
        SongTitle= Bondiana.getStringArray("SongTitle");
    }



    @Override
    public void onCompletion(MediaPlayer mp) {
        NextSong();
        onAir();
    }
    long forwardTime = 5000;
    long backwardTime= 5000;
    //кнопка вперед на 5 сек
    void ForwardScript() {
        long startTime= mPlayer.getCurrentPosition();
        long finalTime= mPlayer.getDuration();
        if ((startTime + forwardTime) <= finalTime) {
            startTime = startTime + forwardTime;
            mPlayer.seekTo((int) startTime);
        } else {
            toastMessage("Cannot jump forward 5 seconds");
        }
    }

    //кнопка назад на 5 сек
    void TowardScript() {
        long startTime= mPlayer.getCurrentPosition();
        if ((startTime - backwardTime) > 0) {
            startTime = startTime - backwardTime;
            mPlayer.seekTo((int) startTime);
        } else {
            toastMessage( "Cannot jump backward 5 seconds");
        }
    }

    boolean ResumeState;
    long startTimeBefore;
    Intent intent;

    private boolean isOnAir() {
        boolean b=false;
        if (mPlayer != null) {
            b = mPlayer.isPlaying();
        }return b;
    }
    //Старт/Пауза плеера.
    public void onAir() {
        if (mPlayer != null) {
            if (!isOnAir()) {
                Log.e(MP_LOG_TAG, "onAir"+"Player status " + String.valueOf(isOnAir()));
//                toastMessage(context, "Playing");
                reqAudioFocus();
                if (MPType!=MP_RADIO){mPlayer.setOnCompletionListener(this);}
            }else{
                callback.onStop();
                Log.e("offAir", "Player status" + String.valueOf(isOnAir()));
//                toastMessage(context, "Pause");
                mPlayer.pause();
                startTimeBefore=mPlayer.getCurrentPosition();
                ResumeState=true;
                intent=new Intent(MP_STOPED);
            }
            Log.d("sender", "Broadcasting message");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }else {if(ResumeState){getMPData(mCurCheckPosition);
            Create(MPType,MPData, null);mPlayer.seekTo((int)startTimeBefore);onAir();}}
    }

    //Сброс проигрывателя
    public void releaseMediaPlayer() {
        if (mPlayer != null) {
            try {
                if(mPlayer.isPlaying()) mPlayer.stop();
                mPlayer.reset();
                mPlayer.setRequest(true);
                mPlayer.release();
                // Set the media Player back to null.
                mPlayer = null;
                callback.onStop();
                Log.e(MP_LOG_TAG,"releaseMediaPlayer completed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    private Toast toast;
//    //Всплывающие сообщения
//    void toastMessage(Context context, String cToast) {
//        if (toast != null) {
//            toast.cancel();
//        }
//        toast = makeText(context, cToast, Toast.LENGTH_SHORT);
//        toast.show();
//    }
//    void toastMessage() {
//        if (toast != null) {
//            toast.cancel();
//        }
//    }
    private PopUpToast toast;
        //Всплывающие сообщения
        void toastMessage(String cToast) {
            toast=new PopUpToast(context);
            toast.setMessage(cToast);
        }
        void toastMessage() {
            toast.cancel();
        }


    //    private int MP_TYPE;
    String DATA_SD = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            + File.separator;

    public void Create(int mpType, String Data, OnPreparedListener listener){
        releaseMediaPlayer();
        Uri uri;
        try {mPlayer = new MP_MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            switch (mpType){
                case MP_RAW:
                    uri= Uri.parse(String.format("android.resource://%s/%s",context.getPackageName(),Data));
                        Log.i(LOG_TAG, "prepare Raw " + uri);
                        mPlayer.setDataSource(context, uri);
                        mPlayer.setOnPreparedListener(listener);
                        mPlayer.prepare();
                    break;
                case MP_RADIO:
//                        uri= Uri.parse(Data);
                        Log.i(LOG_TAG, "prepare Radio");
                        mPlayer.setDataSource(Data);
                        Log.i(LOG_TAG, "prepareAsync");
                        mPlayer.setOnPreparedListener(listener);
                        mPlayer.prepareAsync();
                    break;
                case MP_SD:
                        Data=DATA_SD+Data;
                        Log.i(LOG_TAG, "prepare SD");
                        mPlayer.setDataSource(Data);
                        mPlayer.prepare();
                    break;
                case MP_SD_U:
                        Log.i(LOG_TAG, "prepare SD_U" + File.pathSeparator + Data);
                        mPlayer.setDataSource(Data);
                        if (listener!=null){mPlayer.setOnPreparedListener(listener);}
                        mPlayer.prepare();
                    break;
                case MP_HTTP:
                        Log.i(LOG_TAG, "prepare HTTP");
                        mPlayer.setDataSource(Data);
                        Log.i(LOG_TAG, "prepareAsync");
                        mPlayer.setOnPreparedListener(listener);
                        mPlayer.prepareAsync();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(MP_LOG_TAG,"IOException" + e.toString());
        } catch (RuntimeException e){
            Log.i("ошибка", e.toString());
        }

        mPlayer.setMP_Type(mpType);
        mPlayer.setRequest(true);
        mPlayer.setSongName(SongTitle[mCurCheckPosition]);
        mPlayer.setPosition(mCurCheckPosition);
        registerReceiver(mNoiseReceiver, nF);
        setVolume();
        setLoop();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        onAir();
    }


//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        final ResultReceiver receiver = intent.getParcelableExtra(Constants.RECEIVER);
//        receiver.send(Constants.STATUS_RUNNING, Bundle.EMPTY);
//        final Bundle data = new Bundle();
//
//        try {
//            Thread.sleep(Constants.SERVICE_DELAY);
//            data.putString(Constants.RECEIVER_DATA, "Sample result data");
//        } catch (InterruptedException e) {
//            data.putString(Constants.RECEIVER_DATA, "Error");
//        }
//        receiver.send(Constants.STATUS_FINISHED, data);
//    }



}
