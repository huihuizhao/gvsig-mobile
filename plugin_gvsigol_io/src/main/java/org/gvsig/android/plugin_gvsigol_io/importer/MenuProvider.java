package org.gvsig.android.plugin_gvsigol_io.importer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;


import org.gvsig.android.plugin_gvsigol_io.R;

import eu.geopaparazzi.core.utilities.Constants;
import eu.geopaparazzi.library.network.NetworkUtilities;
import eu.geopaparazzi.library.plugin.PluginService;
import eu.geopaparazzi.library.plugin.types.MenuEntry;
import eu.geopaparazzi.library.plugin.types.MenuEntryList;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;


/**
 * @author Cesar Martinez Izquierdo  (www.scolab.es)
 */
public class MenuProvider extends PluginService {
    private static final String NAME = "SpatialiteImporterMenuProvider";
    private MenuEntryList list = null;
    public MenuProvider() {
        super(NAME);
    }

    public IBinder onBind (Intent intent) {
        if (list==null) {
            list = new MenuEntryList();
            list.addEntry(new CustomMenuEntry());
        }
        return list;
    }


    public class CustomMenuEntry extends MenuEntry {
        @Override
        public String getLabel() {
            return "gvSIG Online";
        }

        @Override
        protected boolean processOnClick(IActivitySupporter starter) {
            Context context = starter.getContext();
            if (!NetworkUtilities.isNetworkAvailable(context)) {
                GPDialogs.infoDialog(context, context.getString(R.string.available_only_with_network), null);
                return false;
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            final String user = preferences.getString(Constants.PREF_KEY_USER, "geopaparazziuser"); //$NON-NLS-1$
            final String pwd = preferences.getString(Constants.PREF_KEY_PWD, "geopaparazzipwd"); //$NON-NLS-1$
            final String url = preferences.getString(Constants.PREF_KEY_SERVER, ""); //$NON-NLS-1$

            if (url.length() == 0 || user.length() == 0 || pwd.length() == 0) {
                GPDialogs.infoDialog(context, getString(R.string.error_set_cloud_settings_gvsigol), null);
                return false;
            }
            return true;
        }
    }

}