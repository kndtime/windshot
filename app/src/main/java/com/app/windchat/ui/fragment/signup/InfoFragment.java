package com.app.windchat.ui.fragment.signup;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.app.windchat.R;
import com.app.windchat.Utils;
import com.app.windchat.api.model.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnInfoFilledListener} interface
 * to handle interaction events.
 */
public class InfoFragment extends Fragment {

    private OnInfoFilledListener mListener;
    private View root;

    private User current;
    private Button btn_signup;
    private EditText edit_fname;
    private EditText edit_lname;
    private EditText edit_email;
    private EditText edit_password;

    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance(){
        return new InfoFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_info, container, false);
        current = new User();
        initViews();
        return root;
    }

    private void initViews()
    {
        btn_signup = (Button) root.findViewById(R.id.btn_signup);
        edit_lname = (EditText) root.findViewById(R.id.input_lname);
        edit_fname = (EditText) root.findViewById(R.id.input_fname);
        edit_email = (EditText) root.findViewById(R.id.input_email);
        edit_password = (EditText) root.findViewById(R.id.input_password);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate())
                    mListener.onFragmentInteraction(current);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInfoFilledListener) {
            mListener = (OnInfoFilledListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInfoFilledListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean validate(){
        boolean valid = true;
        String fname = edit_fname.getText().toString();
        String lname = edit_lname.getText().toString();
        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();

        if (fname.isEmpty()){
            valid = false;
            edit_fname.setError(Utils.getString(R.string.empty));
        }

        if (lname.isEmpty()){
            valid = false;
            edit_lname.setError(Utils.getString(R.string.empty));
        }

        if (password.isEmpty() || password.length() < 6){
            valid = false;
            edit_password.setError(Utils.getString(R.string.pass_invalid));
        }

        if (email.isEmpty()){
            valid = false;
            edit_email.setError(Utils.getString(R.string.email_empty));
            return valid;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            edit_email.setError(Utils.getString(R.string.email_invalid));
            return valid;
        }

        if (!isAvailable(email)){
            valid = false;
            edit_email.setError(Utils.getString(R.string.email_taken));
            return valid;
        }

        current.setFirstname(fname);
        current.setLastname(lname);
        current.setEmail(email);
        current.setPassword(password);

        return valid;
    }


    private boolean isAvailable(String email){
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnInfoFilledListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(User user);
    }
}
