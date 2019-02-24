package com.example.simple.myrememberdfirebase;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.simple.myrememberdfirebase.Model.MyResponse;
import com.example.simple.myrememberdfirebase.Model.SetNotification;
import com.example.simple.myrememberdfirebase.Model.Sender;
import com.example.simple.myrememberdfirebase.Remote.APIService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {
    private EditText data_date;
    private EditText data_time;
    private EditText data_place;
    private EditText data_activity;

    FirebaseFirestore db;
    Map<String, Object> user;
    TimePickerDialog timePickerDialog;
    Calendar calendar = Calendar.getInstance();
    APIService mService;

    String id_rmmb;
    boolean action;
    String[] sdate;
    public String month,NoMonth,y;


    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String connstr_query_update = getResources().getString(R.string.config_IP) + "/Android_Query/query_update.php";

        Common.currentToken = FirebaseInstanceId.getInstance().getToken();
        mService = Common.getFCMClient();

        Log.d("MyToken", Common.currentToken);

        db = FirebaseFirestore.getInstance();
        user = new HashMap<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        data_date = view.findViewById(R.id.data_date);
        data_time = view.findViewById(R.id.data_time);
        data_place = view.findViewById(R.id.data_place);
        data_activity = view.findViewById(R.id.data_activity);
        Button btn_submit = view.findViewById(R.id.btn_submit);

        Bundle bundle = getArguments();
        if (bundle != null) {
            action = bundle.getBoolean("action");
            if (action) {
                id_rmmb = bundle.getString("id_rmmb");
                String date = bundle.getString("dmy");
                String time = bundle.getString("data_time");
                String place = bundle.getString("data_place");
                String detail = bundle.getString("detail");
                month = bundle.getString("Month");
                NoMonth = bundle.getString("NoMonth");
                y = bundle.getString("Year");

                data_date.setText(date);
                data_time.setText(time);
                data_place.setText(place);
                data_activity.setText(detail);
                btn_submit.setText("edit");
                btn_submit.setBackground(getResources().getDrawable(R.drawable.roundedbutton_edit));
            }
        }//edit

        data_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = new DatePicker();
                if (getFragmentManager() != null) {
                    datePicker.show(getFragmentManager(), "DatePicker");
                }
            }
        });

        data_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_TimePicker();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateDate() && ValidateTime() && ValidatePlace() && ValidateActivity()) {
                    if (!action) {
                        String cdate = data_date.getText().toString();
                        String ctime = data_time.getText().toString();
                        String cplace = data_place.getText().toString();
                        String cactivity = data_activity.getText().toString();

                        String[] data_cdate = cdate.split("/");
                        String[] data_ctime = ctime.split(":");
                        String set_month = "";
                        String time_hr = "";
                        String time_minu = "";

                        String day = data_cdate[0];
                        int month = Integer.parseInt(data_cdate[1]);
                        if (month <= 9) {
                            set_month = "0" + month;
                        } else {
                            set_month += month;
                        }
                        String year = data_cdate[2];

                        int hr_time = Integer.parseInt(data_ctime[0]);
                        int minu_time = Integer.parseInt(data_ctime[1]);

                        if (hr_time <= 9) {
                            time_hr = "0" + hr_time;
                        } else {
                            time_hr += hr_time;
                        }

                        if (minu_time <= 9) {
                            time_minu = "0" + minu_time;
                        } else {
                            time_minu += minu_time;
                        }

                        String set_time = time_hr + ":" + time_minu;

                        MySQLConnect bg = new MySQLConnect(getActivity());
                        bg.execute(cactivity, cplace, Common.currentToken, year, set_month, day, set_time,"add");
                    } else {
                        StringRequest stringRequestUpdate = new StringRequest(Request.Method.POST, connstr_query_update, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getContext(), "Edit data successfully :)", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), MonthActivity.class);
                                intent.putExtra("Month",month);
                                intent.putExtra("NoMonth",NoMonth);
                                intent.putExtra("Year",y);
                                startActivity(intent);
                                CustomIntent.customType(getContext(), "left-to-right");
                                getActivity().finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                String date = data_date.getText().toString().trim();
                                sdate = date.split("/");

                                Map<String, String> postMap = new HashMap<>();
                                postMap.put("id_rmmb", id_rmmb);
                                postMap.put("activity", data_activity.getText().toString().trim());
                                postMap.put("place", data_place.getText().toString().trim());
                                postMap.put("token", Common.currentToken);
                                postMap.put("year", sdate[2]);
                                postMap.put("month", sdate[1]);
                                postMap.put("day", sdate[0]);
                                postMap.put("time", data_time.getText().toString().trim());

                                return postMap;
                            }
                        };

                        Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext()).add(stringRequestUpdate);
                    }
                }
            }
        });

        return view;
    }

    private void set_TimePicker() {
        int Hour = calendar.get(Calendar.HOUR_OF_DAY);
        int Minute = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                data_time.setText(hour + ":" + minute);
            }
        }, Hour, Minute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }//set_TimePicker


    private boolean ValidateDate() {
        String date = data_date.getText().toString().trim();
        if (date.isEmpty()) {
            data_date.requestFocus();
            data_date.setError("");
            return false;
        } else {
            data_date.setError(null);
            return true;
        }
    }//ValidateDate

    private boolean ValidateTime() {
        String time = data_time.getText().toString().trim();
        if (time.isEmpty()) {
            data_time.requestFocus();
            data_time.setError("");
            return false;
        } else {
            data_time.setError(null);
            return true;
        }
    }//ValidateTime

    private boolean ValidatePlace() {
        String place = data_place.getText().toString().trim();
        if (place.isEmpty()) {
            data_place.requestFocus();
            data_place.setError("");
            return false;
        } else {
            data_place.setError(null);
            return true;
        }
    }//ValidatePlace

    private boolean ValidateActivity() {
        String act = data_activity.getText().toString().trim();
        if (act.isEmpty()) {
            data_activity.requestFocus();
            data_activity.setError("");
            return false;
        } else {
            data_activity.setError(null);
            return true;
        }
    }//ValidatePlace
}
