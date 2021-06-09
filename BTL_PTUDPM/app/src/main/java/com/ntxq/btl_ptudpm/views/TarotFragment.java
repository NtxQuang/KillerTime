package com.ntxq.btl_ptudpm.views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ntxq.btl_ptudpm.API;
import com.ntxq.btl_ptudpm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class TarotFragment extends Fragment implements View.OnLongClickListener, View.OnClickListener {
    private ImageView imRandomCard;
    private LinearLayout layoutInfo;
    private TextView name;
    private TextView meaning;
    private TextView desc;
    private TextView guide;

    private boolean hold = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tarot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        imRandomCard = getActivity().findViewById(R.id.im_randomCard);
        imRandomCard.setOnLongClickListener(this);
        imRandomCard.setOnClickListener(this);
        name = getActivity().findViewById(R.id.tv_name);
        meaning = getActivity().findViewById(R.id.tv_meaning);
        desc = getActivity().findViewById(R.id.tv_desc);
        layoutInfo = getActivity().findViewById(R.id.layout_info);
        guide = getActivity().findViewById(R.id.tv_guide);
    }

    @Override
    public void onClick(View view) {
        if(!hold){
            imRandomCard.setRotation(0);
            imRandomCard.setImageResource(R.drawable.tarot_card_back);
            layoutInfo.setVisibility(View.GONE);
            guide.setText(getResources().getString(R.string.hold_the_card));
            hold= true;
        }
    }


    @Override
    public boolean onLongClick(View view) {
        if (hold) {
            AndroidNetworking.get(API.API_GET_RANDOM_TAROT_CARD).build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int rand = new Random().nextInt(2);
                                JSONArray array = response.getJSONArray("cards");
                                JSONObject object = array.getJSONObject(0);
                                String nameShort = object.getString("name_short");
                                String name_ = object.getString("name");
                                String meaning_ = rand == 0 ? object.getString("meaning_up") : object.getString("meaning_rev");
                                String desc_ = object.getString("desc");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        setLayout(rand, nameShort, name_, meaning_, desc_, getResources().getString(R.string.tap_the_card));
                                    }
                                }, 1000);
                                hold= false;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }

        return true;
    }

    public void setLayout(int rand, String nameShort, String name_, String meaning_, String desc_, String guide_) {
        String uri = "@drawable/" + nameShort;
        int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        imRandomCard.setImageDrawable(res);
        if (rand == 0) {
            imRandomCard.setRotation(0);
            name.setText(name_);
        } else {
            imRandomCard.setRotation(180);
            name.setText(name_ + " (reverse)");
        }
        layoutInfo.setVisibility(View.VISIBLE);
        meaning.setText(meaning_);
        desc.setText(desc_);
        guide.setText(guide_);
    }

}
