package ru.solomatin.specialty;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


import ru.solomatin.specialty.Model.Person;
import ru.solomatin.specialty.Model.Specialty;

/**
 * Фрагмент с информацией о работнике
 */
public class DetailFragment extends Fragment {

    public static final String TAG = "DetailFragment";
    private MainActivity listener;
    private Person person;
    @BindView(R.id.f_name) TextView f_name;
    @BindView(R.id.l_name) TextView l_name;
    @BindView(R.id.thumbnail) ImageView thumbnail;
    @BindView(R.id.age) TextView age;
    @BindView(R.id.birthday) TextView birthday;
    @BindView(R.id.specialty) TextView specialty;
    private Unbinder unbinder;

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
        unbinder = ButterKnife.bind(this, view);
        Picasso.with(listener)
                .load(person.getAvatr_url())
                .placeholder(R.drawable.empty_image)
                .error(R.drawable.empty_image)
                .into(thumbnail);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
