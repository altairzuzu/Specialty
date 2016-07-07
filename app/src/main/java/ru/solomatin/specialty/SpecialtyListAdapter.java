package ru.solomatin.specialty;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.solomatin.specialty.Model.Specialty;

/**
 * Адаптер для списка специальностей
 */
public class SpecialtyListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Specialty> specialtyItems;

    public SpecialtyListAdapter(Activity activity, List<Specialty> specialtyItems) {
        this.activity = activity;
        this.specialtyItems = specialtyItems;
    }

    @Override
    public int getCount() {
        return specialtyItems.size();
    }

    @Override
    public Object getItem(int location) {
        return specialtyItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_specialty_row, null);

        //TextView id = (TextView) convertView.findViewById(R.id.specialty_id);
        TextView name = (TextView) convertView.findViewById(R.id.name);

        Specialty m = specialtyItems.get(position);
        String nameStr = String.valueOf(m.getSpecialty_id())+" "+m.getName();
        name.setText(nameStr);

        return convertView;
    }

}
