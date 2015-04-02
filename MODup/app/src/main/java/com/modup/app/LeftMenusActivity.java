package com.modup.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.modup.adapter.DrawerAdapter;
import com.modup.fragment.*;
import com.modup.model.DrawerItem;
import com.modup.model.SingleWorkout;
import com.modup.utils.ImageUtil;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class LeftMenusActivity extends ActionBarActivity implements FeedFragment.OnFragmentInteractionListener, UserFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener, CreateFragment.OnFragmentInteractionListener, TimersFragment.OnFragmentInteractionListener,
        CalendarFragment.OnFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener, SingleWorkoutItemDetailFragment.OnFragmentInteractionListener {

    public static final String LEFT_MENU_OPTION = "com.modup.app.LeftMenusActivity";
    public static final String LEFT_MENU_OPTION_1 = "Left Menu Option 1";
    public static final String LEFT_MENU_OPTION_2 = "Left Menu Option 2";
    private String TAG = LeftMenusActivity.class.getCanonicalName();

    private ListView mDrawerList;
    private List<DrawerItem> mDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private SingleWorkout currentSingleWorkoutObject;
    private ParseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_menus);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        currentUser = ParseUser.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_view);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        prepareNavigationDrawerItems();
        setAdapter();
        //mDrawerList.setAdapter(new DrawerAdapter(this, mDrawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            mDrawerLayout.closeDrawer(mDrawerList);
            FragmentManager fragmentManager = getFragmentManager();
            Fragment mFragment = new FeedFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();

        }
    }

    private void setAdapter() {
        String option = LEFT_MENU_OPTION_1;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(LEFT_MENU_OPTION)) {
            option = extras.getString(LEFT_MENU_OPTION, LEFT_MENU_OPTION_1);
        }

        boolean isFirstType = true;

        View headerView = null;
        if (option.equals(LEFT_MENU_OPTION_1)) {
            headerView = prepareHeaderView(R.layout.header_navigation_drawer_1);
        } else if (option.equals(LEFT_MENU_OPTION_2)) {
            headerView = prepareHeaderView(R.layout.header_navigation_drawer_2);
            isFirstType = false;
        }

        BaseAdapter adapter = new DrawerAdapter(this, mDrawerItems, isFirstType);

        mDrawerList.addHeaderView(headerView);//Add header before adapter (for pre-KitKat)
        mDrawerList.setAdapter(adapter);
    }

    private View prepareHeaderView(int layoutRes) {
        View headerView = getLayoutInflater().inflate(layoutRes, mDrawerList, false);
        final ImageView iv = (ImageView) headerView.findViewById(R.id.imageViewProfilePic);
        TextView tv = (TextView) headerView.findViewById(R.id.email);

        //needed to get User profile pic for parse feed
        try{
            currentUser.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    try {
                        final byte[] mBytes = parseObject.getBytes("photo");
                        if (mBytes != null) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);
                                    iv.setImageBitmap(bitmap);
                                }
                            });
                        }
                    } catch (Exception f) {
                        Log.e(TAG, f.getMessage());
                    }
                }
            });
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        tv.setText(ParseUser.getCurrentUser().getEmail());

        return headerView;
    }

    private void prepareNavigationDrawerItems() {
        mDrawerItems = new ArrayList<DrawerItem>();
        mDrawerItems.add(
                new DrawerItem(
                        R.drawable.feed,
                        R.string.drawer_title_feed,
                        DrawerItem.DRAWER_ITEM_TAG_FEED));
        mDrawerItems.add(
                new DrawerItem(
                        R.drawable.user,
                        R.string.drawer_title_user,
                        DrawerItem.DRAWER_ITEM_TAG_USER));
        mDrawerItems.add(
                new DrawerItem(
                        R.drawable.stopwatch,
                        R.string.drawer_title_timers,
                        DrawerItem.DRAWER_ITEM_TAG_TIMERS));
        mDrawerItems.add(
                new DrawerItem(
                        R.drawable.calendar,
                        R.string.drawer_title_calendar,
                        DrawerItem.DRAWER_ITEM_TAG_CALENDAR));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position/*, mDrawerItems.get(position - 1).getTag()*/);
        }
    }

    //set up fragment switching here
    private void selectItem(int position/*, int drawerTag*/) {
        // minus 1 because we have header that has 0 position
        FragmentManager fragmentManager = getFragmentManager();
        Fragment mFragment;
        if (position < 1) { //because we have header, we skip clicking on it
            return;
        }
        switch (position) {
            case 1:
                mFragment = new FeedFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();
                break;
            case 2:
                mFragment = new UserFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();
                break;
            case 3:
                mFragment = new TimersFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();
                break;
            case 4:
                mFragment = new CalendarFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();
                break;
        }
/*		String drawerTitle = getString(mDrawerItems.get(position - 1).getTitle());
        Toast.makeText(this, "You selected " + drawerTitle + " at position: " + position, Toast.LENGTH_SHORT).show();*/

        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerItems.get(position - 1).getTitle());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
