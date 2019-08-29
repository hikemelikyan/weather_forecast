package weatherforcaster.doit.myweatherforcaster.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import weatherforcaster.doit.myweatherforcaster.R;
import weatherforcaster.doit.myweatherforcaster.model.FiveDayThreeHourModel.AllList;

public class FiveDayAdapter extends RecyclerView.Adapter<FiveDayAdapter.ViewHolder> {

    private Context mContext;
    private List<AllList> mList;
    private int mCount;

    public FiveDayAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setData(List<AllList> mListToAdd, int mCount) {
        mList.clear();
        mList.addAll(mListToAdd);
        this.mCount = mCount;
        notifyDataSetChanged();
    }

    private AllList getItem(int position) {
        return mList.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_style, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mDayTemp.setText(String.valueOf(getItem(i).getMain().getTempMax()));
        viewHolder.mNightTemp.setText(String.valueOf(getItem(i).getMain().getTempMin()));
        viewHolder.mWind.setText(String.valueOf(getItem(i).getWind().getSpeed()));
        viewHolder.mHumidity.setText(String.valueOf(getItem(i).getMain().getHumidity()));
        viewHolder.mPressure.setText(String.valueOf(getItem(i).getMain().getPressure()));
        viewHolder.mTime.setText(getItem(i).getDtTxt().substring
                (getItem(i).getDtTxt().length()-8,getItem(i).getDtTxt().length()-3));
        switch (getItem(i).getWeather().get(0).getIcon()) {
            case "01d":
                Picasso.get().load(R.drawable.sunny).into(viewHolder.mImageIllustration);
                break;
            case "02d":
                Picasso.get().load(R.drawable.sunny_cloud).into(viewHolder.mImageIllustration);
                break;
            case "03d":
                Picasso.get().load(R.drawable.cloud).into(viewHolder.mImageIllustration);
                break;
            case "04d":
                Picasso.get().load(R.drawable.dark_cloud).into(viewHolder.mImageIllustration);
                break;
            case "09d":
                Picasso.get().load(R.drawable.dark_rainy).into(viewHolder.mImageIllustration);
                break;
            case "10d":
                Picasso.get().load(R.drawable.sunny_rainy).into(viewHolder.mImageIllustration);
                break;
            case "11d":
                Picasso.get().load(R.drawable.storm).into(viewHolder.mImageIllustration);
                break;
            case "13d":
                Picasso.get().load(R.drawable.snowy).into(viewHolder.mImageIllustration);
                break;
            case "50d":
                Picasso.get().load(R.drawable.mist).into(viewHolder.mImageIllustration);
                break;
            case "01n":
                Picasso.get().load(R.drawable.night).into(viewHolder.mImageIllustration);
                break;
            case "02n":
                Picasso.get().load(R.drawable.night_cloud).into(viewHolder.mImageIllustration);
                break;
            case "03n":
                Picasso.get().load(R.drawable.cloud).into(viewHolder.mImageIllustration);
                break;
            case "04n":
                Picasso.get().load(R.drawable.dark_cloud).into(viewHolder.mImageIllustration);
                break;
            case "09n":
                Picasso.get().load(R.drawable.dark_rainy).into(viewHolder.mImageIllustration);
                break;
            case "11n":
                Picasso.get().load(R.drawable.storm).into(viewHolder.mImageIllustration);
                break;
            case "13n":
                Picasso.get().load(R.drawable.snowy).into(viewHolder.mImageIllustration);
                break;
            case "50n":
                Picasso.get().load(R.drawable.mist).into(viewHolder.mImageIllustration);
                break;
            case "10n":
                Picasso.get().load(R.drawable.sunny_rainy).into(viewHolder.mImageIllustration);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mPressure;
        private TextView mDayTemp;
        private TextView mNightTemp;
        private TextView mWind;
        private TextView mHumidity;
        private TextView mTime;
        private ImageView mImageIllustration;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mHumidity = itemView.findViewById(R.id.id_field_humidity);
            mPressure = itemView.findViewById(R.id.id_field_pressure);
            mWind = itemView.findViewById(R.id.id_field_wind);
            mTime = itemView.findViewById(R.id.id_field_time);
            mDayTemp = itemView.findViewById(R.id.id_day_temperature);
            mNightTemp = itemView.findViewById(R.id.id_night_temperature);
            mImageIllustration = itemView.findViewById(R.id.id_image_illustration);
        }
    }
}
