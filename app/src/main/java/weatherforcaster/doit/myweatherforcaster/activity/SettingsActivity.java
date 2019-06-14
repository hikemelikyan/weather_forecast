package weatherforcaster.doit.myweatherforcaster.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import weatherforcaster.doit.myweatherforcaster.R;
import weatherforcaster.doit.myweatherforcaster.adapter.SettingsAdapter;
import weatherforcaster.doit.myweatherforcaster.models.Settings;

public class SettingsActivity extends AppCompatActivity implements SettingsAdapter.RecyclerClickListener, PopupMenu.OnMenuItemClickListener {
    private RecyclerView mRecyclerView;
    private SettingsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InitView();

    }

    private void InitView() {
        mRecyclerView = findViewById(R.id.id_single_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new SettingsAdapter(this, this);
        mAdapter.setData(new Settings());
        mRecyclerView.setAdapter(mAdapter);
        Toolbar toolbar = findViewById(R.id.id_toolbar_for_settings);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
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
        Settings settings = new Settings();
        switch (menuItem.getItemId()) {
            case R.id.id_popup_celsius:
                settings.setUnitType("°C");
                mAdapter.setData(settings);
                return true;
            case R.id.id_popup_kelvin:
                settings.setUnitType("K");
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
