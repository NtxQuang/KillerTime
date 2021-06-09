package com.ntxq.btl_ptudpm.views.zodiac;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ntxq.btl_ptudpm.API;
import com.ntxq.btl_ptudpm.NotificationPublisher;
import com.ntxq.btl_ptudpm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ZodiacAstrologyFragment extends Fragment implements View.OnClickListener {

    private SharedPreferences sharedPreferences;

    private ImageView aries;
    private ImageView taurus;
    private ImageView gemini;
    private ImageView cancer;
    private ImageView leo;
    private ImageView virgo;
    private ImageView libra;
    private ImageView scorpio;
    private ImageView sagittarius;
    private ImageView capricorn;
    private ImageView aquarius;
    private ImageView pisces;

    private TextView zodiac;
    private TextView date;
    private TextView horoscope;

    private ImageView btnNotification;
    private LinearLayout layout;

    public final static String SHARED_PREF_IS_NOTIFY = "isNotify";

    private boolean isNotify;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zodiac_astrology, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_IS_NOTIFY, Context.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        aries = getActivity().findViewById(R.id.aries);
        aries.setOnClickListener(this);
        taurus = getActivity().findViewById(R.id.taurus);
        taurus.setOnClickListener(this);
        gemini = getActivity().findViewById(R.id.gemini);
        gemini.setOnClickListener(this);
        cancer = getActivity().findViewById(R.id.cancer);
        cancer.setOnClickListener(this);
        leo = getActivity().findViewById(R.id.leo);
        leo.setOnClickListener(this);
        virgo = getActivity().findViewById(R.id.virgo);
        virgo.setOnClickListener(this);
        libra = getActivity().findViewById(R.id.libra);
        libra.setOnClickListener(this);
        scorpio = getActivity().findViewById(R.id.scorpio);
        scorpio.setOnClickListener(this);
        sagittarius = getActivity().findViewById(R.id.sagittarius);
        sagittarius.setOnClickListener(this);
        capricorn = getActivity().findViewById(R.id.capricorn);
        capricorn.setOnClickListener(this);
        aquarius = getActivity().findViewById(R.id.aquarius);
        aquarius.setOnClickListener(this);
        pisces = getActivity().findViewById(R.id.pisces);
        pisces.setOnClickListener(this);

        zodiac = getActivity().findViewById(R.id.tv_zodiac);
        date = getActivity().findViewById(R.id.tv_date);
        horoscope = getActivity().findViewById(R.id.tv_horoscope);

        btnNotification = getActivity().findViewById(R.id.btn_notification);
        btnNotification.setOnClickListener(this);
        layout = getActivity().findViewById(R.id.layout_astro_content);

        isNotify = sharedPreferences.getBoolean(SHARED_PREF_IS_NOTIFY, false);
        btnNotification.setImageResource(isNotify ? R.drawable.ic_baseline_notifications_on_24 : R.drawable.ic_baseline_notifications_off_24);
    }

    @Override
    public void onClick(View view) {
        layout.setVisibility(View.VISIBLE);
        switch (view.getId()) {
            case R.id.aries:
                getAstrology("aries");
                break;
            case R.id.taurus:
                getAstrology("taurus");
                break;
            case R.id.gemini:
                getAstrology("gemini");
                break;
            case R.id.cancer:
                getAstrology("cancer");
                break;
            case R.id.leo:
                getAstrology("leo");
                break;
            case R.id.virgo:
                getAstrology("virgo");
                break;
            case R.id.libra:
                getAstrology("libra");
                break;
            case R.id.scorpio:
                getAstrology("scorpio");
                break;
            case R.id.capricorn:
                getAstrology("capricorn");
                break;
            case R.id.sagittarius:
                getAstrology("sagittarius");
                break;
            case R.id.aquarius:
                getAstrology("aquarius");
                break;
            case R.id.pisces:
                getAstrology("pisces");
                break;
            case R.id.btn_notification:
                setSharedPrefValue(!isNotify);
                isNotify = sharedPreferences.getBoolean(SHARED_PREF_IS_NOTIFY, false);
                btnNotification.setImageResource(isNotify ? R.drawable.ic_baseline_notifications_on_24 : R.drawable.ic_baseline_notifications_off_24);
                scheduleNotification(getNotification());
                if (isNotify){
                    Toast.makeText(getContext(), "Subscribed astrology!", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getContext(), "Unsubscribed astrology!", Toast.LENGTH_SHORT).show();
                }

        }
    }

    public void setSharedPrefValue(boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREF_IS_NOTIFY, value);
        editor.commit();
    }

    public void getAstrology(String yourZodiac) {
        AndroidNetworking.get(API.API_GET_ZODIAC_TODAY).addPathParameter("your_zodiac", yourZodiac).build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String zodiac__ = response.getString("sunsign");
                            String date_ = response.getString("date");
                            String horoscope_ = response.getString("horoscope");
                            zodiac.setText(zodiac__);
                            date.setText(date_);
                            horoscope.setText(horoscope_);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }


    private Notification getNotification() {
        String zodiac_ = sharedPreferences.getString("zodiac", "");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "default");
        builder.setContentText("Come to see the astrology today!")
                .setContentTitle("Have a nice day!")
                .setSmallIcon(R.drawable.ic_baseline_notifications_off_24)
                .setChannelId("NtxQ")
                .setAutoCancel(true);
        return builder.build();
    }

    private void scheduleNotification(Notification notification) {
        Intent notificationIntent = new Intent(getContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.ID_EXTRA, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_EXTRA, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext()
                , 0
                , notificationIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        if(isNotify){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP
                    , calendar.getTimeInMillis()
                    , AlarmManager.INTERVAL_DAY
                    , pendingIntent);
        } else{
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }

    }


}
