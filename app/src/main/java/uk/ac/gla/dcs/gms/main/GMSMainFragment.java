package uk.ac.gla.dcs.gms.main;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by ito.
 */
public abstract class GMSMainFragment extends Fragment{

    public static final String ARG_SECTION = "section";


    @Override
    public void onAttach(Activity activity) {
        //this fragment have been attached to activity, if supported, notify it
        if (activity instanceof GMSMainFragmentCallbacks){
            if (getArguments().containsKey(ARG_SECTION)){
                ((GMSMainFragmentCallbacks) activity).onSectionAttached(getArguments().getString(ARG_SECTION));
            }
            else
                ((GMSMainFragmentCallbacks) activity).onSectionAttached("");
        }
        super.onAttach(activity);
    }
}
