package com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;
import java.util.Date;

import static com.arturkowalczyk300.mvvmlivedataexampleproject.View.AddEditWeatherReading.DATE_START_YEAR;


@Database(entities = {WeatherReading.class}, version = 5)
@TypeConverters({Converters.class})
public abstract class WeatherReadingsDatabase extends androidx.room.RoomDatabase {
    private static WeatherReadingsDatabase instance;

    public abstract WeatherDAO weatherDAO(); //lib creates rest by itself

    public static synchronized WeatherReadingsDatabase getInstance(Context context) {
        if (instance == null) //to make it thread-safe
        {
            instance = Room.
                    databaseBuilder(
                            context.getApplicationContext(),
                            WeatherReadingsDatabase.class,
                            "weatherReadings_database")  //NON-NLS
                    .fallbackToDestructiveMigration() //destroy whole database, when version will increase
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private WeatherDAO weatherDAO;

        private PopulateDbAsyncTask(WeatherReadingsDatabase db) {
            weatherDAO = db.weatherDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
