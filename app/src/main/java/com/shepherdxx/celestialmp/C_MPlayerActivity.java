package com.shepherdxx.celestialmp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import static com.shepherdxx.celestialmp.extras.Constants.MP_SD_U;

/**
 * Created by Shepherdxx on 02.11.2017.
 */

public class C_MPlayerActivity  extends AppCompatActivity
//        implements
//        AdapterMS.OnViewClicklListener
//        ,AdapterMS.OnChangeClicklListener
//        ,View.OnClickListener
//        ,ControlPanelButtonListener
//        control_panel_default.ButtonListener
//        ,ControlsPanelReciever.Receiver
{

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mplayer);
            context = this;
//            playlist = new PPL(context);


//            //Проверка на интент
//            getIndexIntent();
            //Создаем плейлист
//            CheckPermision();
            MPType = MP_SD_U;
//            CreatePathList();
            findView();
            intent = new Intent("SOME_COMMAND_ACTION", null, this, MP_BackgroundService.class);
            bondiana = new Bundle();
            bondiana.putInt("MPType", MPType);
            bondiana.putStringArray("SongPath", songPath);
            bondiana.putStringArray("SongTitle", songTitle);

//            setPlaylist();


        }

//    ImageView btPlayTb;
    RecyclerView RW;
//    int fragmentIndex;
//    String playlistName;
//    AdapterMS mAdapterMS;
    Context context;
//    ArrayList<MusicScroll> rows;

//    int mCurCheckPosition = 0;
//    boolean firstStart;
//    Toolbar tb;

    Intent intent;
    Bundle bondiana;
    String[] songPath;
    String[] songTitle;
    int MPType, MPState;

//    android.app.Fragment fragment;
//    PPL playlist;
////    private ControlsPanelReciever mReceiver;
//

    //соответствия с View
    void findView() {
//        tb = (Toolbar) findViewById(R.id.tb);
//        setSupportActionBar(tb);
        RW = (RecyclerView) findViewById(R.id.Play_list);
//        btPlayTb = (ImageView) findViewById(tb_play_icon);
//
//        btPlayTb.setOnClickListener(this);

    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case tb_play_icon:
//                MPState = MP_PAUSE;
//                bondiana.putInt("MPState", MPState);
//                intent.putExtra("Bundle", bondiana);
//                startService(intent);
//                break;
//        }
//    }
//
////    //Выесняем какой Fragment грузить
////    private void getIndexIntent() {
////        Intent gainIndexIntent = getIntent();
////        boolean IntentCheck = gainIndexIntent.hasExtra(Intent.EXTRA_SHORTCUT_NAME);
////        if (IntentCheck) {
////            String getExtra = gainIndexIntent.getStringExtra(Intent.EXTRA_TEXT);
////            fragmentIndex = Integer.valueOf(getExtra);
////            if (fragmentIndex == PLAYER) {
////                playlistName = gainIndexIntent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
////                MPType = Integer.valueOf(playlistName);
////                Log.i("Intent", playlistName + " gained");
////            }
////        }
////    }
//
//
//    private void setPlaylist(){
//        mAdapterMS = new AdapterMS(context, rows);
//        LinearLayoutManager mLLManager = new LinearLayoutManager(context);
//        DividerItemDecoration DID =
//                new DividerItemDecoration(RW.getContext(), mLLManager.getOrientation());
//        RW.addItemDecoration(DID);
//        RW.setAdapter(mAdapterMS);
//        RW.setLayoutManager(mLLManager);
//        mAdapterMS.setmOnViewClicklListener(this);
//        mAdapterMS.setmOnChangeClicklListener(this);
//    }
//
//    @Override
//    public void getTrackName(String trackName) {
//        getSupportActionBar().setTitle(trackName);
//        mCurCheckPosition = mPlayer.getPosition();
//        RW.scrollToPosition(mCurCheckPosition);
//    }
//
//    @Override
//    public void toolBarButton(boolean but) {
//        if (but) {
//            btPlayTb.setImageResource(ic_pause_circle_outline_black_48dp);
//        } else {
//            btPlayTb.setImageResource(R.drawable.ic_play_circle_outline_black_48dp);
//        }
//    }
//
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    public void onItemClick(ImageView im, View v, MusicScroll obj, int position) {
//        mCurCheckPosition = position;
//        Log.i("onItemClick", "received");
//        bondiana.putInt("MPData", mCurCheckPosition);
//        MPState = MP_PLAY;
//        bondiana.putInt("MPState", MPState);
//        intent.putExtra("Bundle", bondiana);
//        startService(intent);
//
//    }
//
//    /**
//     * Uses the ShareCompat Intent builder to setUpdateSongTime our Forecast intent for sharing. We set the
//     * type of content that we are sharing (just regular text), the text itself, and we return the
//     * newly created Intent.
//     *
//     * @return The Intent to use to start our share.
//     */
//    private Intent createSongSendIntent() {
//        String ShareMessage;
////        if (AlbumName != null && ArtistName != null) {
////            ShareMessage = AlbumName + " " + ArtistName + " " + SongName;
////        } else ShareMessage = SongName;
//        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
//                .setType("text/plain")
////                .setText(ShareMessage)
//                .getIntent();
//        return shareIntent;
//    }
//
//    @Override
//    public void onReceiveResult(int resultCode, Bundle data) {
//        switch (resultCode) {
//            case Constants.STATUS_RUNNING:
//                try {
//                    String a = String.valueOf(fragment.isInLayout());
//                    Log.i("Fragment", a);
//                    fragment.getView().setVisibility(View.VISIBLE);
//                    fragment.onResume();
//                } catch (
//                        NullPointerException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case Constants.STATUS_FINISHED:
//
//                Toast.makeText(this, "Service finished with data: "
//                        + data.getString(Constants.RECEIVER_DATA), Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }
//
//    private void CheckPermision() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
//            } else {
//                finish();
//            }
//        } else {
//            rows = playlist.plCreate(MPType);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 123:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    rows = playlist.plCreate(MPType);
//                } else {
//                    Toast.makeText(context, "В доступе отказано", Toast.LENGTH_SHORT).show();
//                    CheckPermision();
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//                break;
//        }
//    }
//
//    private void CreatePathList() {
//        int st;
//        songPath = new String[rows.size()];
//        songTitle = new String[rows.size()];
//        try {
//            for (st = 0; st < rows.size(); st++) {
//                switch (MPType) {
//                    case MP_SD_U:
//                        songPath[st] = rows.get(st).getmUrl();
//                        break;
//                    case MP_RAW:
//                        songPath[st] = String.valueOf(rows.get(st).getSongId());
//                        break;
//                    case MP_SD:
//                        songPath[st] = rows.get(st).getSongName();
//                        break;
//                }
//                songTitle[st] = rows.get(st).getSongName();
//                System.out.println(songPath[st]);
//                System.out.println(songTitle[st]);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onItemClick(TextView im, View v, MusicScroll obj, int position) {
//        Intent i=new Intent(MPlayerActivity.this, SongEditorActivity.class);
//        Log.i("onItemClick", "received"+ obj.getmUrl());
//        i.putExtra("Url", obj.getmUrl());
//        //Start intent
//        startActivity(i);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        reloadPl();
//    }
//    private void reloadPl(){
//        try {
//            mAdapterMS.clear();
//            playlist = new PPL(context);
//            rows = playlist.plCreate(Integer.valueOf(playlistName));
//            Log.i("reloadPl", String.valueOf(rows.size()));
//            mAdapterMS.addAll(rows);
//            mAdapterMS.notifyDataSetChanged();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        } finally {
//            System.out.println("hi");
//        }
//    }

}
