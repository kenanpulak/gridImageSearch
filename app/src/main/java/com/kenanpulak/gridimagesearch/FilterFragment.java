package com.kenanpulak.gridimagesearch;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.kenanpulak.gridimagesearch.models.Filter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FilterFragment extends DialogFragment {

    private Spinner mSizeSpinner;
    private Spinner mColorSpinner;
    private Spinner mTypeSpinner;
    private Button mSaveButton;
    private EditText mSiteFilter;
    private Filter mFilter;

    public interface FilterDialogListener {
        void onFinishFilterDialog(Filter filter);
    }

    public FilterFragment() {
        // Empty constructor required for DialogFragment
    }

    public static FilterFragment newInstance(String title) {
        FilterFragment frag = new FilterFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container);

        mFilter = getArguments().getParcelable("filter");

        mSizeSpinner = (Spinner) view.findViewById(R.id.spinnerSizes);
        mColorSpinner = (Spinner) view.findViewById(R.id.spinnerColors);
        mTypeSpinner = (Spinner) view.findViewById(R.id.spinnerType);
        mSaveButton = (Button) view.findViewById(R.id.btnSave);
        mSiteFilter = (EditText) view.findViewById(R.id.etSizeFilter);

        setSpinnerToValue(mSizeSpinner,mFilter.size);
        setSpinnerToValue(mColorSpinner,mFilter.color);
        setSpinnerToValue(mTypeSpinner,mFilter.type);
        mSiteFilter.setText(mFilter.site);

        String title = getArguments().getString("title", "Advanced Filters");
        getDialog().setTitle(title);
        // Show soft keyboard automatically
       // getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Button cancelButton = (Button) view.findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });

        Button saveButton = (Button) view.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                FilterDialogListener listener = (FilterDialogListener) getActivity();

                Filter tempFilter = new Filter();

                if(mSizeSpinner.getSelectedItemPosition() != 0){
                    tempFilter.size = mSizeSpinner.getSelectedItem().toString();
                }
                else{
                    tempFilter.size = null;
                }
                if(mColorSpinner.getSelectedItemPosition() != 0){
                    tempFilter.color = mColorSpinner.getSelectedItem().toString();
                }
                else {
                    tempFilter.color = null;
                }
                if(mTypeSpinner.getSelectedItemPosition() != 0){
                    tempFilter.type = mTypeSpinner.getSelectedItem().toString();
                }
                else{
                    tempFilter.type = null;
                }
                if (mSiteFilter.getText().toString().length() > 0){
                    tempFilter.site = mSiteFilter.getText().toString();
                }
                else{
                    tempFilter.site = null;
                }

                listener.onFinishFilterDialog(tempFilter);
                dismiss();
            }
        });
        return view;
    }

    public void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }
}
