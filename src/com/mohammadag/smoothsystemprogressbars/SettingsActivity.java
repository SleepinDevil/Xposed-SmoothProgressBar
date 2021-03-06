package com.mohammadag.smoothsystemprogressbars;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends Activity {

	private SmoothProgressBar mProgressBar;
	private CheckBox mCheckBoxMirror;
	private CheckBox mCheckBoxReversed;
	private Spinner mSpinnerInterpolators;
	private SeekBar mSeekBarSectionsCount;
	private SeekBar mSeekBarStrokeWidth;
	private SeekBar mSeekBarSeparatorLength;
	private SeekBar mSeekBarSpeed;
	private TextView mTextViewSpeed;
	private TextView mTextViewStrokeWidth;
	private TextView mTextViewSeparatorLength;
	private TextView mTextViewSectionsCount;

	private float mSpeed;
	private int mStrokeWidth;
	private int mSeparatorLength;
	private int mSectionsCount;

	private SettingsHelper mSettingsHelper;
	protected int mColor = Color.parseColor("#33b5e5");
	private ListView mColorsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_custom);

		mSettingsHelper = new SettingsHelper(getApplicationContext());

		mProgressBar = (SmoothProgressBar) findViewById(R.id.progressbar);
		mCheckBoxMirror = (CheckBox) findViewById(R.id.checkbox_mirror);
		mCheckBoxReversed = (CheckBox) findViewById(R.id.checkbox_reversed);
		mSpinnerInterpolators = (Spinner) findViewById(R.id.spinner_interpolator);
		mSeekBarSectionsCount = (SeekBar) findViewById(R.id.seekbar_sections_count);
		mSeekBarStrokeWidth = (SeekBar) findViewById(R.id.seekbar_stroke_width);
		mSeekBarSeparatorLength = (SeekBar) findViewById(R.id.seekbar_separator_length);
		mSeekBarSpeed = (SeekBar) findViewById(R.id.seekbar_speed);
		mTextViewSpeed = (TextView) findViewById(R.id.textview_speed);
		mTextViewSectionsCount = (TextView) findViewById(R.id.textview_sections_count);
		mTextViewSeparatorLength = (TextView) findViewById(R.id.textview_separator_length);
		mTextViewStrokeWidth = (TextView) findViewById(R.id.textview_stroke_width);
		mColorsListView = (ListView) findViewById(R.id.colorListView);

		OnClickListener checkboxListener = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setValues();
			}
		};

		mCheckBoxMirror.setOnClickListener(checkboxListener);
		mCheckBoxReversed.setOnClickListener(checkboxListener);

		mSeekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mSpeed = ((float) progress + 1) / 10;
				mTextViewSpeed.setText("Speed: " + mSpeed);
				if (fromUser)
					setValues();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		mSeekBarSectionsCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mSectionsCount = progress + 1;
				mTextViewSectionsCount.setText("Sections count: " + mSectionsCount);
				if (fromUser)
					setValues();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		mSeekBarSeparatorLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mSeparatorLength = progress;
				mTextViewSeparatorLength.setText(String.format("Separator length: %ddp", mSeparatorLength));
				if (fromUser)
					setValues();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		mSeekBarStrokeWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mStrokeWidth = progress;
				mTextViewStrokeWidth.setText(String.format("Stroke width: %ddp", mStrokeWidth));
				setValues();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		mSeekBarSeparatorLength.setProgress(mSettingsHelper.getProgressSeparatorLength());
		mSeekBarSectionsCount.setProgress(mSettingsHelper.getSectionsCount());
		mSeekBarStrokeWidth.setProgress((int)mSettingsHelper.getStrokeWidth());
		mCheckBoxMirror.setChecked(mSettingsHelper.getMirrored());
		mCheckBoxReversed.setChecked(mSettingsHelper.getReversed());
		mSpeed = mSettingsHelper.getSpeed();
		mSeekBarSpeed.setProgress((int)(mSpeed * 10) - 1);
		mTextViewSpeed.setText("Speed: " + mSpeed);

		mSpinnerInterpolators.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.interpolators)));
		mSpinnerInterpolators.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				setValues();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

		mColorsListView.setAdapter(new ColorArrayAdapter(getApplicationContext(), 0, mSettingsHelper));
		mColorsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				Intent colorIntent = new Intent(SettingsActivity.this, ColorPickerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("title", getTitle().toString());
				bundle.putInt("key", position);
				bundle.putString("color", SettingsHelper.convertToARGB(getItem(position)));
				colorIntent.putExtras(bundle);
				startActivityForResult(colorIntent, position);
			}		
		});

		mColorsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				int[] colors = mSettingsHelper.getProgressBarColors();
				if (position >= colors.length)
					return true;

				boolean afterHeldColor = false;

				int[] colorsNew = new int[colors.length-1];
				for (int i = 0; i < colors.length; i++) {
					if (i != position) {
						if (!afterHeldColor)
							colorsNew[i] = colors[i];
						else
							colorsNew[i-1] = colors[i];
					} else {
						afterHeldColor = true;
					}
				}

				mSettingsHelper.setProgressBarColors(colorsNew);
				((ColorArrayAdapter) mColorsListView.getAdapter()).notifyDataSetChanged();

				setValues();

				return true;
			}
		});
		setValues();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED)
			return;

		String color = "#" + data.getStringExtra("color");
		int position = requestCode;

		int[] colors = mSettingsHelper.getProgressBarColors();
		if (position >= colors.length) {
			int[] colorsNew = new int[position+1];
			for (int i = 0; i < colors.length; i++) {
				colorsNew[i] = colors[i];
			}
			colorsNew[position] = Color.parseColor(color);
			colors = colorsNew;
		} else {
			colors[position] = Color.parseColor(color);
		}

		mSettingsHelper.setProgressBarColors(colors);
		((ColorArrayAdapter) mColorsListView.getAdapter()).notifyDataSetChanged();

		setValues();
		super.onActivityResult(requestCode, resultCode, data);
	}

	public Integer getItem(int position) {
		try {
			return mSettingsHelper.getProgressBarColors()[position];
		} catch (ArrayIndexOutOfBoundsException e) {
			return Color.parseColor("#33b5e5");
		}
	}

	private void setValues() {
		mProgressBar.setSmoothProgressDrawableSpeed(mSpeed);
		mProgressBar.setSmoothProgressDrawableSectionsCount(mSectionsCount);
		mProgressBar.setSmoothProgressDrawableSeparatorLength(dpToPx(mSeparatorLength));
		mProgressBar.setSmoothProgressDrawableStrokeWidth(dpToPx(mStrokeWidth));
		mProgressBar.setSmoothProgressDrawableReversed(mCheckBoxReversed.isChecked());
		mProgressBar.setSmoothProgressDrawableMirrorMode(mCheckBoxMirror.isChecked());

		Interpolator interpolator;
		switch (mSpinnerInterpolators.getSelectedItemPosition()) {
		case 1:
			interpolator = new LinearInterpolator();
			break;
		case 2:
			interpolator = new AccelerateDecelerateInterpolator();
			break;
		case 3:
			interpolator = new DecelerateInterpolator();
			break;
		case 0:
		default:
			interpolator = new AccelerateInterpolator();
			break;
		}

		mProgressBar.setSmoothProgressDrawableInterpolator(interpolator);
		mProgressBar.setSmoothProgressDrawableColors(mSettingsHelper.getProgressBarColors());
	}

	public int dpToPx(int dp) {
		Resources r = getResources();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dp, r.getDisplayMetrics());
		return px;
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_item:
			setValues();
			mSettingsHelper.setSpeed(mSpeed).setSectionCount(mSectionsCount).setStrokeWidth(mStrokeWidth);
			mSettingsHelper.setProgressSeparatorLength(mSeparatorLength).setMirrored(mCheckBoxMirror.isChecked());
			mSettingsHelper.setReversed(mCheckBoxReversed.isChecked()).setProgressBarColor(mColor);
			mSettingsHelper.setProgressBarInterpolator(mProgressBar.getInterpolator());
			return true;
		case R.id.preview_item:
			setValues();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}