package com.example.witsbuses;

import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SelectBusStops extends Activity implements OnItemSelectedListener {

	Spinner startSpinner;
	Spinner EndSpinner;
	List<String> newBusStops = new LinkedList<String>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectbusstopsform);
		startSpinner = (Spinner) findViewById(R.id.StopOneSelection);
		EndSpinner = (Spinner) findViewById(R.id.StopTwoSelection);
		startSpinner.setOnItemSelectedListener(this);
		EndSpinner.setOnItemSelectedListener(this);
		String[] busStops = getResources().getStringArray(R.array.BusStops);
		for (String s : busStops) {
			newBusStops.add(s);
		}
	}

	@SuppressLint("ShowToast")
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		int s = startSpinner.getSelectedItemPosition();
		String startStop = startSpinner.getSelectedItem().toString();
		int e = EndSpinner.getSelectedItemPosition();
		String EndStop = EndSpinner.getSelectedItem().toString();

		if (newBusStops.contains((Object) startStop)) {
			newBusStops.clear();
			String[] busStops = getResources().getStringArray(R.array.BusStops);
			for (String name : busStops) {
				if (!(name.equals(startStop))) {
					newBusStops.add(name);
					if (name.equals(EndStop)) {
						e = newBusStops.indexOf(name);
					}
				}
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, newBusStops);
			EndSpinner.setAdapter(adapter);
			try {
				EndSpinner.setSelection(e);
			} catch (Exception Error) {
				EndSpinner.setSelection(0);
			}

		}
		TextView t = (TextView) findViewById(R.id.textView2);
		t.setText(startStop + " to " + EndStop);

		if (startStop.equals("Esselen")) {
			startStop = "esselen";

			DBAdapter db = new DBAdapter(this);

			db.open();
			// db.insertRecord("09h00", "09h05", "09h10", "09h15", "09h25",
			// "10h50");
			 //Cursor cursor = db.getAllRecords();
			Cursor cursor = db.getRecord(startStop, "wEC");
			if (cursor.moveToFirst()) {
				do {
					Log.d("results",
							cursor.getString(cursor.getColumnIndex("wEC")));
				} while (cursor.moveToNext());
			} else
				Toast.makeText(this, "empty cursor", Toast.LENGTH_LONG);
			cursor = db.getRecord("esselen", "wEC");
			db.close();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		//
	}

	public void getData(Cursor C) {

	}
}
