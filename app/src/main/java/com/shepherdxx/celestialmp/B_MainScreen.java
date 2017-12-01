package com.shepherdxx.celestialmp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.shepherdxx.celestialmp.extras.Constants;
import com.shepherdxx.celestialmp.extras.ControlPanelButtonListener;
import com.shepherdxx.celestialmp.extras.FragmentListener;
import com.shepherdxx.celestialmp.extras.PopUpToast;
import com.shepherdxx.celestialmp.fragment.Fragment_KrasView;
import com.shepherdxx.celestialmp.fragment.Fragment_Playlist;
import com.shepherdxx.celestialmp.fragment.Fragment_VK;
import com.shepherdxx.celestialmp.fragment.StartFragment;
import com.shepherdxx.celestialmp.fragment.dummy.DummyContent;
import com.shepherdxx.celestialmp.plailist.PlayListInfo;
import com.shepherdxx.celestialmp.plailist.TrackInfo;
import com.shepherdxx.celestialmp.settings.SettingsActivity;

import java.util.ArrayList;

import static com.shepherdxx.celestialmp.A_WelcomeScreen.ACTION_RESUME;
import static com.shepherdxx.celestialmp.MP_BackgroundService.mPlayer;
import static com.shepherdxx.celestialmp.extras.Constants.MP_RADIO;
import static com.shepherdxx.celestialmp.extras.Constants.iERROR;

public class B_MainScreen extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener
        , ControlPanelButtonListener
        , FragmentListener
        , Fragment_VK.OnVKListFragmentInteractionListener

{
    String Log_tag = B_MainScreen.class.getSimpleName();

    FragmentManager fragmentManager = getSupportFragmentManager();
    Toolbar toolbar;
    String AlbumName, ArtistName, SongName;

    /**
     * Сохраняемые Переменные
     */
    String onKrasVPath;
    boolean onStart;
    boolean onKrasV;
    int checkedFragmentId;

    /**
     * (@Link dummyId) заменить на Id из настроек приложения
     */
    int dummyId = R.id.player_playlist;

    NavigationView navView;
    ArrayList<Integer> fragmentId = new ArrayList<>();

    /**
     * Установка навигционного поля
     */
    private void setDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navView = navigationView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_main_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PopUpToast(getBaseContext())
                        .setMessage(getBaseContext().getResources().getString(R.string.develop));
            }
        });
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            onStart = savedInstanceState.getBoolean(onStartStr);
            onKrasV = savedInstanceState.getBoolean(onKrasView);
            onKrasVPath = savedInstanceState.getString(onKrasL);
            checkedFragmentId = savedInstanceState.getInt(cFrId);
            fragmentId = savedInstanceState.getIntegerArrayList(frIdAr);
//            checkedFragmentId= fragmentId != null ? fragmentId[fragmentId.length - 1] : dummyId;
        } else onStart = true;

        setDrawerLayout();

        String action = getIntent().getAction();
            switch (action){
                case ACTION_RESUME:
                    int id;
                    if (mPlayer.getMP_Type()==MP_RADIO)id=R.id.radio_activity;
                    else id=R.id.player_activity;
                    checkedFragment(id);
                    navViewCheckedItem();
                    break;
                default:
                    checkedFragment(FragmentId());
                    navViewCheckedItem();
                    break;
            }
    }

    private int FragmentId() {
        if (onStart) {
            onStart = false;
            return dummyId;
        }
        return checkedFragmentId;
    }

    void navViewCheckedItem() {
        navView.setCheckedItem(FragmentId());
        Log.i(Log_tag, "navViewCheckedItem " + String.valueOf(FragmentId()));
    }

    void checkedFragment(int id) {
        Intent intent;
        checkedFragmentId = id;
        int playlist_id;
        switch (id) {
            case R.id.player_playlist:
                Fragment exchangeFragment = StartFragment.newInstance();
                replacer(exchangeFragment);
                break;
            case R.id.kras_view_activity:
                if (!onKrasV)
                    exchangeFragment = Fragment_KrasView.newInstance("http://kadu.ru/");
                else exchangeFragment = Fragment_KrasView.newInstance(onKrasVPath);
                replacer(exchangeFragment);
                break;
            case R.id.player_activity:
                if (mPlayer != null) {
                    playlist_id = mPlayer.getPlaylistId();
                    if (playlist_id == Constants.PLAYLIST_RADIO)  playlist_id = Constants.PLAYLIST_All_Audio;
                    exchangeFragment =
                            Fragment_Playlist.newInstance(1, playlist_id);}
                else exchangeFragment = Fragment_Playlist.newInstance(2);
                replacer(exchangeFragment);
                break;
            case R.id.radio_activity:
                exchangeFragment =
                        Fragment_Playlist.newInstance(1, Constants.PLAYLIST_RADIO);
                replacer(exchangeFragment);
                break;
            case R.id.vk_activity:
                exchangeFragment = Fragment_VK.newInstance(1);
                replacer(exchangeFragment);
                break;
            case R.id.settings:
                // create intent to perform web search for this planet

                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_share:
                String message;
                if (currentTrack().equals(""))
                    message = "Failed";
                else message = currentTrack();
                startActivity(SendIntent(message));
                break;
            case R.id.nav_send:
                startActivity(SendIntent("Failed"));
                break;
            case R.id.web_search:
                // create intent to perform web search for this planet
                intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, toolbar.getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.cancel, Toast.LENGTH_LONG).show();
                }
                startActivity(intent);
                break;
        }
    }

    @Override
    public android.support.v4.app.LoaderManager getSupportLoaderManager() {
        return super.getSupportLoaderManager();
    }

    // Отправка сообщения с названием песни
    private Intent SendIntent(String ShareMessage) {
//        if (AlbumName != null && ArtistName != null) {
//            ShareMessage = AlbumName + " " + ArtistName + " " + SongName;
//        } else ShareMessage = SongName;
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(ShareMessage)
                .getIntent();
        return shareIntent;
    }

    //Закрытие панели меню DrawerLayout
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exchangeFragment instanceof Fragment_KrasView) {
                WebView kras_viewer = ((Fragment_KrasView) exchangeFragment).getKras_viewer();
                if (kras_viewer.canGoBack()) {
                    kras_viewer.goBack();
                } else super.onBackPressed();
            } else super.onBackPressed();
            if (fragmentId != null && fragmentId.size() - 2 > 0) {
                checkedFragmentId = fragmentId.get(fragmentId.size() - 2);
                fragmentId.remove(fragmentId.size() - 1);
            } else checkedFragmentId = dummyId;
            navViewCheckedItem();
        }
    }

    //Меню в правом верхнем углу
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.b__main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.search_button) {
            if (exchangeFragment instanceof Fragment_KrasView) {
                boolean b = ((Fragment_KrasView) exchangeFragment).getPanelVisibility();
                ((Fragment_KrasView) exchangeFragment).setPanelVisibility(!b);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    Fragment exchangeFragment;

    //Панель выбора меню DrawerLayout
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        checkedFragment(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Смена фрагментов
    void replacer(Fragment exchangeFragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, exchangeFragment);
        //Добавление в стак отмены
        if (fragmentId.size() != 0)
            fragmentTransaction.addToBackStack(null);
        fragmentId.add(checkedFragmentId);
        Log.i(Log_tag, fragmentId.toString());
        // Commit the transaction
        fragmentTransaction.commit();

    }


    private String currentTrack() {
        if (mPlayer != null) {
            AlbumName = (mPlayer.getAlbumName() == null) ? "" : mPlayer.getAlbumName();
            ArtistName = (mPlayer.getArtistName() == null) ? "" : mPlayer.getArtistName();
            SongName = (mPlayer.getSongName() == null) ? "" : mPlayer.getSongName();
        }
        if (AlbumName.equals("") || ArtistName.equals("") || SongName.equals("")) {
            return (SongName + " " + AlbumName + " " + ArtistName);
        } else return "";
    }

    @Override
    public void getTrackName(String trackName) {
        toolbar.setTitle(trackName);
    }

    @Override
    public void toolBarButton(boolean but) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onVKListFragmentInteraction(DummyContent.DummyItem item) {

    }

    private Boolean checkConnection() {
        boolean isConnected = false;
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork.isConnectedOrConnecting();
        } catch (NullPointerException e) {
            Toast.makeText(this, iERROR, Toast.LENGTH_SHORT).show();
        }
        Log.i("B_main_screen", "checkConnection " + isConnected);
        return isConnected;
    }

    String onStartStr = "onStart";
    String onKrasView = "krasChanel";
    String onKrasL = "lastView";
    String cFrId = "CheckedFragmentId";
    String frIdAr = "fragmentIdArray";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(onStartStr, onStart);
        outState.putInt(cFrId, checkedFragmentId);
        outState.putIntegerArrayList(frIdAr, fragmentId);
        if (exchangeFragment instanceof Fragment_KrasView) {
            outState.putBoolean(onKrasView, true);
            outState.putString(onKrasL, ((Fragment_KrasView) exchangeFragment).getPath());
            Log.i(Log_tag, ((Fragment_KrasView) exchangeFragment).getPath() + " " + onKrasView);
        } else outState.putBoolean(onKrasView, false);
    }




    @Override
    public void onPlaylistClick(PlayListInfo info) {

    }

    @Override
    public void onTrackClick(TrackInfo item, int position) {
        if (checkConnection()) {
            toolbar.setTitle(item.getmRadio());
            int id = item.getPlaylistId();
            startService(
                    PreService.startBGService(this,
                            id,
                            position)
            );
        }
    }

    @Override
    public void onTrackClick(TrackInfo.DummyItem item) {

    }
}
