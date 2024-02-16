package com.example.lagarichecklist;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MaddeEkleDialog extends DialogFragment {
    private static final String TAG = "MaddeEkleDialog";

    public interface OnInputListener{
        void sendInput(String input, String inputNumara);
    }
    public OnInputListener mOnInputListener;

    //widgets
    private EditText mInputMadde;
    private EditText mInputNumara;
    private TextView mActionOk, mActionCancel;

    //vars
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_madde_ekle, container, false);
        mActionCancel = view.findViewById(R.id.action_madde_iptal);
        mActionOk = view.findViewById(R.id.action_madde_ekle);
        mInputMadde = view.findViewById(R.id.input_madde);
        mInputNumara = view.findViewById(R.id.input_madde);

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });


        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capturing input");

                String input = mInputMadde.getText().toString();
                String inputNumara = mInputNumara.getText().toString();

//                if(!input.equals("")){
//
//                    //Easiest way: just set the value
//                    ((MainActivity)getActivity()).mInputDisplay.setText(input);
//
//                }

                //"Best Practice" but it takes longer
                mOnInputListener.sendInput(input, inputNumara);

                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}
