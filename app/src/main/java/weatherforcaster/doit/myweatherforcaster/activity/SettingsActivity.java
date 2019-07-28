package weatherforcaster.doit.myweatherforcaster.activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import weatherforcaster.doit.myweatherforcaster.Common.Common;
import weatherforcaster.doit.myweatherforcaster.R;
import weatherforcaster.doit.myweatherforcaster.adapter.SettingsAdapter;
import weatherforcaster.doit.myweatherforcaster.models.Settings;
import weatherforcaster.doit.myweatherforcaster.service.NotificationService;

public class SettingsActivity extends AppCompatActivity implements SettingsAdapter.RecyclerClickListener,
        PopupMenu.OnMenuItemClickListener {

    private RecyclerView mRecyclerView;
    private SettingsAdapter mAdapter;
    Settings settings;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InitView();
        editor.putBoolean("created",true);
        Log.d("TESTING","onCreate Settings");
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TESTING","onStart Settings");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationService.NOTIFICATION_ID);
    }

    private void InitView() {
        Log.d("TESTING","InitView Settings");
        settings = new Settings(this);
        mRecyclerView = findViewById(R.id.id_single_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        sharedPreferences = getSharedPreferences(Common.SHARED_PREFERANCE_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mAdapter = new SettingsAdapter(this, this);
        mAdapter.setData(new Settings(this));
        mRecyclerView.setAdapter(mAdapter);
        Toolbar toolbar = findViewById(R.id.id_toolbar_for_settings);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        String previous = sharedPreferences.getString("frequency","");
        if (item.getItemId() == R.id.id_ok) {
            if(mAdapter.getData().getUnitType().equals("°F")){
                editor.putString("unit", "imperial");
            }else if(mAdapter.getData().getUnitType().equals("°C")){
                editor.putString("unit","metric");
            }
            editor.putString("frequency", mAdapter.getData().getFrequency());
            editor.putBoolean("switch", mAdapter.getData().getSwitchPosition());
        }
        if(sharedPreferences.getBoolean("switch",false)){
            if(!previous.equals(mAdapter.getData().getFrequency())){
                Intent serviceIntent = new Intent(SettingsActivity.this,NotificationService.class);
                serviceIntent.putExtra("frequency",mAdapter.getData().getFrequency());
                startService(serviceIntent);
            }
        }
        editor.apply();
        setResult(RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendData(String click) {
        View view;
        PopupMenu popupMenu;
        switch (click) {
            case SettingsAdapter.CLICK_FOR_FREQUENCY:
                view = findViewById(R.id.id_text_frequency);
                popupMenu = new PopupMenu(this, view);
                popupMenu.inflate(R.menu.popup_menu_frequency);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
            case SettingsAdapter.CLICK_FOR_SWITCH:
                Toast.makeText(this, "swi", Toast.LENGTH_SHORT).show();
                break;
            case SettingsAdapter.CLICK_FOR_UNIT:
                view = findViewById(R.id.id_text_unit);
                popupMenu = new PopupMenu(this, view);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.id_popup_celsius:
                settings.setUnitType("°C");
                mAdapter.setData(settings);
                return true;
            case R.id.id_popup_fahrenheit:
                settings.setUnitType("°F");
                mAdapter.setData(settings);
                return true;
            case R.id.id_once_a_day:
                settings.setFrequency("Once a day");
                mAdapter.setData(settings);
                return true;
            case R.id.id_twice_a_day:
                settings.setFrequency("Twice a day");
                mAdapter.setData(settings);
                return true;
            case R.id.id_trice_a_day:
                settings.setFrequency("Trice a day");
                mAdapter.setData(settings);
                return true;
            default:
                mAdapter.setData(settings);
                return true;
        }
    }
}