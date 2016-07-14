package ru.solomatin.specialty;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.solomatin.specialty.Model.Person;

/**
 * Адаптер для списка работников
 */
public class PersonListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Person> personItems;

    public PersonListAdapter(Activity activity, List<Person> personItems) {
        this.activity = activity;
        this.personItems = personItems;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        @BindView(R.id.f_name) TextView f_name;
        @BindView(R.id.l_name) TextView l_name;
        @BindView(R.id.age) TextView age;
        @BindView(R.id.thumbnail) ImageView thumbnail;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getCount() {
        return personItems.size();
    }

    @Override
    public Object getItem(int location) {
        return personItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.list_person_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        Person p = personItems.get(position);
        Picasso.with(activity)
                .load(p.getAvatr_url())
                .placeholder(R.drawable.empty_image)
                .error(R.drawable.empty_image)
                .into(holder.thumbnail);
        holder.f_name.setText(p.getF_name());
        holder.l_name.setText(p.getL_name());
        if (p.getAge() != 0) {
            holder.age.setText(String.valueOf(p.getAge()));
        } else {
            holder.age.setText("-");
        }
        return convertView;
    }

}

