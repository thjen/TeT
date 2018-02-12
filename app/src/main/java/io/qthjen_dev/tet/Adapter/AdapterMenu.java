package io.qthjen_dev.tet.Adapter;

import android.content.Context;
import android.content.Intent;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.qthjen_dev.tet.Activity.ListDataActivity;
import io.qthjen_dev.tet.R;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.ViewHolder> {

    private Context context;

    public int CardBackground[] = {

            R.color.appllenew,
            R.color.videos,
            R.color.notes,
            R.color.safari,
            R.color.calendar,
            R.color.message,
            R.color.ibooks,
            R.color.colorgroup,

    };

    public int MenuMain[] = {

            R.string.chamemenu,
            R.string.thaycomenu,
            R.string.nguoiyeumenu,
            R.string.vochongmenu,
            R.string.haimenu,
            R.string.haymenu,
            R.string.englishmenu,
            R.string.japanmenu

    };

    public AdapterMenu(Context context, RecyclerView mRecyclerMenu) {

        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.menu_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tv_name.setText(MenuMain[position]);
        holder.card_menu.setCardBackgroundColor(ContextCompat.getColor(context, CardBackground[position]));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ListDataActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return 8;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        CardView card_menu;

        View view;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_menu);
            card_menu = itemView.findViewById(R.id.card_menu);

            view = itemView;

        }

    }

}
