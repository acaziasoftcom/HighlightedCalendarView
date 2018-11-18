package com.acaziasoft.dummyproject

import android.app.Activity
import android.os.Bundle
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog

class MainActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setTitle("Calendar View")

    val builder = SingleDateAndTimePickerDialog.Builder(this)

  }
}
