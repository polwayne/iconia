package de.polwayne.iconia.fragments;



import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.polwayne.iconia.R;
import de.polwayne.iconia.database.DatabaseTables;
import de.polwayne.iconia.database.IconiaProvider;

/**
 * A Preference {@link android.preference.PreferenceFragment} subclass.
 *
 */
public class PrefsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        Preference p = findPreference("pref_key_create_backup");
        p.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals("pref_key_create_backup")){
           Cursor c = getActivity().getContentResolver().query(IconiaProvider.ITEMS_URI, DatabaseTables.ItemColumns.ALL_COLLUMNS,null,null,DatabaseTables.ItemColumns.TIMESTAMP + " DESC");
            int id_index = c.getColumnIndex(DatabaseTables.ItemColumns.ID);
            int time_index = c.getColumnIndex(DatabaseTables.ItemColumns.TIMESTAMP);
            int title_index = c.getColumnIndex(DatabaseTables.ItemColumns.TITLE);
            int details_index = c.getColumnIndex(DatabaseTables.ItemColumns.DETAILS);
            int data_index = c.getColumnIndex(DatabaseTables.ItemColumns.DATA);
            int category_index = c.getColumnIndex(DatabaseTables.ItemColumns.CATEGORY);

           while (c.moveToNext()){
                long id = c.getLong(id_index);
                long time = c.getLong(time_index);
                String title = c.getString(title_index);
                String details = c.getString(details_index);
                String data = c.getString(data_index); // maybe copy image also into backup
                int category = c.getInt(category_index);
               // TODO create backup

           }
            c.close();

            return true;
        }
        return false;
    }
}
