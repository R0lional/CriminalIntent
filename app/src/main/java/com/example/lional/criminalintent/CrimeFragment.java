package com.example.lional.criminalintent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

@SuppressLint("ValidFragment")
public class CrimeFragment extends Fragment implements View.OnClickListener, TextWatcher {
    private static final String TAG = "Crimefragment";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_IMAGE = 2;
    private static final String DIALOG_DATE = "dialog_date";
    private static final String DIALOG_TIME = "dialog_time";

    public static final String DATE_FORMAT = "EEEE, MMMM dd, HH:mm, yyyy";
    public static final String EXTRA_CRIME_ID = "com.example.lional.criminalintent.crime_id";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private ActionBar mActionBar;
    private ImageButton mImageButton;
    private ImageView mImageView;

    @SuppressLint("ValidFragment")
    private CrimeFragment(ActionBar actionBar) {
        mActionBar = actionBar;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID crimeId = (UUID) getArguments().getSerializable(CrimeFragment.EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    public static CrimeFragment newInstance(UUID crimeId, ActionBar actionBar) {
        Bundle args = new Bundle();
        args.putSerializable(CrimeFragment.EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment(actionBar);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        if (NavUtils.getParentActivityName(getActivity()) != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(this);

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(DateFormat.format(DATE_FORMAT, mCrime.getmDate()));
        mDateButton.setOnClickListener(this);

        mImageButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
        mImageButton.setOnClickListener(this);

        mImageView = v.findViewById(R.id.crime_image_show);

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.ismSolved());

        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.crime_delete, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "requestCode: " + requestCode + ", resultCode: " + resultCode);
        if(requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            if (resultCode == Activity.RESULT_OK) {
                mCrime.setmDate(date);
                mDateButton.setText(DateFormat.format(DATE_FORMAT, date));
            }
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            TimePickerFragment dialog = TimePickerFragment.newInstance(date);
            dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
            dialog.show(fragmentManager, DIALOG_TIME);
        } else if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            if (resultCode == Activity.RESULT_OK) {
                mCrime.setmDate(date);
                mDateButton.setText(DateFormat.format(DATE_FORMAT, date));
            }
        } else if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                String filename = (String) data.getStringExtra(CrimeFragmentCamera.EXTRA_PHOTO_NAME);
                if (filename != null) {
                    Photo photo = new Photo(filename);
                    mCrime.setPhoto(photo);
                    showPhoto();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.crime_delete_item:
            case android.R.id.home:
                if (item.getItemId() == R.id.crime_delete_item) {
                    CrimeLab.get(getActivity()).deleteCrime(mCrime);
                }

                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                break;
            default:
                super.onOptionsItemSelected(item);
                break;
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crime_date:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
                break;
            case R.id.crime_imageButton:
                PackageManager pm = getActivity().getPackageManager();
                boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                        pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
                if (hasCamera &&
                        (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED)) {
                    startCameraPreview();
                } else {
                    if (hasCamera) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                CrimeFragmentCamera.CAMERA_REQUEST_CODE);
                    } else {
                        Toast.makeText(getContext(), R.string.no_camera, Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        Log.d(TAG,"edit string: " + editable.toString());
        mCrime.setmTitle(editable.toString());
    }

    private void startCameraPreview() {
        Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, mCrime.getmId());
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[CrimeFragmentCamera.CAMERA_REQUEST_CODE]
                == PackageManager.PERMISSION_GRANTED) {
            startCameraPreview();
        }
    }

    private void showPhoto() {
        BitmapDrawable bitmapDrawable = null;
        Photo photo = mCrime.getmPhoto();
        if (photo != null) {
            Log.d(TAG, "filename: " + photo.getFileName());
            String path = getActivity().getFileStreamPath(photo.getFileName()).getAbsolutePath();
            Log.d(TAG, "path: " + path);
            bitmapDrawable = PhotoUtils.getScaledDrawable(getActivity(), path);
        }
        mImageView.setImageDrawable(bitmapDrawable);
    }
}
