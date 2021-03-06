package org.pathfinderfr.app.character;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.pathfinderfr.R;


public class FragmentDeleteAction extends DialogFragment implements View.OnClickListener {

    public static final String ARG_NAME = "argName";

    private FragmentDeleteAction.OnFragmentInteractionListener mListener;
    private String name;

    public FragmentDeleteAction() {
        // Required empty public constructor
    }

    public void setListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentAbilityPicker.
     */
    public static FragmentDeleteAction newInstance(OnFragmentInteractionListener listener) {
        FragmentDeleteAction fragment = new FragmentDeleteAction();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_NAME)) {
            name = getArguments().getString(ARG_NAME);
        }

        // restore value that was selected
        if(savedInstanceState != null) {
            name = savedInstanceState.getString(ARG_NAME, name);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sheet_main_deletecharacter, container, false);

        TextView title = rootView.findViewById(R.id.delete_character_title);
        String template = getResources().getString(R.string.sheet_character_delete);
        title.setText(String.format(template, name == null ? "" : name));

        EditText text = rootView.findViewById(R.id.sheet_character_name);
        text.requestFocus();
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(text, 0);

        rootView.findViewById(R.id.name_cancel).setOnClickListener(this);
        rootView.findViewById(R.id.name_ok).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(),0);

        // apply button was pressed!
        if(v.getId() == R.id.name_cancel) {
            dismiss();
            return;
        } else if(v.getId() == R.id.name_ok) {
            if(mListener != null) {
                String confirm = ((EditText)getView().findViewById(R.id.sheet_character_name)).getText().toString();
                if("oui".equalsIgnoreCase(confirm) || confirm.equalsIgnoreCase(name)) {
                    mListener.onDelete();
                    dismiss();
                }
            }
            return;
        }
    }

    public interface OnFragmentInteractionListener {
        void onDelete();
    }
}

