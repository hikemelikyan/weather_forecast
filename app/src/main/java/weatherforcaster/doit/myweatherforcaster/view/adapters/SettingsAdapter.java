package weatherforcaster.doit.myweatherforcaster.view.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import weatherforcaster.doit.myweatherforcaster.shared.configurations.Common;
import weatherforcaster.doit.myweatherforcaster.R;
import weatherforcaster.doit.myweatherforcaster.model.Settings;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {

    public static final String CLICK_FOR_UNIT = "unit";
    public static final String CLICK_FOR_FREQUENCY = "frequency";
    public static final String CLICK_FOR_SWITCH = "switch";
    private Context mContext;
    private List<Settings> mList;
    RecyclerClickListener mListener;
    SharedPreferences mShared;
    SharedPreferences.Editor editor;

    public interface RecyclerClickListener {
        void sendData(String click);
    }

    public SettingsAdapter(Context mContext, RecyclerClickListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        mList = new ArrayList<>();
        mShared = mContext.getSharedPreferences(Common.SHARED_PREFERANCE_NAME, Context.MODE_PRIVATE);
        editor = mShared.edit();
    }

    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Settings settings = new Settings(mContext);
        return new SettingsViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_recycler_style, viewGroup, false), settings);
    }

    public void setData(Settings data) {
        mList.clear();
        mList.add(data);
        notifyDataSetChanged();
    }

    public Settings getData() {
        return mList.get(0);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder settingsViewHolder, int i) {
        editor.putBoolean("created", false);
        settingsViewHolder.mTextUnitCurrent.setText(getData().getUnitType());
        if (getData().getSwitchPosition()) {
            settingsViewHolder.mLayout.animate().alpha(1).setDuration(250);
            settingsViewHolder.mFrequencyCurrent.setText(getData().getFrequency());
        } else {
            settingsViewHolder.mLayout.animate().alpha(0).setDuration(250);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class SettingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView mTextUnit;
        private TextView mTextUnitCurrent;
        private Switch mSwitch;
        private TextView mFrequency;
        private LinearLayout mLayout;
        private TextView mFrequencyCurrent;
        private boolean isChecked = true;

        public SettingsViewHolder(@NonNull View itemView, Settings settings) {
            super(itemView);
            mTextUnit = itemView.findViewById(R.id.id_text_unit);
            mTextUnitCurrent = itemView.findViewById(R.id.id_text_current_unit);
            mFrequency = itemView.findViewById(R.id.id_text_frequency);
            mFrequencyCurrent = itemView.findViewById(R.id.id_text_current_frequency);
            mLayout = itemView.findViewById(R.id.id_linear_visible_invisible);
            mSwitch = itemView.findViewById(R.id.id_switch);

            mTextUnitCurrent.setText(settings.getUnitType());
            mFrequencyCurrent.setText(settings.getFrequency());
            mSwitch.setChecked(settings.getSwitchPosition());

            mFrequency.setOnClickListener(this);
            mFrequencyCurrent.setOnClickListener(this);
            mTextUnit.setOnClickListener(this);
            mTextUnitCurrent.setOnClickListener(this);
            mSwitch.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.id_text_current_frequency || v.getId() == R.id.id_text_frequency) {
                mListener.sendData(CLICK_FOR_FREQUENCY);
                notifyDataSetChanged();
            } else if (v.getId() == R.id.id_text_unit || v.getId() == R.id.id_text_current_unit) {
                mListener.sendData(CLICK_FOR_UNIT);
                notifyDataSetChanged();
            } else if (v.getId() == R.id.id_switch) {
                getData().setSwitchPosition(!isChecked);
                isChecked = !isChecked;
                setData(getData());
            }
        }
    }
}