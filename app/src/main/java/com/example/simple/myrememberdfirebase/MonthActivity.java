package com.example.simple.myrememberdfirebase;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;


public class MonthActivity extends AppCompatActivity {
    private String NoMonth, y, month;
    RecyclerView recyclerView;
    Adapter2 adapter2;
    JSONArray data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        String connstr_query = getResources().getString(R.string.config_IP) + "/Android_Query/get_query.php";

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            month = bundle.getString("Month");
            NoMonth = bundle.getString("NoMonth");
            y = bundle.getString("Year");

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(month);
            setSupportActionBar(toolbar);

            AppBarLayout bg_header = findViewById(R.id.bg_header);
            switch (NoMonth) {
                case "01":
                    bg_header.setBackgroundColor(getColor(R.color.colorMonth01));
                    break;
                case "02":
                    bg_header.setBackgroundColor(getColor(R.color.colorMonth02));
                    break;
                case "03":
                    bg_header.setBackgroundColor(getColor(R.color.colorMonth03));
                    break;
                case "04":
                    bg_header.setBackgroundColor(getColor(R.color.colorMonth04));
                    break;
                case "05":
                    bg_header.setBackgroundColor(getColor(R.color.colorMonth05));
                    break;
                case "06":
                    bg_header.setBackgroundColor(getColor(R.color.colorMonth06));
                    break;
                case "07":
                    bg_header.setBackgroundColor(getColor(R.color.colorMonth07));
                    break;
                case "08":
                    bg_header.setBackgroundColor(getColor(R.color.colorMonth08));
                    break;
                case "09":
                    bg_header.setBackgroundColor(getColor(R.color.colorMonth09));
                    break;
                case "10":
                    bg_header.setBackgroundColor(getColor(R.color.colorMonth10));
                    break;
                case "11":
                    bg_header.setBackgroundColor(getColor(R.color.colorMonth11));
                    break;
                case "12":
                    bg_header.setBackgroundColor(getColor(R.color.colorMonth12));
                    break;
            }
        }

        //ข้อมูลของการแจ้งเตือนในแต่ละปี แต่ละเดือน
        StringRequest stringRequest = new StringRequest(Request.Method.POST, connstr_query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            data = new JSONArray(response);
                            adapter2 = new Adapter2(getApplicationContext(), data, new Adapter2.OnItemClickListener() {
                                @Override
                                public void onItemClick(Context context, String id_rmmb, String dmy, String data_time, String data_place, String detail, int position) {
                                    AddFragment fragment = new AddFragment();
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putBoolean("action", true);
                                    bundle1.putString("id_rmmb", id_rmmb);
                                    bundle1.putString("dmy", dmy);
                                    bundle1.putString("data_time", data_time);
                                    bundle1.putString("data_place", data_place);
                                    bundle1.putString("detail", detail);
                                    bundle1.putString("Month", month);
                                    bundle1.putString("NoMonth", NoMonth);
                                    bundle1.putString("Year", y);
                                    fragment.setArguments(bundle1);
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.setCustomAnimations(R.anim.right_to_left, R.anim.right_to_left);
                                    transaction.replace(R.id.main2, fragment);
                                    transaction.commit();
                                }
                            });
                            recyclerView = findViewById(R.id.rcv2);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setAdapter(adapter2);
                            enableSwipeToDelete();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("minimal", String.valueOf(error));
                        // Handle error
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("year", y);
                postMap.put("month", NoMonth);
                postMap.put("token", Common.currentToken);
                return postMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }//oncreate

    private void enableSwipeToDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                try {
                    final int position = viewHolder.getAdapterPosition();
                    final JSONObject item = (JSONObject) data.get(position);
                    final String cactivity = item.getString("activity");
                    final String cplace = item.getString("place");
                    final String year = item.getString("year");
                    final String set_month = item.getString("month");
                    final String day = item.getString("day");
                    final String set_time = item.getString("time");
                    adapter2.removeItem(position);
                    final CoordinatorLayout coordinatorLayout = findViewById(R.id.main2);
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Deleted Successfully", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        MySQLConnect bg = new MySQLConnect(getApplicationContext());
                                        bg.execute(cactivity, cplace, Common.currentToken, year, set_month, day, set_time, "undo");
                                        adapter2.restoreItem(item, position+1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Recover data successfully", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();
                                }
                            });
                    snackbar.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }//delete


    @Override
    public void onBackPressed() {
        Fragment fragment = getVisibleFragment();
        if (fragment instanceof AddFragment) {
            ClearFragment();
            Intent intent = new Intent(this, MonthActivity.class);
            intent.putExtra("NoMonth", NoMonth);
            intent.putExtra("Month", month);
            intent.putExtra("Year", y);
            startActivity(intent);
            CustomIntent.customType(this, "left-to-right");
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
//            super.onBackPressed();
        }
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MonthActivity.this.getSupportFragmentManager();
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
