package com.shepherdxx.celestialmp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shepherdxx.celestialmp.extras.Constants;

import static com.shepherdxx.celestialmp.MP_BackgroundService.mPlayer;

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

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final String action = getIntent().getAction();
            Intent intent;
            Bundle Bondiana;
            int state;
            switch (action) {
                case MP_BackgroundService.ACTION_PLAY:
                case MP_BackgroundService.ACTION_PAUSE:
                case MP_BackgroundService.ACTION_TOGGLE_PLAYBACK:
                    intent = new Intent(this, MP_BackgroundService.class);
                    Bondiana = new Bundle();
                    Bondiana.putInt("MPType", 0);
                    state = Constants.MP_PAUSE;
                    Bondiana.putInt("MPState",state);
                    intent.putExtra("Bundle", Bondiana);
                    intent.setAction(action);
                    startService(intent);
                    break;
//                case MP_BackgroundService.ACTION_TOGGLE_PLAYBACK_DELAYED:
//                case MP_BackgroundService.ACTION_RANDOM_MIX_AUTOPLAY:
                case MP_BackgroundService.ACTION_NEXT_SONG:
                    intent = new Intent(this, MP_BackgroundService.class);
                    Bondiana = new Bundle();
                    Bondiana.putInt("MPType", 0);
                    state = Constants.MP_NEXT;
                    Bondiana.putInt("MPState",state);
                    intent.putExtra("Bundle", Bondiana);
                    intent.setAction(action);
                    startService(intent);
                    break;
//                case MP_BackgroundService.ACTION_NEXT_SONG_DELAYED:
//                case MP_BackgroundService.ACTION_NEXT_SONG_AUTOPLAY:
                case MP_BackgroundService.ACTION_PREVIOUS_SONG:
//                case MP_BackgroundService.ACTION_PREVIOUS_SONG_AUTOPLAY:
//                case MP_BackgroundService.ACTION_CYCLE_SHUFFLE:
//                case MP_BackgroundService.ACTION_CYCLE_REPEAT:
                    intent = new Intent(this, MP_BackgroundService.class);
                    Bondiana = new Bundle();
                    Bondiana.putInt("MPType", 0);
                    state = Constants.MP_BACK;
                    Bondiana.putInt("MPState",state);
                    intent.putExtra("Bundle", Bondiana);
                    intent.setAction(action);
                    startService(intent);
                    break;
                default:
                    throw new IllegalArgumentException("No such action: " + action);
            }

            finish();
        }
    }
