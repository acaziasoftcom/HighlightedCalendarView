package com.acaziasoft.highlightedcalendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class CalendarView extends LinearLayout {

  private static String LOGTAG = "Calendar View";

  private static final int DAYS_COUNT = 42;

  private static final String DATE_FORMAT = "MMM yyyy";

  private String dateFormat;

  private Calendar currentDate = Calendar.getInstance();

  private EventHandler eventHandler;

  private LinearLayout header;
  private ImageView btnPrev;
  private ImageView btnNext;
  private TextView txtDate;
  private GridView grid;

  int[] rainbow = new int[]{
      R.color.summer,
      R.color.fall,
      R.color.winter,
      R.color.spring
  };

  int[] monthSeasons = new int[]{2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

  public CalendarView(Context context) {
    super(context);
  }

  public CalendarView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initControl(context, attrs);
  }

  public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initControl(context, attrs);
  }

  private void initControl(Context context, AttributeSet attributeSet) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.control_calendar, this);
  }

  private void loadDateFormat(AttributeSet attrs) {
    TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

    try {
      dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
      if (dateFormat == null) {
        dateFormat = DATE_FORMAT;
      }
    } finally {
      ta.recycle();
    }
  }

  private void assignUiElement() {
    header = findViewById(R.id.calendar_header);
    btnPrev = findViewById(R.id.calendar_prev_button);
    btnNext = findViewById(R.id.calendar_next_button);
    txtDate = findViewById(R.id.calendar_date_display);
    grid = findViewById(R.id.calendar_grid);
  }

  private void assignClickHandlers() {
    btnNext.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        currentDate.add(Calendar.MONTH, 1);
        updateCalendar();
      }
    });

    btnPrev.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        currentDate.add(Calendar.MONTH, -1);
      }
    });

    grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (eventHandler == null) {
          return false;
        }
        eventHandler.onDayLongPress((Date) adapterView.getItemAtPosition(i));
        return true;
      }
    });
  }

  private void updateCalendar() {
    updateCalendar(null);
  }

  private void updateCalendar(HashSet<Date> events) {
    ArrayList<Date> cells = new ArrayList<>();
    Calendar calendar = (Calendar) currentDate.clone();
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK - 1);

    calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

    while (cells.size() < DAYS_COUNT) {
      cells.add(calendar.getTime());
      calendar.add(Calendar.DAY_OF_MONTH, 1);

      grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

      SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
      txtDate.setText(sdf.format(currentDate.getTime()));

      // set header color according to current season
      int month = currentDate.get(Calendar.MONTH);
      int season = monthSeasons[month];
      int color = rainbow[season];

      header.setBackgroundColor(getResources().getColor(color));
    }
  }


  public class CalendarAdapter extends ArrayAdapter<Date> {
    private HashSet<Date> eventDays;

    private LayoutInflater inflater;

    public CalendarAdapter(@NonNull Context context, ArrayList<Date> days, HashSet<Date> eventDays) {
      super(context, R.layout.control_calendar_day, days);

      this.eventDays = eventDays;
      inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      Date date = getItem(position);
      int day = date.getDate();
      int month = date.getMonth();
      int year = date.getYear();

      Date today = new Date();

      if (convertView == null) {
        convertView = inflater.inflate(R.layout.control_calendar_day, parent, false);
      }

      convertView.setBackgroundResource(0);

      if (eventDays != null) {
        for (Date eventDate : eventDays) {
          if (eventDate.getDate() == day && eventDate.getMonth() == month &&
              eventDate.getYear() == year) {
            convertView.setBackgroundResource(R.drawable.reminder);
            break;
          }
        }
      }

      ((TextView) convertView).setTypeface(null, Typeface.NORMAL);
      ((TextView) convertView).setTextColor(Color.BLACK);

      if (month != today.getMonth() || year != today.getYear()) {
        // if this day is outside current month, grey it out
        ((TextView) convertView).setTextColor(convertView.getResources().getColor(R.color.greyed_out));
      } else if (day == today.getDate()) {
        ((TextView) convertView).setTypeface(null, Typeface.BOLD);
        ((TextView) convertView).setTextColor(convertView.getResources().getColor(R.color.today));
      }

      ((TextView) convertView).setText(String.valueOf(date.getDate()));

      return convertView;
    }

    private void setEventHandler(EventHandler eh) {
      eventHandler = eventHandler;
    }
  }
}
