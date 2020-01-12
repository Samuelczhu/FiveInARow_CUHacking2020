package com.example.fiveinarow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StonesAdapter extends BaseAdapter {

    private final Context context;
    private final Stone[] stones;

    public StonesAdapter(Context context, Stone[] stones){
        this.context = context;
        this.stones = stones;
    }

    @Override
    public int getCount() {
        return stones.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Stone currentStone = stones[position]; //get the current stone

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.grid_item, null);
        }

        ImageView cellImg = (ImageView) convertView.findViewById(R.id.cell_image);
        cellImg.setImageResource(currentStone.getImageID());

        return convertView;
    }
}
