package ru.solomatin.specialty;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import ru.solomatin.specialty.Model.Person;

/**
 * Адаптер для списка работников
 */
public class PersonListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Person> personItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public PersonListAdapter(Activity activity, List<Person> personItems) {
        this.activity = activity;
        this.personItems = personItems;
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

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_person_row, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        // Пока не удалось загрузить фотографию работника - черный квадрат
        thumbNail.setDefaultImageResId(R.drawable.empty_image);
        thumbNail.setErrorImageResId(R.drawable.empty_image);
        TextView f_name = (TextView) convertView.findViewById(R.id.f_name);
        TextView l_name = (TextView) convertView.findViewById(R.id.l_name);
        TextView age = (TextView) convertView.findViewById(R.id.age);

        Person p = personItems.get(position);
        thumbNail.setImageUrl(p.getAvatr_url(), imageLoader);
        f_name.setText(p.getF_name());
        l_name.setText(p.getL_name());
        if (p.getAge() != 0) {
            age.setText(String.valueOf(p.getAge()));
        } else {
            age.setText("-");
        }

        return convertView;
    }

}

