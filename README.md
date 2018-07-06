# Highlighted Calendar View
CalendarView, optimized for seasons


# Installation

Add the library into your project using Gradle. Put it in (normally, it should be) `app/build.gradle`:

``
implementation 'com.acaziasoft.highlightedcalendarview:highlightedcalendarview:1.0.1'
``

# Usage

Use in the XML layout file like so:

```
  <com.acaziasoft.highlightedcalendarview.CalendarView
      app:dateFormat="MMMM yyyy"
      android:layout_width="match_parent"
      android:layout_height="wrap_content" />
```

Where `app:dateFormat` is `SimpleDateFormat` compatible.

# License

MIT