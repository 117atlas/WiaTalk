package ensp.reseau.wiatalk.ui.activities;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.app.UpdateReceiver;
import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.app.WiaTalkService;
import ensp.reseau.wiatalk.app.WiaTalkServiceRunnerBroadcastReceiver;
import ensp.reseau.wiatalk.localstorage.LocalStorageDiscussions;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.network.NetworkAPI;
import ensp.reseau.wiatalk.network.NetworkUtils;
import ensp.reseau.wiatalk.network.UserInterface;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.fragment.CallsFragment;
import ensp.reseau.wiatalk.ui.fragment.DiscussionsFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements WiaTalkService.IUpdates{

    private View navHeader;
    private DrawerLayout main;
    private NavigationView navigationView;
    private CircleImageView pp;
    private TextView username;
    private TextView usermobile;

    private ViewPager viewPager;
    private TabLayout tabs;
    private SearchView searchView;
    private Toolbar toolbar;

    private DiscussionsFragment messagesFragment;
    private CallsFragment callsFragment;


    private WiaTalkService wiaTalkService;
    private Intent wiaTalkServiceIntent;

    private UpdateReceiver updateReceiver;
    private boolean pause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabs();
        initDrawer();

        setServiceIntent();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getUpdates();
            }
        }, 5000);*/

        onUpdate();
    }

    private void initTabs(){
        viewPager = findViewById(R.id.viewpager);
        tabs = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        messagesFragment = new DiscussionsFragment();
        callsFragment = new CallsFragment();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(messagesFragment, getResources().getString(R.string.tab_messages));
        adapter.addFragment(callsFragment, getResources().getString(R.string.tab_calls));
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }

    public void initDrawer(){
        main = findViewById(R.id.activity_main);
        navigationView = findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
        pp = navHeader.findViewById(R.id.pp);
        username = navHeader.findViewById(R.id.username);
        usermobile = navHeader.findViewById(R.id.usermobile);

        //Update photo, name, mobile

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.myprofile : {
                        Toast.makeText(MainActivity.this, "My profile", Toast.LENGTH_SHORT).show();
                        UiUtils.switchActivity(MainActivity.this, ProfileActivity.class, false, null);
                    } break;
                    case R.id.newgroup : {
                        Toast.makeText(MainActivity.this, "New Group", Toast.LENGTH_SHORT).show();
                        UiUtils.switchActivity(MainActivity.this, CreateGroupActivity.class, false, null);
                    } break;
                    case R.id.contacts : {
                        Toast.makeText(MainActivity.this, "Contacts", Toast.LENGTH_SHORT).show();
                        UiUtils.switchActivity(MainActivity.this, ContactsActivity.class, false, null);
                    } break;
                    case R.id.markedmessages : {
                        Toast.makeText(MainActivity.this, "Marked Messages", Toast.LENGTH_SHORT).show();
                    } break;
                    case R.id.settings : {
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        UiUtils.switchActivity(MainActivity.this, SettingsActivity.class, false, null);
                    } break;
                    case R.id.share : {
                        Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_SHORT).show();
                    } break;
                    case R.id.help : {
                        Toast.makeText(MainActivity.this, "Help", Toast.LENGTH_SHORT).show();
                    } break;
                    case R.id.report : {
                        Toast.makeText(MainActivity.this, "Report", Toast.LENGTH_SHORT).show();
                    } break;
                }
                return false;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, main, toolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        main.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentListTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentListTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return fragmentListTitles.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.app_bar_search));
        MenuItem searchMenuItem = menu.findItem(R.id.app_bar_search);

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                tabs.setVisibility(View.INVISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                tabs.setVisibility(View.VISIBLE);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                /*if (!((WorkerListAdapter)searchFragment.getRecyclerView().getAdapter()).isEmpty())
                    ((WorkerListAdapter)searchFragment.getRecyclerView().getAdapter()).getFilter().filter(newText);*/
                return true;
            }
        });
        return true;
    }

    private void setServiceIntent(){
        //Set Service Intent
        wiaTalkServiceIntent = new Intent(this, WiaTalkService.class);
        if (isWiatalkServiceRunning()) {
            //Bind to the service
            bindService(wiaTalkServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }else{
            wiaTalkService=new WiaTalkService();
            //Start the service
            startService(wiaTalkServiceIntent);
            //Bind to the service
            bindService(wiaTalkServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private boolean isWiatalkServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : serviceInfos) {
            System.out.println(WiaTalkService.class.getName()+"|"+service.service.getClassName());
            if (WiaTalkService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            wiaTalkService = ((WiaTalkService.MyBinder) service).getService();
            //Set Initial Args
            wiaTalkService.setArg0(0.02);
            wiaTalkService.setMeId(WiaTalkApp.getMe(MainActivity.this).get_Id());
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            wiaTalkService = null;
        }
    };

    @Override
    protected void onDestroy() {
        //UnBind from service
        System.out.println("ON DESTROY ACTIVITY");
        unbindService(serviceConnection);
        //Stop Service
        stopService(wiaTalkServiceIntent);
        //Prepare intent to broadcast reciver
        Intent intent = new Intent(MainActivity.this,WiaTalkServiceRunnerBroadcastReceiver.class);
        intent.setAction(WiaTalkServiceRunnerBroadcastReceiver.ACTION_SET_UpdateService);
        intent.putExtra(WiaTalkServiceRunnerBroadcastReceiver.keyVal_arg0, 0.02);
        intent.putExtra(WiaTalkServiceRunnerBroadcastReceiver.keyVal_arg1, "Wiatalk Service");
        //Send broadcast to start UpdateService after the activity ended
        sendBroadcast(intent);
        super.onDestroy();
    }

    private void getUpdates(){
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        User user = WiaTalkApp.getMe(this);
        if (user!=null) {
            Call<UserInterface.GetUserResponse> call = userInterface.updates(user.get_Id());
            call.enqueue(new Callback<UserInterface.GetUserResponse>() {
                @Override
                public void onResponse(Call<UserInterface.GetUserResponse> call, Response<UserInterface.GetUserResponse> response) {
                    if (response.body()==null) {
                        Log.e("GET UPDATES", "Response body is null");
                        return;
                    }
                    if (response.body().isError()){
                        Log.e("GET UPDATES", response.body().getMessage());
                    }
                    else{
                        Log.d("GET UPDATES", "Success");
                        for (Group group: response.body().getUser().getGroups()){
                            LocalStorageDiscussions.storeGroup(group, MainActivity.this);
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserInterface.GetUserResponse> call, Throwable t) {
                    Log.e("GET UPDATE FAILURE", t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onUpdate() {

    }

    /*public class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            boolean update = b.getBoolean("update");
            ArrayList<Group> groups = (ArrayList<Group>) b.getSerializable("Groups");
            if (update && groups!=null){
                messagesFragment.update(groups);
            }

        }
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
        if(updateReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(updateReceiver);
        updateReceiver = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pause) {
            messagesFragment.update();
            pause = false;
        }
        updateReceiver = new UpdateReceiver(new UpdateReceiver.OnUpdateReceived() {
            @Override
            public void onUpdateReceived(ArrayList<Group> groups) {
                messagesFragment.update(groups);
            }
        });
        final IntentFilter intentFilter = new IntentFilter("Update");
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver, intentFilter);
    }
}
