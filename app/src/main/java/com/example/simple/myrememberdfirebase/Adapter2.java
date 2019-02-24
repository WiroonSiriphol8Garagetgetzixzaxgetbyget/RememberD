package com.example.simple.myrememberdfirebase;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder> {
    private int lastPosition = -1;
    private JSONArray data;
    private final OnItemClickListener listener;
    private Context context;

    Adapter2(Context context, JSONArray data, OnItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Context context, String id_rmmb, String dmy, String data_time, String data_place, String detail, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, place, detail;
        CardView cardView;
        ImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_detail);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            place = itemView.findViewById(R.id.place);
            detail = itemView.findViewById(R.id.detail);
            img = itemView.findViewById(R.id.img);
        }

        @SuppressLint("ResourceType")
        void bind(final JSONObject data, final OnItemClickListener listener, final int position) {
            try {
                String day = data.getString("day");
                String month = data.getString("month");
                String year = data.getString("year");
                String dmy = day + "/" + month + "/" + year;
                String data_time = data.getString("time");
                String data_place = data.getString("place");
                String data_detail = data.getString("activity");

                String mDrawableName = "ic_" + month + "_" + day;
                int resID = context.getResources().getIdentifier(mDrawableName, "drawable", context.getPackageName());
                img.setImageResource(resID);
                date.setText(dmy);
                time.setText(data_time);
                place.setText(data_place);
                detail.setText(data_detail);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String id_rmmb = data.getString("id_rmmb");
                        String day = data.getString("day");
                        String month = data.getString("month");
                        String year = data.getString("year");
                        String dmy = day + "/" + month + "/" + year;
                        String data_time = data.getString("time");
                        String data_place = data.getString("place");
                        String data_detail = data.getString("activity");
                        listener.onItemClick(context, id_rmmb, dmy, data_time, data_place, data_detail, position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.right_to_left);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

        try {
            holder.bind((JSONObject) (data.get(position)), listener, position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.length();
    }


    void removeItem(final int position) {
        //ลบข้อมูล
        String connstr_query_delete = context.getResources().getString(R.string.config_IP) + "/Android_Query/query_delete.php";
        StringRequest stringRequestDelete = new StringRequest(Request.Method.POST, connstr_query_delete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("my_delete", "delete Ok");
                        data.remove(position);
                        notifyItemRemoved(position);
                        if (getItemCount() == 0) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
//                            CustomIntent.customType(context.getApplicationContext(), "fadein-to-fadeout");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("my_delete", String.valueOf(error));
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> postMap3 = new HashMap<>();
                try {
                    JSONObject d = (JSONObject) data.get(position);
                    String id_rmmb = d.getString("id_rmmb");
                    Log.d("delete5555", id_rmmb);
                    postMap3.put("id_rmmb", id_rmmb);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return postMap3;
            }
        };
        Volley.newRequestQueue(context).add(stringRequestDelete);

    }

    public void restoreItem(JSONObject item, int position) throws JSONException {
        data.put(item);
        notifyItemInserted(position);
    }


}




