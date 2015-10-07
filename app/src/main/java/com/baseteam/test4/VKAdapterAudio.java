package com.baseteam.test4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by matvey on 07.10.15.
 */
public class VKAdapterAudio extends ArrayAdapter<VKaudio> {
    private final Context context;
    private final VKaudio[] values;

    public VKAdapterAudio(Context context, VKaudio[] values) {
        super(context, R.layout.list_audio_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_audio_item, parent, false);
        TextView fl = (TextView) rowView.findViewById(R.id.firstLine);
        TextView sl = (TextView) rowView.findViewById(R.id.secondLine);
        fl.setText(values[position].title);
        sl.setText(values[position].artist);
        return rowView;
    }

}
