package ru.solomatin.specialty;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.solomatin.specialty.Model.Person;
import ru.solomatin.specialty.Model.Specialty;

/**
 * Фрагмент со списком работников
 */
public class PersonFragment extends ListFragment {

    public static final String TAG = "PersonFragment";
    private PersonListAdapter adapter;
    private MainActivity listener;
    private List<Person> personList;
    private Specialty specialty;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        personList = new ArrayList<>();
        // Отбираем из общего списка Работников только тех,
        // у кого нужная специальность
        for (Person p : listener.personList) {
            if (p.getSpecialty().contains(specialty)) {
                personList.add(p);
            }
        }
        PersonListAdapter adapter = new PersonListAdapter(getActivity(),
                personList);
        setListAdapter(adapter);
        listener.setTitle(String.valueOf(specialty.getName()));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        listener.changeFragment(position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    public void setSpecialty (Specialty specialty) {
        this.specialty = specialty;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }
}
