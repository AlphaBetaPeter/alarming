/*
 * Alarming, an alarm app for the Android platform
 *
 * Copyright (C) 2014-2015 Peter Mösenthin <peter.moesenthin@gmail.com>
 *
 * Alarming is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.petermoesenthin.alarming.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.markushi.ui.CircleButton;
import de.petermoesenthin.alarming.R;
import de.petermoesenthin.alarming.pref.AlarmGson;
import de.petermoesenthin.alarming.util.StringUtil;

import java.util.List;

public class AlarmCardRecyclerAdapter extends RecyclerView.Adapter<AlarmCardRecyclerAdapter.AlarmCardViewHolder>{

	public static final String DEBUG_TAG = AlarmCardRecyclerAdapter.class.getSimpleName();

	private List<AlarmGson> mAlarms;
	private Context mContext;
	private AdapterCallBacks mAdapterCallBacks;

	public AlarmCardRecyclerAdapter(List<AlarmGson> mAlarms) {
		this.mAlarms = mAlarms;
	}

	public AlarmCardRecyclerAdapter(List<AlarmGson> mAlarms, AdapterCallBacks adapterCallBacks) {
		this.mAdapterCallBacks = adapterCallBacks;
		this.mAlarms = mAlarms;
	}

	public interface AdapterCallBacks {

		void onAlarmTimeClick(AlarmCardViewHolder viewHolder, int position);

		void onAlarmSetClick(AlarmCardViewHolder viewHolder, int position);

		void onVibrateClick(AlarmCardViewHolder viewHolder, int position);

		void onRepeatAlarmClick(AlarmCardViewHolder viewHolder, int position);

		void onAlarmTextClick(AlarmCardViewHolder viewHolder, int position);

		void onChooseColorClick(AlarmCardViewHolder viewHolder, int position);

		void onDeleteAlarmClick(AlarmCardViewHolder viewHolder, int position);
	}

	@Override
	public AlarmCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		mContext = viewGroup.getContext();
		View itemView = LayoutInflater.
				from(mContext).
				inflate(R.layout.card_alarm_time, viewGroup, false);

		return new AlarmCardViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(AlarmCardViewHolder viewHolder, int i) {
		AlarmGson alarm = mAlarms.get(i);
		setAlarmTimeView(viewHolder, alarm.getHour(), alarm.getMinute());

		setCircleButtonActive(viewHolder, alarm.isAlarmSet());

		viewHolder.vibrate.setChecked(alarm.doesVibrate());

		viewHolder.repeatAlarm.setChecked(alarm.doesRepeat());

		showWeekdayPanel(viewHolder,alarm.doesRepeat());

		if (!alarm.getMessage().isEmpty()) {
			viewHolder.alarmText.setText(alarm.getMessage());
		}

		View colorChooser = viewHolder.chooseColor.findViewById(R.id.view_color_indicator);
		int color = alarm.getColor();
		if (color == -1) {
			color = mContext.getResources().getColor(R.color.material_yellow);
		}
		colorChooser.setBackgroundColor(color);

		if(mAdapterCallBacks != null){
			setOnClickListeners(i, viewHolder);
		}
	}

	@Override
	public int getItemCount() {
		return mAlarms.size();
	}

	private void setAlarmTimeView(AlarmCardViewHolder viewHolder, int hour, int minute) {
		String alarmFormatted = StringUtil.getTimeFormattedSystem(mContext, hour,
				minute);
		String[] timeSplit = alarmFormatted.split(" ");
		viewHolder.am_pm.setVisibility(View.INVISIBLE);
		viewHolder.alarmTime.setText(timeSplit[0]);
		if (timeSplit.length > 1) {
			viewHolder.am_pm.setText(timeSplit[1]);
			viewHolder.am_pm.setVisibility(View.VISIBLE);
		}
	}

	public void setCircleButtonActive(AlarmCardViewHolder viewHolder, boolean isActive) {
		if (isActive) {
			viewHolder.alarmSet.setColor(mContext.getResources().getColor(R.color.material_yellow));
			viewHolder.alarmSet.setImageResource(R.drawable.ic_bell_ring);
		} else {
			viewHolder.alarmSet.setColor(mContext.getResources().getColor(R.color.veryLightGray));
			viewHolder.alarmSet.setImageResource(R.drawable.ic_bell_outline);
		}
	}

	public void showWeekdayPanel(AlarmCardViewHolder viewHolder, boolean show){
		if(show){
			viewHolder.weekdayPanel.setVisibility(View.VISIBLE);
		}else {
			viewHolder.weekdayPanel.setVisibility(View.GONE);
		}
	}

	private void setOnClickListeners(final int position, final AlarmCardViewHolder viewHolder) {
		viewHolder.alarmTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAdapterCallBacks.onAlarmTimeClick(viewHolder, position);
			}
		});

		viewHolder.alarmSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAdapterCallBacks.onAlarmSetClick(viewHolder, position);
			}
		});

		viewHolder.vibrate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAdapterCallBacks.onVibrateClick(viewHolder, position);
			}
		});

		viewHolder.repeatAlarm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAdapterCallBacks.onRepeatAlarmClick(viewHolder, position);
			}
		});

		viewHolder.alarmText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAdapterCallBacks.onAlarmTextClick(viewHolder, position);
			}
		});

		viewHolder.chooseColor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAdapterCallBacks.onChooseColorClick(viewHolder, position);
			}
		});
		viewHolder.deletAlarm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAdapterCallBacks.onDeleteAlarmClick(viewHolder, position);
			}
		});
	}


	public static class AlarmCardViewHolder extends RecyclerView.ViewHolder {

		public TextView alarmTime;
		public TextView am_pm;
		public CircleButton alarmSet;
		public CheckBox vibrate;
		public CheckBox repeatAlarm;
		public TextView alarmText;
		public LinearLayout chooseColor;
		public ImageView deletAlarm;
		public LinearLayout weekdayPanel;


		public AlarmCardViewHolder(View itemView) {
			super(itemView);

			if (itemView != null) {
				alarmTime = (TextView) itemView.findViewById(R.id.textView_alarmTime);
				am_pm = (TextView) itemView.findViewById(R.id.textView_am_pm);
				alarmSet = (CircleButton) itemView.findViewById(R.id.button_alarm_set);
				vibrate = (CheckBox) itemView.findViewById(R.id.checkBox_vibrate);
				repeatAlarm = (CheckBox) itemView.findViewById(R.id.checkBox_repeat_alarm);
				alarmText = (TextView) itemView.findViewById(R.id.textView_alarmText);
				chooseColor = (LinearLayout) itemView.findViewById(R.id.layout_choose_color);
				deletAlarm = (ImageView) itemView.findViewById(R.id.button_deleteAlarm);
				weekdayPanel = (LinearLayout) itemView.findViewById(R.id.layout_weekday_panel);
			}
		}
	}
	
}
