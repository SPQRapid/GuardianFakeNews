package com.example.android.guardianfakenews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tudor on 07.07.2017.
 */

public class GuardianAdapter extends ArrayAdapter<Guardian> {

    public GuardianAdapter(Context context, List<Guardian> guardianList) {
        super(context, 0, guardianList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Guardian guardian = getItem(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.title);
        titleTextView.setText(guardian.getTitle());

        TextView idTextView = (TextView) convertView.findViewById(R.id.section_id);
        idTextView.setText(guardian.getId());

        TextView sectionTextView = (TextView) convertView.findViewById(R.id.section);
        sectionTextView.setText(guardian.getSection());

        return convertView;
    }
}
