package com.ntxq.btl_ptudpm.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.ntxq.btl_ptudpm.API;
import com.ntxq.btl_ptudpm.R;

public class NumberFragment extends Fragment implements View.OnClickListener {
    private EditText edtNumber;

    private Button btnGetNumber;

    private TextView tvNumber;
    private TextView tvFactNumber;

    private LinearLayout layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_numbers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        layout = getActivity().findViewById(R.id.layout_fact);
        edtNumber = getActivity().findViewById(R.id.edt_number);
        tvNumber = getActivity().findViewById(R.id.tv_number);
        tvFactNumber = getActivity().findViewById(R.id.tv_factNumber);

        btnGetNumber = getActivity().findViewById(R.id.btn_getNumber);
        btnGetNumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_getNumber:
                String input = edtNumber.getText().toString();
                if (!check(input)) {
                    showDialog();
                    return;
                }
                layout.setVisibility(View.VISIBLE);
                String[] arr = input.split("/");

                switch (arr.length) {
                    case 1:
                        tvNumber.setText("Number " + arr[0]);
                        getNumberFact(arr[0]);
                        break;

                    case 2:
                        tvNumber.setText(arr[0] + "/" + arr[1]);
                        getDateFact(arr[0], arr[1]);
                        break;
                    case 3:
                        tvNumber.setText(arr[0] + "/" + arr[1] + "/" + arr[2]);
                        getYearFact(arr[0], arr[1], arr[2]);
                        break;
                    default:
                        showDialog();
                }
                break;
        }
    }


    public void getNumberFact(String number) {
        AndroidNetworking.get(API.API_GET_NUMBER_FACT).addPathParameter("number", number).build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        tvFactNumber.setText(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void getDateFact(String day, String month) {
        AndroidNetworking.get(API.API_GET_DATE_FACT)
                .addPathParameter("day", day)
                .addPathParameter("month", month)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                tvFactNumber.setText(response);
            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    private void getYearFact(String day, String month, String year) {
        StringBuilder str = new StringBuilder();
        str.append("We have something about this date:\n");
        AndroidNetworking.get(API.API_GET_DATE_FACT)
                .addPathParameter("day", day)
                .addPathParameter("month", month)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                str.append(response);
                Log.e("TAG", "getYearFact 1: " + str.toString());
                str.append("\n");
                AndroidNetworking.get(API.API_GET_YEAR_FACT).addPathParameter("year", year).build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                str.append("And " + response);
                                str.append("\n\nThe number represents for this date is ");
                                str.append(getRepresentNumber(day, month, year) + ".");
                                tvFactNumber.setText(str);
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
            }

            @Override
            public void onError(ANError anError) {

            }
        });


    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("We need a date..")
                .setMessage("If you don't know how a date look like, at least please input 3 positive number separated by a" + " /.")
                .setNeutralButton("Understood!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean check(String str) {
        if (str.contains("-") || str.contains(".") || str.contains("_") || str.contains("//")) {
            return false;
        }
        return true;
    }

    public int getRepresentNumber(String day, String month, String year) {
        int result = 0;
        for (int i = 0; i < day.length(); i++) {
            result += (day.charAt(i) - '0');
        }
        for (int i = 0; i < month.length(); i++) {
            result += (month.charAt(i) - '0');
        }
        for (int i = 0; i < year.length(); i++) {
            result += (year.charAt(i) - '0');
        }
        while (result >= 10) {
            String str = String.valueOf(result);
            result = 0;
            for (int i = 0; i < str.length(); i++) {
                result += str.charAt(i) - '0';
            }
        }
        return result;
    }
}
