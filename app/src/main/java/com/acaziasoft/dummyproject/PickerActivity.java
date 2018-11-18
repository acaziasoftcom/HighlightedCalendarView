package com.acaziasoft.dummyproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.util.Date;

public class PickerActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    new SingleDateAndTimePickerDialog.Builder(this)
        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
          @Override
          public void onDisplayed(SingleDateAndTimePicker picker) {
            Log.e("AAAAA", "Displayed");
          }
        })
        .title("Simple")
        .listener(new SingleDateAndTimePickerDialog.Listener() {
          @Override
          public void onDateSelected(Date date) {
            Log.e("Dataaaaa", date.toString());
          }
        })
        .display();
  }
}
