package ru.solomatin.specialty;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import ru.solomatin.specialty.Model.Person;
import ru.solomatin.specialty.Model.Specialty;

/**
 * Фрагмент с информацией о работнике
 */
public class DetailFragment extends Fragment {

    public static final String TAG = "DetailFragment";
    private MainActivity listener;
    private Person person;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) view
                .findViewById(R.id.thumbnail);
        // Пока не удалось загрузить фотографию работника - черный квадрат
        thumbNail.setDefaultImageResId(R.drawable.empty_image);
        thumbNail.setErrorImageResId(R.drawable.empty_image);
        TextView f_name = (TextView) view.findViewById(R.id.f_name);
        TextView l_name = (TextView) view.findViewById(R.id.l_name);
        TextView birthday = (TextView) view.findViewById(R.id.birthday);
        TextView age = (TextView) view.findViewById(R.id.age);
        TextView specialty = (TextView) view.findViewById(R.id.specialty);
        thumbNail.setImageUrl(person.getAvatr_url(), imageLoader);
        f_name.setText(person.getF_name());
        l_name.setText(person.getL_name());
        if (person.getAge() != 0) {
            age.setText(String.valueOf(person.getAge()));
        } else {
            age.setText("-");
        }
        birthday.setText(person.getBirthday());
        ArrayList<Specialty> sp = person.getSpecialty();
        String specialtyString = "";
        for (Specialty s: sp) {
            specialtyString = specialtyString + s.getName() + "\n";
        }
        specialty.setText(specialtyString);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listener.setTitle(String.valueOf(person.getL_name()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
