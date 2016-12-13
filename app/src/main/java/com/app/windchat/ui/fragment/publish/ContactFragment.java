package com.app.windchat.ui.fragment.publish;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.windchat.R;
import com.app.windchat.api.model.User;
import com.app.windchat.api.rest.Api;
import com.app.windchat.ui.adapter.list.ContactCheckAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link onGetIdsListener} interface
 * to handle interaction events.
 */
public class ContactFragment extends Fragment implements ContactCheckAdapter.onIdsChangedListener {

    private onGetIdsListener mListener;
    private ListView list;
    private Toolbar toolbar;
    private LinearLayout bottom_bar;
    private ImageView btn_send;
    private TextView nb_contact;
    private ContactCheckAdapter adapter;
    private View root;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_contact, container, false);
        initViews();
        return root;
    }

    private void initViews(){
        list = (ListView) root.findViewById(R.id.list);
        toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        bottom_bar = (LinearLayout) root.findViewById(R.id.bottom_bar);
        nb_contact = (TextView) root.findViewById(R.id.nb_contact);
        btn_send = (ImageView) root.findViewById(R.id.btn_send);
        toolbar.setTitle("My friends");
        adapter = new ContactCheckAdapter(getActivity(),new ArrayList<User>(), this);
        list.setAdapter(adapter);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onGetIds(adapter.getIds());
            }
        });
        query();
    }

    private void query(){
        Call<JsonElement> call = new Api().getRestClient().get_rawfriends();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonArray array = response.body().getAsJsonArray();
                if (array!=null) {
                    for (JsonElement e : array) {
                        User user = new Gson().fromJson(e, User.class);
                        user.apply();
                        adapter.add(user);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onGetIdsListener) {
            mListener = (onGetIdsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onIdsChanged(ArrayList<Integer> ids) {
        if (ids.size() == 0){
            bottom_bar.setVisibility(View.GONE);
            return;
        }
        bottom_bar.setVisibility(View.VISIBLE);
        String contact = String.valueOf(ids.size()) + " conctacts";
        nb_contact.setText(contact);
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
    public interface onGetIdsListener {
        // TODO: Update argument type and name
        void onGetIds(ArrayList<Integer> ids);
    }
}
