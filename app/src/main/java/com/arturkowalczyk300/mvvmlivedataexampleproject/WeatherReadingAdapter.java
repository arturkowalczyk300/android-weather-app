package com.arturkowalczyk300.mvvmlivedataexampleproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WeatherReadingAdapter extends ListAdapter<WeatherReading, WeatherReadingAdapter.WeatherReadingHolder> {
    private OnItemClickListener listener;

    public WeatherReadingAdapter() {
        super(DIFF_CALLBACK);
    }


    private static final DiffUtil.ItemCallback<WeatherReading> DIFF_CALLBACK = new DiffUtil.ItemCallback<WeatherReading>() {
        @Override
        public boolean areItemsTheSame(@NonNull WeatherReading oldItem, @NonNull WeatherReading newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull WeatherReading oldItem, @NonNull WeatherReading newItem) {
            return //oldItem.getReadTime().equals(newItem.getReadTime()) && // TODO: implement readTime back
                    oldItem.getTemperature() == newItem.getTemperature() &&
                    oldItem.getPressure() == newItem.getPressure() &&
                    oldItem.getHumidity() == newItem.getHumidity();
        }
    };

    @NonNull
    @Override
    public WeatherReadingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_reading_item, parent, false);
        return new WeatherReadingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherReadingHolder holder, int position) {
        WeatherReading currentWeatherReading = getItem(position);
        //holder.textViewReadTime.setText(currentWeatherReading.getReadTime().toString()); //todo: fix readTime handle, it was on-launch crash reason
        holder.textViewTemperature.setText(Float.toString(currentWeatherReading.getTemperature()));
        holder.textViewPressure.setText(Float.toString(currentWeatherReading.getPressure()));
        holder.textViewHumidity.setText(Float.toString(currentWeatherReading.getHumidity()));
    }

    public WeatherReading getWeatherReadingAt(int position) {
        return getItem(position);
    }

    class WeatherReadingHolder extends RecyclerView.ViewHolder {
        private TextView textViewReadTime;
        private TextView textViewTemperature;
        private TextView textViewPressure;
        private TextView textViewHumidity;

        public WeatherReadingHolder(View itemView) {
            super(itemView);
            textViewReadTime = itemView.findViewById(R.id.textView_readTime);
            textViewTemperature = itemView.findViewById(R.id.textView_temperature);
            textViewPressure = itemView.findViewById(R.id.textView_pressure);
            textViewHumidity = itemView.findViewById(R.id.textView_humidity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(WeatherReading weatherReading);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
