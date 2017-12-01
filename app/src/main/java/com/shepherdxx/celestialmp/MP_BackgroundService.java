package com.shepherdxx.celestialmp;

import android.app.Service;
import android.appwidget.AppWidgetManager;
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
import com.shepherdxx.celestialmp.plailist.MyTrackInfo;
import com.shepherdxx.celestialmp.plailist.PlayListInfo;
import com.shepherdxx.celestialmp.plailist.PlayListTrue;

import java.io.File;
import java.io.IOException;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.widget.Toast.makeText;
import static com.shepherdxx.celestialmp.MP_MediaPlayer.LOG_TAG;
import static com.shepherdxx.celestialmp.extras.Constants.BUNDLE;
import static com.shepherdxx.celestialmp.extras.Constants.DEFAULT_R_M;
import static com.shepherdxx.celestialmp.extras.Constants.MP_HTTP;
import static com.shepherdxx.celestialmp.extras.Constants.MP_PLAY;
import static com.shepherdxx.celestialmp.extras.Constants.MP_PREPARE;
import static com.shepherdxx.celestialmp.extras.Constants.MP_PREPARE_RADIO;
import static com.shepherdxx.celestialmp.extras.Constants.MP_RADIO;
import static com.shepherdxx.celestialmp.extras.Constants.MP_RAW;
import static com.shepherdxx.celestialmp.extras.Constants.MP_SD;
import static com.shepherdxx.celestialmp.extras.Constants.MP_SD_U;
import static com.shepherdxx.celestialmp.extras.Constants.MP_STARTED;
import static com.shepherdxx.celestialmp.extras.Constants.MP_STOPED;


public class MP_BackgroundService
        extends Service
        implements MediaPlayer.OnCompletionListener
        ,SharedPreferences.OnSharedPreferenceChangeListener
    ,AudioManager.OnAudioFocusChangeListener
    ,OnPreparedListener
{

    public static final String ACTION_PLAY = "com.shepherdxx.celestialmp.action.PLAY";
    public static final String ACTION_PAUSE = "com.shepherdxx.celestialmp.action.PAUSE";
    public static final String ACTION_STOP = "com.shepherdxx.celestialmp.action.STOP";
    public static final String ACTION_TOGGLE_PLAYBACK = "com.shepherdxx.celestialmp.action.TOGGLE_PLAYBACK";
    public static final String ACTION_NEXT_SONG = "com.shepherdxx.celestialmp.action.NEXT_SONG";
    public static final String ACTION_PREV_SONG = "com.shepherdxx.celestialmp.action.PREVIOUS_SONG";
    public static final String ACTION_FORWARD = "com.shepherdxx.celestialmp.action.FORWARD";
    public static final String ACTION_TOWARD = "com.shepherdxx.celestialmp.action.TOWARD";
    String MP_LOG_TAG = "BackgroundService";
    public MP_MediaPlayer mPlayer=null;
    int MPType;
    public int MPState=0;
    String MPData;
    String SongTitle;

    Context context = this;
    int mCurPosition;
//    String[] SongPath;
//    String[] SongTitle;
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

    public void setVolume(){
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
                       if(MPState==MP_PLAY){
                           onAir();
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
            mPlayer.start();
            registerReceiver(mNoiseReceiver, nF);
        }

        @Override
        public void onStop() {
            MPState=getState();
            updateWidgets();
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


        initWidgets();
        sInstance = this;
        synchronized (sWait) {
            if (mPlayer==null) {
                mCurPosition = sharedPreferences.getInt("lastPos", 0);
                currentPlaylistId = sharedPreferences.getInt("lastId", -1);
            }
            sWait.notifyAll();}
    }

    //TODO 1:
    private void reqAudioFocus(){
        // Request audio focus for play back
        int result = am.requestAudioFocus(this,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioFocusGranted = true;
            callback.onPlay();
            ResumeState=false;
            broadcastIntent = new Intent(MP_STARTED);
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
            String action;

            if (mPlayer == null) {
                Log.i(MP_LOG_TAG,"mPlayer created");
                mPlayer = new MP_MediaPlayer();
            }
            action= intent.getAction();
            if (action != null)
                switch (action) {
                    case ACTION_PAUSE:
                        if (mPlayer.isPlaying()) onAir();
                        break;
                    case ACTION_TOGGLE_PLAYBACK:
                        onAir();
                        break;
                    case ACTION_FORWARD:
                        ForwardScript();
                        break;
                    case ACTION_TOWARD:
                        TowardScript();
                        break;
                    case ACTION_NEXT_SONG:
                        NextSong();
                        break;
                    case ACTION_PREV_SONG:
                        prevSong();
                        break;
                }
            if (IntentCheck) {
                Bondiana = intent.getBundleExtra(BUNDLE);
                if (action != null)
                    switch (action) {
                        case ACTION_PLAY:
                            currentPlaylistId = Bondiana.getInt("Playlist");
                            mCurPosition = Bondiana.getInt("MPData");
                            Log.i(LOG_TAG, ACTION_PLAY + " " + currentPlaylistId + " MPData " + mCurPosition);
                            playListInfo = gainPlaylist(currentPlaylistId);
                            if (playListInfo == null) {
                                new PopUpToast(context).setMessage("audioTracks not found");
                                break;
                            }
                            getMPData(playListInfo, mCurPosition);
                            Create(MPType, MPData, this);
                            break;
                    }
            }
            Log.i(LOG_TAG, "onStartCommand " + action + MPData + " gained");

        }
        return START_NOT_STICKY;
    }

    PlayListInfo playListInfo;

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
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                Log.i("onAudioFocusChange","AUDIOFOCUS_GAIN");
                if(ResumeState&!isOnAir()){onAir();
                    ResumeState=false;}
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

    private static int NEXT_SONG=1;
    private static int PREV_SONG=-1;
    private void prevSong() {
        PlayListInfo playListInfo=curPlaylist();
        boolean b = (mPlayer.isPlaying());
        OnPreparedListener onPreparedListener = deltaCheck(PREV_SONG)&b? this:null;
        getMPData(playListInfo,deltaChange(PREV_SONG));
        Create(MPType,MPData, onPreparedListener);
    }

    private void NextSong() {
        PlayListInfo playListInfo=curPlaylist();
        boolean b = (mPlayer.isPlaying());
        OnPreparedListener onPreparedListener = deltaCheck(NEXT_SONG)&b? this:null;
        Log.i(LOG_TAG,"deltaCheck " + File.pathSeparator + deltaCheck(NEXT_SONG));
        getMPData(playListInfo,deltaChange(NEXT_SONG));
        Create(MPType,MPData, onPreparedListener);
    }

    private int deltaChange(int delta){
        int sum = mCurPosition + delta;
        if (deltaCheck(delta))mCurPosition = sum;
        return mCurPosition;
    }

    private boolean deltaCheck(int delta) {
        PlayListInfo playListInfo = curPlaylist();
        int sum   = mCurPosition + delta;
        int length=playListInfo.audioTracks.size();
        Log.i(LOG_TAG,"deltaCheck summ" + File.pathSeparator + sum);
        switch (delta) {
            case 1:
                if (sum<=length-1)
                    return true;
                break;
            case -1:
                if (sum>=0)
                    return true;
                break;
            default:
                return false;
        }
        return false;
    }

    private void getMPData(PlayListInfo playListInfo, int Position) {
        int length=playListInfo.audioTracks.size();
        if (Position>length){Position = 0 ;
            mCurPosition =Position;}
        MyTrackInfo pti= playListInfo.audioTracks.get(Position);
        trackInfo = pti;
        MPData      = pti.getData();
        SongTitle   = pti.getTitle();
        MPType      = playListInfo.plType;
        sharedPreferences.edit()
                .putInt("lastId",(int)playListInfo.playlistId)
                .putInt("lastPos", Position)
                .apply();
        Log.i(LOG_TAG,"getMPData " + length);
        Log.i(LOG_TAG,"getMPData " + String.valueOf(mCurPosition));
    }

    int currentPlaylistId;
    private PlayListInfo gainPlaylist(int id){
        return  new PlayListTrue(context).createPlaylist(id);
    }

    private PlayListInfo curPlaylist(){
        return  gainPlaylist(currentPlaylistId);
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
    Intent broadcastIntent;

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
                reqAudioFocus();
                if (MPType!=MP_RADIO){mPlayer.setOnCompletionListener(this);}
            }else{
                callback.onStop();
                Log.e("offAir", "Player status" + String.valueOf(isOnAir()));
                mPlayer.pause();
                startTimeBefore=mPlayer.getCurrentPosition();
                ResumeState=true;
                broadcastIntent =new Intent(MP_STOPED);
            }
            Log.d("sender", "Broadcasting message");
            sendMyBroadcast(broadcastIntent);
        }else {if(ResumeState){getMPData(gainPlaylist(-1), mCurPosition);
            Create(MPType,MPData, null);mPlayer.seekTo((int)startTimeBefore);onAir();}}
        assert mPlayer != null;
        MPState=getState();
        updateWidgets();
    }

    public void sendMyBroadcast(Intent i) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    //Сброс проигрывателя
    public void releaseMediaPlayer() {
        if (mPlayer != null) {
            try {
                if(mPlayer.isPlaying()) mPlayer.stop();
                callback.onStop();
                mPlayer.reset();
                mPlayer.release();
                // Set the media Player back to null.
                mPlayer = null;
                Log.e(MP_LOG_TAG,"releaseMediaPlayer completed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private PopUpToast toast;
        //Всплывающие сообщения
        void toastMessage(String cToast) {
            toast=new PopUpToast(context);
            toast.setMessage(cToast);
        }
        void toastMessage() {
            toast.cancel();
        }


    String DATA_SD = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            + File.separator;

    public void Create(int mpType, String Data, OnPreparedListener listener){
        releaseMediaPlayer();
        Uri uri;
        try {mPlayer = MP_MediaPlayer.newPlayer();
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
                        broadcastIntent=new Intent(MP_PREPARE_RADIO);
                        sendMyBroadcast(broadcastIntent);
                    break;
                case MP_SD:
                        Data=DATA_SD+Data;
                        Log.i(LOG_TAG, "prepare SD" + File.pathSeparator + Data);
                        mPlayer.setDataSource(Data);
                        mPlayer.prepare();
                    break;
                case MP_SD_U:
                        Log.i(LOG_TAG, "prepare SD_U" + File.pathSeparator + Data);
                        mPlayer.setDataSource(Data);
                        if (listener!=null){mPlayer.setOnPreparedListener(listener);}
                        mPlayer.prepare();
                        broadcastIntent=new Intent(MP_PREPARE);
                        sendMyBroadcast(broadcastIntent);
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
            Log.i(MP_LOG_TAG, "IOException" + e.toString());
        }
        mPlayer.setMP_Type(mpType);
        mPlayer.setPosition(mCurPosition);
        registerReceiver(mNoiseReceiver, nF);
        setVolume();
        setLoop();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        onAir();
    }



    /**
     * Object used for PlaybackService startup waiting.
     */
    private static final Object[] sWait = new Object[0];
    /**
     * The appplication-wide instance of the PlaybackService.
     */
    public static MP_BackgroundService sInstance;
    public static boolean hasInstance() {
        return sInstance != null;
    }

    public MP_MediaPlayer getPlayer() {
        return mPlayer;
    }

    public static boolean hasPlayer() {
        if (hasInstance()) return sInstance.getPlayer()!= null;
        return false;
    }

    private void initWidgets()
    {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        SmallWidget.checkEnabled(this, manager);

    }

    private void updateWidgets()
    {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
//        Song song = mCurrentSong;

        SmallWidget.updateAppWidget(this, manager, SongTitle, MPState);
    }

    public static MP_BackgroundService get(Context context) {

        if (sInstance == null) {
            context.startService(new Intent(context, MP_BackgroundService.class));

            while (sInstance == null) {
                try {
                    synchronized (sWait) {
                        sWait.wait();
                    }
                } catch (InterruptedException ignored) {
                }
            }
        }

        return sInstance;
    }

    MyTrackInfo trackInfo=null;
    public MyTrackInfo getTrackInfo() {
        return trackInfo;
    }

    public long getCurTime() {
        return mPlayer.getCurrentPosition();
    }

    public void setCurTime(int time) {
        mPlayer.seekTo(time);
    }

    public int getmCurPosition() {
        return mCurPosition;
    }

    public int getState() {
        int state=0;
        if (mPlayer.isPlaying())state=1;
        return state;
    }
}
