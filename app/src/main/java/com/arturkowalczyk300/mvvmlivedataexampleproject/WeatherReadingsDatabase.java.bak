package com.arturkowalczyk300.mvvmlivedataexampleproject;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;
import java.util.Date;

import static com.arturkowalczyk300.mvvmlivedataexampleproject.AddEditWeatherReading.DATE_START_YEAR;


@Database(entities = {WeatherReading.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class WeatherReadingsDatabase extends androidx.room.RoomDatabase {
    private static WeatherReadingsDatabase instance;

    public abstract WeatherDAO weatherDAO(); //lib creates rest by itself

    public static synchronized WeatherReadingsDatabase getInstance(Context context) {
        if (instance == null) //to make it thread-safe
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), WeatherReadingsDatabase.class, "weatherReadings_database")
                    .fallbackToDestructiveMigration() //destroy whole database, when version will increase
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private WeatherDAO weatherDAO;
        private PopulateDbAsyncTask(WeatherReadingsDatabase db){ weatherDAO = db.weatherDAO(); }

        @Override
        protected Void doInBackground(Void... voids)
        {
            Date date1 = new Date(2021-DATE_START_YEAR, 8, 6, 20, 11);
            weatherDAO.insert(new WeatherReading(date1, 20.0f, 1023, 80));
            weatherDAO.insert(new WeatherReading(Calendar.getInstance().getTime(), 25.0f, 1030, 75));
            weatherDAO.insert(new WeatherReading(Calendar.getInstance().getTime(), 30.0f, 1033, 70));
            return null;
        }
    }

}
