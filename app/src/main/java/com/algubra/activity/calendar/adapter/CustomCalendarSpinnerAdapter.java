/**
 * 
 */
package com.algubra.activity.calendar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.manager.AppPreferenceManager;

import java.util.List;

/**
 * @author Rijo K Jose
 * 
 */
public class CustomCalendarSpinnerAdapter extends ArrayAdapter<String> {

	/** The hiding item index. */
	private int hidingItemIndex;
	private Context context;

	/**
	 * Instantiates a new custom adapter.
	 *
	 * @param context
	 *            the context
	 * @param textViewResourceId
	 *            the text view resource id
	 * @param objects
	 *            the objects
	 * @param hidingItemIndex
	 *            the hiding item index
	 */
	public CustomCalendarSpinnerAdapter(Context context, int textViewResourceId,
										List<String> objects, int hidingItemIndex) {
		super(context, textViewResourceId, objects);
		this.hidingItemIndex = hidingItemIndex;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.widget.ArrayAdapter#getDropDownView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v = null;
		if (position == hidingItemIndex) {
			TextView tv = new TextView(getContext());
			tv.setTextColor(context.getResources().getColor(android.R.color.white));
			tv.setVisibility(View.GONE);
			v = tv;
			if(AppPreferenceManager.getSchoolSelection(context).equals("ISG")){
				tv.setBackground(context.getResources().getDrawable(R.drawable.list_calendar_selector));
			}else{
				tv.setBackground(context.getResources().getDrawable(R.drawable.list_calendar_selector_blue));

			}
		} else {
			v = super.getDropDownView(position, null, parent);


		}
		return v;
	}
}
