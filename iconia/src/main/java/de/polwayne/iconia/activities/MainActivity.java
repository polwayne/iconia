package de.polwayne.iconia.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.capricorn.ArcMenu;

import de.polwayne.iconia.R;
import de.polwayne.iconia.adapter.DrawerAdapter;
import de.polwayne.iconia.fragments.FoldableListFragment;
import de.polwayne.iconia.fragments.MainFragment;
import de.polwayne.iconia.pojo.Item;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private FoldableListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();
        if(savedInstanceState != null)
            return;
        initFragment();
    }

    private void initDrawer(){
        String[] mMenuTitles = getResources().getStringArray(R.array.nav_drawer);
        int[] icons = {R.drawable.earth, R.drawable.head, R.drawable.clouds, R.drawable.vintage,R.drawable.cogs, R.drawable.light};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new DrawerAdapter(this, mMenuTitles,icons));
        mDrawerList.setOnItemClickListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.app_name,  /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */
        );
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void initFragment(){
        mListFragment = new FoldableListFragment();
        getFragmentManager().beginTransaction().add(R.id.content_frame,mListFragment).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if(mDrawerToggle != null)
            mDrawerToggle.syncState();
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        switch (id){
            case R.id.action_add:
                Intent i = new Intent(this,CreateItemActivity.class);
                startActivity(i);
                return true;
            case R.id.action_settings:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if(mListFragment != null && mListFragment.onBackPressed())
            return;
        super.onBackPressed();
    }

    // Navigation Drawer ItemClicks
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0) {
            mListFragment.filterCategory(-1);
        }
        else if(position == 1) {
            mListFragment.filterCategory(Item.CATEGORY_THOUGHT);
        }
        else if(position == 2) {
            mListFragment.filterCategory(Item.CATEGORY_DREAM);
        }
        else if(position == 3) {
            mListFragment.filterCategory(Item.CATEGORY_MEMORY);
        }
        else if(position == 4){
            Intent i = new Intent(this,PreferencesActivity.class);
            startActivity(i);
        }
        mDrawerLayout.closeDrawers();
    }
}
