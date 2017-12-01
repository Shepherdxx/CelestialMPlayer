package com.shepherdxx.celestialmp.extras;

import com.shepherdxx.celestialmp.plailist.PlayListInfo;
import com.shepherdxx.celestialmp.plailist.MyTrackInfo;

/**
 * Created by Shepherdxx on 30.11.2017.
 */

public interface FragmentListener {
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    void onPlaylistClick (PlayListInfo item);
    void onTrackClick (MyTrackInfo item, int position);
    void onTrackClick (MyTrackInfo.DummyItem item);
}
