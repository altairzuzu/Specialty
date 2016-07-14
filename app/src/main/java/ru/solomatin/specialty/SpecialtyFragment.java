package ru.solomatin.specialty;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import ru.solomatin.specialty.Model.Person;
import ru.solomatin.specialty.Model.Specialty;

/**
 * Фрагмент со списком специальностей
 */
public class SpecialtyFragment extends ListFragment {

    public static final String TAG = "SpecialtyFragment";
    private SpecialtyListAdapter adapter;
    private MainActivity listener;
    private List<Person> personList;
    private List<Specialty> specialtyList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // При удалении MainActivity - отцепляем фрагмент и оставляем в памяти
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new SpecialtyListAdapter(getActivity(),
                listener.specialtyList);
        setListAdapter(adapter);
        listener.setTitle(getResources().getString(R.string.specialty_title));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        listener.changeFragment(position);
    }

    public void notifyRefresh() {
        adapter.notifyDataSetChanged();
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public List<Specialty> getSpecialtyList() {
        return specialtyList;
    }

    public void setSpecialtyList(List<Specialty> specialtyList) {
        this.specialtyList = specialtyList;
    }
}
