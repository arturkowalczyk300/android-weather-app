package com.arturkowalczyk300.mvvmlivedataexampleproject.View;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.arturkowalczyk300.mvvmlivedataexampleproject.R;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class SublimePickerDialogFragment extends DialogFragment {
    DateFormat mDateFormatter, mTimeFormatter;
    SublimePicker mSublimePicker;
    Callback mCallback;

    SublimeListenerAdapter mListener = new SublimeListenerAdapter() {
        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker, SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
            if (mCallback != null) {
                mCallback.onDateTimeRecurrenceSet(selectedDate, hourOfDay, minute, recurrenceOption, recurrenceRule);
            }
            dismiss();
        }

        @Override
        public void onCancelled() {
            if (mCallback != null) {
                mCallback.onCancelled();
            }
            dismiss();
        }
    };

    public SublimePickerDialogFragment() {
        //init formatters
        mDateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        mTimeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        mTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    //set activity callback
    public void setCallback(Callback callback){ mCallback = callback;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSublimePicker = (SublimePicker) getActivity()
                .getLayoutInflater().inflate(R.layout.sublime_picker, container);

        //retrieve SublimeOptions
        Bundle arguments = getArguments();
        SublimeOptions options = null;

        //options can be null, if so, default options are used
        if(arguments != null){
            options = arguments.getParcelable("SUBLIME_OPTIONS");
        }

        mSublimePicker.initializePicker(options, mListener);

        return mSublimePicker;
    }

    //for commnunicating with the activity
    public interface Callback {
        void onCancelled();

        void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                     int hourOfDay, int minute,
                                     SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                     String recurrenceRule);
    }
}
