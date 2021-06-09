package com.ntxq.btl_ptudpm.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ntxq.btl_ptudpm.API;
import com.ntxq.btl_ptudpm.R;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private TextView tvAdvice;
    private TextView btnTarot;
    private TextView btnZodiac;
    private TextView btnFactNumber;
    private LinearLayout viewChuckNorris;
    private LinearLayout viewFact;
    private TextView tvJoke;
    private TextView tvFact;
    private TextView tvName;

    private ImageView avatar;

    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storageReference = FirebaseStorage.getInstance().getReference();
        initView();
        getChuckNorrisJoke();
        getUselessFact();
        new RenewAdvice().start();
    }

    private void initView() {
        tvAdvice = getActivity().findViewById(R.id.tv_advice);
        tvAdvice.setSelected(true);
        btnTarot = getActivity().findViewById(R.id.btn_tarot);
        btnZodiac = getActivity().findViewById(R.id.btn_zodiac);
        btnFactNumber = getActivity().findViewById(R.id.btn_factNumber);
        viewChuckNorris = getActivity().findViewById(R.id.linearLayout_chuck);
        viewFact = getActivity().findViewById(R.id.linearLayout_fact);
        tvJoke = getActivity().findViewById(R.id.tv_joke);
        tvFact = getActivity().findViewById(R.id.tv_fact);
        tvName= getActivity().findViewById(R.id.tv_name);
        MainActivity main= (MainActivity) getActivity();
        tvName.setText(main.getHeaderEmail());

        btnTarot.setOnClickListener(this);
        btnZodiac.setOnClickListener(this);
        btnFactNumber.setOnClickListener(this);
        viewChuckNorris.setOnClickListener(this);
        viewFact.setOnClickListener(this);

        avatar = getActivity().findViewById(R.id.im_avatar);
        avatar.setOnLongClickListener(this);

        StorageReference avatarRef= storageReference.child("avatar.jpg");
        avatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(avatar).load(uri).apply(RequestOptions.circleCropTransform()).into(avatar);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                avatar.setImageResource(R.drawable.ic_baseline_person_24);
            }
        });
    }


    @Override
    public void onClick(View view) {
        MainActivity main = (MainActivity) getActivity();
        switch (view.getId()) {
            case R.id.btn_tarot:
                main.showFragment(MainActivity.TAROT_FRAGMENT);
                break;
            case R.id.btn_zodiac:
                main.showFragment(MainActivity.ZODIAC_FRAGMENT);
                break;
            case R.id.btn_factNumber:
                main.showFragment(MainActivity.NUMBER_FRAGMENT);
            case R.id.linearLayout_chuck:
                getChuckNorrisJoke();
                break;
            case R.id.linearLayout_fact:
                getUselessFact();
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {

        PopupMenu popupMenu = new PopupMenu(getContext(), avatar);
        popupMenu.inflate(R.menu.menu_popup);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.pop_uploadImage:
                        Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST_CODE){
            if (resultCode== RESULT_OK){
                Uri imageUri = data.getData();
//                avatar.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }
        }
    }
    public void uploadImageToFirebase(Uri imageUri) {
        final StorageReference fileRef= storageReference.child("avatar.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(avatar).load(uri).apply(RequestOptions.circleCropTransform()).into(avatar);
                    }
                });
            }
        });
    }

    private void getUselessFact() {
        AndroidNetworking.get(API.API_GET_RANDOM_USELESS_FACT).build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String fact = response.getString("data");
                            tvFact.setText(fact);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void getChuckNorrisJoke() {
        AndroidNetworking.get(API.API_GET_RANDOM_CHUCk_NORRIS_JOKE).build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String joke = response.getString("value");
                            tvJoke.setText(joke);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void getAdvice() {
        AndroidNetworking.get(API.API_GET_RANDOM_ADVICE).build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response.getJSONObject("slip");
                            String advice = object.getString("advice");
                            tvAdvice.setText(advice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


    class RenewAdvice extends Thread {
        @Override
        public void run() {
            while (true) {
                getAdvice();
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
