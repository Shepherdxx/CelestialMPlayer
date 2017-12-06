package com.shepherdxx.celestialmp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shepherdxx.celestialmp.extras.Constants;

import static com.shepherdxx.celestialmp.A_WelcomeScreen.ACTION_RESUME;
import static com.shepherdxx.celestialmp.A_WelcomeScreen.ACTION_START;

public class PreService extends Activity {
        final static int INTENT_FLAGS = Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_TASK_ON_HOME;

        /**
         * Returns an intent pointing to this activity.
         *
         * @param context the context to use.
         * @param action the action to set.
         * @return an intent for this activity.
         */
        public static Intent getIntent(Context context, String action) {
            Intent intent = new Intent(context, PreService.class)
                    .setFlags(INTENT_FLAGS)
                    .setAction(action);
            return intent;
        }

        public static Intent startBGService(Context context, int id, int pos) {
            Intent intent = controlBGService(context, MP_BG_Service.ACTION_PLAY);
            Bundle b = new Bundle();
            b.putInt("Playlist", id);
            b.putInt("MPData", pos);
            intent.putExtra("Bundle", b);
            Log.i("PreService#1","Playlist" + id   +
                    "MPData"   + pos
            );
            return intent;
        }

        public static Intent controlBGService(Context context, String action) {
            Intent intent = new Intent(context, MP_BG_Service.class);
            intent.setAction(action);
            return intent;
        }


        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final String action = getIntent().getAction();
            if (action != null)
                switch (action) {
                    case MP_BG_Service.ACTION_PLAY:
                        Intent  intent = new Intent(this, B_MainScreen.class);
                        if (MP_BG_Service.hasInstance()) {
                                intent.setAction(ACTION_RESUME);
                        } else  intent.setAction(ACTION_START);
                        Log.i("PreService#1", intent.getAction());
                        startActivity(intent);
                        break;
                    case MP_BG_Service.ACTION_TOGGLE_PLAYBACK:
                    case MP_BG_Service.ACTION_NEXT_SONG:
                    case MP_BG_Service.ACTION_PREV_SONG:
                        startService(controlBGService(this, action));
                        break;
//                case MP_BG_Service.ACTION_PAUSE:
//                case MP_BG_Service.ACTION_TOGGLE_PLAYBACK_DELAYED:
//                case MP_BG_Service.ACTION_RANDOM_MIX_AUTOPLAY:
//                case MP_BG_Service.ACTION_NEXT_SONG_DELAYED:
//                case MP_BG_Service.ACTION_NEXT_SONG_AUTOPLAY:
//                case MP_BG_Service.ACTION_PREVIOUS_SONG_AUTOPLAY:
//                case MP_BG_Service.ACTION_CYCLE_SHUFFLE:
//                case MP_BG_Service.ACTION_CYCLE_REPEAT:
                    default:
                        throw new IllegalArgumentException("No such action: " + action);
                }
            Log.i("PreService", "get action " + action);
            finish();
        }
    }
