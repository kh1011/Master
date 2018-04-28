package com.yxclient.app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;


import com.yxclient.app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimePickOrderUtils implements OnValueChangeListener,
		OnClickListener {
	private String initDateTime;
	private Context activity;
	private Calendar calendar;
	private NumberPicker hourpicker;
	private NumberPicker minutepicker;
	private NumberPicker datepicker;
	private String[] minuteArrs;
	private String hourStr;
	private String minuteStr;
	private String dateStr;
	private Dialog dialog;
	private TextView startDateTime;
	private TextView tv_time;
	private String[] dayArrays;
	private long currentTimeMillis;
	private int day;

	public DateTimePickOrderUtils(Context activity, String initDateTime,
								  TextView startDateTime, int day) {
		this.startDateTime = startDateTime;
		this.activity = activity;
		this.initDateTime = initDateTime;
		this.day=day;

	}

	public void initPicker() {
		Calendar calendar = Calendar.getInstance();

		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);

		if (50 <= minutes)
			minutes = 0;

		else if (40 <= minutes)
			minutes = 5;

		else if (30 <= minutes)
			minutes = 4;

		else if (20 <= minutes)
			minutes = 3;

		else if (10 <= minutes)
			minutes = 2;

		else
			minutes = 1;

		// 设置日期 day天以内
		dayArrays = new String[day==4?3:day];
		for (int i = 0; i < dayArrays.length; i++) {
			currentTimeMillis = System.currentTimeMillis() + i * 24 * 3600
					* 1000;
			calendar.setTime(new Date(currentTimeMillis));
			dayArrays[i] = calendar.get(Calendar.MONTH) + 1 + "月"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "日";

		}
		currentTimeMillis = System.currentTimeMillis();// 设置当前时间的毫秒值
		datepicker.setOnValueChangedListener(this);
		datepicker.setDisplayedValues(dayArrays);
		datepicker.setMinValue(0);
		datepicker.setMaxValue(dayArrays.length - 1);
		datepicker.setValue(0);
		dateStr = dayArrays[0];// 初始值

		// 设置小时 预约两个小时以后
		hourpicker.setOnValueChangedListener(this);
		hourpicker.setMaxValue(24);
		hourpicker.setMinValue(1);
		if (minutes == 0) {
			hourpicker.setValue(hours + 1);
			hourStr = hours + 1 + "";// 初始值

		} else {
			hourpicker.setValue(hours);
			hourStr = hours +"";// 初始值

		}
		/*if (minutes == 0) {
			hourpicker.setValue(hours + 3);
			hourStr = hours + 3 + "";// 初始值

		} else {
			hourpicker.setValue(hours + 2);
			hourStr = hours + 2 + "";// 初始值

		}*/

		// 设置分钟
		minuteArrs = new String[] { "00", "10", "20", "30", "40", "50" };
		minutepicker.setOnValueChangedListener(this);
		minutepicker.setDisplayedValues(minuteArrs);
		minutepicker.setMinValue(0);
		minutepicker.setMaxValue(minuteArrs.length - 1);
		minutepicker.setValue(minutes);
		minuteStr = minuteArrs[minutes];// 初始值

	}

	/**
	 * 弹出日期时间选择框方法
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Dialog dateTimePicKDialog() {
		View dateTimeLayout = View.inflate(activity, R.layout.common_datetime2,
				null);
		dateTimeLayout.findViewById(R.id.tv_cancel).setOnClickListener(this);
		dateTimeLayout.findViewById(R.id.tv_confirm).setOnClickListener(this);
		tv_time = (TextView) dateTimeLayout.findViewById(R.id.tv_time);
		tv_time.setVisibility(View.GONE);
		datepicker = (NumberPicker) dateTimeLayout
				.findViewById(R.id.datepicker);
		hourpicker = (NumberPicker) dateTimeLayout
				.findViewById(R.id.hourpicker);
		minutepicker = (NumberPicker) dateTimeLayout
				.findViewById(R.id.minuteicker);
		datepicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		hourpicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		minutepicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		if (day==4){
			minutepicker.setVisibility(View.GONE);
		}
		initPicker();
		dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(dateTimeLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity) activity).getWindowManager().getDefaultDisplay()
				.getHeight();
		wl.width = LayoutParams.MATCH_PARENT;
		wl.height = LayoutParams.WRAP_CONTENT;

		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围隐藏
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

		onDateChanged();
		return dialog;
	}

	@SuppressWarnings("deprecation")
	public void onDateChanged() {
		// 获得日历实例
		calendar = Calendar.getInstance();
		calendar.setTime(new Date(currentTimeMillis));
		Date date = calendar.getTime();
		date.setHours(Integer.parseInt(hourStr.equals("24")?"00":hourStr));
		date.setMinutes(Integer.parseInt(minuteStr));
		calendar.setTime(date);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(day==3){
			initDateTime = sdf.format(calendar.getTime()) + " " + hourStr + ":" + minuteStr;
			tv_time.setText(initDateTime);
		}else if (day==4){
			String hourStr_=hourStr.equals("24")?"00":hourStr;
			initDateTime = sdf.format(calendar.getTime()) + " " + hourStr_/* + ":" + minuteStr*/;
			tv_time.setText(initDateTime);
		}


	}

	/**
	 * 截取子串
	 *
	 * @param srcStr
	 *            源串
	 * @param pattern
	 *            匹配模式
	 * @param indexOrLast
	 * @param frontOrBack
	 * @return
	 */
	public static String spliteString(String srcStr, String pattern,
									  String indexOrLast, String frontOrBack) {
		String result = "";
		int loc = -1;
		if (indexOrLast.equalsIgnoreCase("index")) {
			loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置

		} else {
			loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置

		}
		if (frontOrBack.equalsIgnoreCase("front")) {
			if (loc != -1)
				result = srcStr.substring(0, loc); // 截取子串
		} else {
			if (loc != -1)
				result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
		}
		return result;
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		switch (picker.getId()) {
			case R.id.datepicker:
				currentTimeMillis = System.currentTimeMillis() + newVal * 24 * 3600
						* 1000;
				dateStr = dayArrays[newVal];
				onDateChanged();

				break;
			case R.id.hourpicker:
				hourStr = newVal + "";
				onDateChanged();

				break;
			case R.id.minuteicker:
				minuteStr = minuteArrs[newVal];
				onDateChanged();

				break;

			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_cancel:
				dialog.dismiss();
				break;
			case R.id.tv_confirm:

				startDateTime.setText(initDateTime);
				dialog.dismiss();
				break;
			default:
				break;
		}
	}

}
