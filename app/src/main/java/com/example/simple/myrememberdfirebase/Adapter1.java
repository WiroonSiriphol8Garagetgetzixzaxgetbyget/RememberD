package com.example.simple.myrememberdfirebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Adapter1 extends RecyclerView.Adapter<Adapter1.ViewHolder> {
    private int lastPosition = -1;
    private JSONArray data;
    private final OnItemClickListener listener;
    private Context context;
    private String NoMonth;

     Adapter1(Context context, JSONArray data, OnItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(Context context, String NoMonth, String month, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_header, txt_count;
        CardView cardView;
        ImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            txt_header = itemView.findViewById(R.id.txt_header);
            txt_count = itemView.findViewById(R.id.txt_count);
            cardView = itemView.findViewById(R.id.card);
            img = itemView.findViewById(R.id.img);
        }

        @SuppressLint("SetTextI18n")
        void bind(final JSONObject data, final OnItemClickListener listener, int position) {
            try {
                NoMonth = data.getString("month");
                switch (NoMonth) {
                    case "01":
                        txt_header.setText("January");
                        img.setImageResource(R.drawable.ic_one);
                        break;
                    case "02":
                        txt_header.setText("February");
                        img.setImageResource(R.drawable.ic_two);
                        break;
                    case "03":
                        txt_header.setText("March");
                        img.setImageResource(R.drawable.ic_three);
                        break;
                    case "04":
                        txt_header.setText("April");
                        img.setImageResource(R.drawable.ic_four);
                        break;
                    case "05":
                        txt_header.setText("May");
                        img.setImageResource(R.drawable.ic_five);
                        break;
                    case "06":
                        txt_header.setText("June");
                        img.setImageResource(R.drawable.ic_six);
                        break;
                    case "07":
                        txt_header.setText("July");
                        img.setImageResource(R.drawable.ic_seven);
                        break;
                    case "08":
                        txt_header.setText("August");
                        img.setImageResource(R.drawable.ic_eight);
                        break;
                    case "09":
                        txt_header.setText("September");
                        img.setImageResource(R.drawable.ic_nine);
                        break;
                    case "10":
                        txt_header.setText("October");
                        img.setImageResource(R.drawable.ic_ten);
                        break;
                    case "11":
                        txt_header.setText("November");
                        img.setImageResource(R.drawable.ic_eleven);
                        break;
                    case "12":
                        txt_header.setText("December");
                        img.setImageResource(R.drawable.ic_twelve);
                        break;
                }
                    int count = data.getInt("count_notification");
                txt_count.setText(count + " appointment");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String month = txt_header.getText().toString().trim();
                        String NoMonth = data.getString("month");
                        listener.onItemClick(context, NoMonth, month, getAdapterPosition());
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview1, parent, false);
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
            holder.bind(((JSONObject) data.get(position)), listener, position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.length();
    }


}




