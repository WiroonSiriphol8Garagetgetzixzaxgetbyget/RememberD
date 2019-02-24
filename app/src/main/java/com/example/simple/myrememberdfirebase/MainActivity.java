package com.example.simple.myrememberdfirebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    Map<String, Object> user;

    TextView year1;
    TextView year2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Common.currentToken = FirebaseInstanceId.getInstance().getToken();
        if (!Common.flag_year){
            Common.year = "2019";
        }


        db = FirebaseFirestore.getInstance();
        user = new HashMap<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotification", "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }

        year1 = findViewById(R.id.year1);
        year2 = findViewById(R.id.year2);

        Button btn_set_config = findViewById(R.id.btn_set_config);
        btn_set_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                View view = inflater.inflate(R.layout.dialog_setting, null);
                builder.setView(view);

                final EditText yyy = view.findViewById(R.id.data_year);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String y = yyy.getText().toString().trim();
                        Common.year = y;
                        Common.flag_year = true;
                        if (ValidateYear(yyy)) {
                            yyy.setText(y);
                            load_data();//get data from api
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter the year again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });//setting

        FloatingActionButton button = findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.right_to_left, R.anim.right_to_left);
                transaction.replace(R.id.main, new AddFragment());
                transaction.commit();
            }
        });

        load_data();//get data from api
    }//onCreate

    private boolean ValidateYear(EditText editText) {
        return editText.getText().toString().trim().length() == 4;
    }//ValidateDate

    public void load_data() {
        String sub_y1 = Common.year.substring(0, 2);
        String sub_y2 = Common.year.substring(2, 4);
        year1.setText(sub_y1);
        year2.setText(sub_y2);

        String connstr_query_count = getResources().getString(R.string.config_IP) + "/Android_Query/get_query_count.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, connstr_query_count,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray data = new JSONArray(response);
                            Adapter1 adapter = new Adapter1(getApplicationContext(), data, new Adapter1.OnItemClickListener() {
                                @Override
                                public void onItemClick(Context context, String NoMonth, String Month, int position) {
                                    Intent intent = new Intent(context, MonthActivity.class);
                                    intent.putExtra("NoMonth", NoMonth);
                                    intent.putExtra("Month", Month);
                                    intent.putExtra("Year", Common.year);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    finish();
                                }
                            });
                            RecyclerView recyclerView = findViewById(R.id.rcv1);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "TimeoutError", Toast.LENGTH_SHORT).show();
                        Log.d("VolleyError", String.valueOf(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("year", Common.year);
                postMap.put("token", Common.currentToken);
                return postMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getVisibleFragment();
        if (fragment instanceof AddFragment) {
            ClearFragment();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");
            finish();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Do you want to exit the app?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
//                    MainActivity.super.onBackPressed();
                    finish();
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }

    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            Fragment fragment = fragments.get(fragments.size() - 1);
            if (fragment != null && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }

    private void ClearFragment() {
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
    }
}
