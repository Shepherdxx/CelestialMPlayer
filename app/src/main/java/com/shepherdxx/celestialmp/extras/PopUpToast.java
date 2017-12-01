package com.shepherdxx.celestialmp.extras;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Shepherdxx on 07.11.2017.
 */

public class PopUpToast extends Toast {

    private static Toast popUpToast=null;
    private Context context;

    public PopUpToast(Context context) {
        super(context);
        this.context=context;
    }

    public void setMessage(
                      String message){
        if (popUpToast!=null){popUpToast.cancel();popUpToast=null;}
        if (message!=null) {
            popUpToast = Toast.makeText(context,message, Toast.LENGTH_SHORT);
            popUpToast.show();}
    }

}
