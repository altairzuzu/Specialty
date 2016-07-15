package ru.solomatin.specialty;

import ru.solomatin.specialty.Model.ApiResponse;
import ru.solomatin.specialty.Model.Specialty;
import ru.solomatin.specialty.Model.Person;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SpecialtyFragment specialtyFragment;
    public List<Person> personList = new ArrayList<>();
    public List<Specialty> specialtyList = new ArrayList<>();
    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new Presenter(this);

        // Отслеживаем изменения стэка возврата
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();

        FragmentManager fm = getSupportFragmentManager();
        specialtyFragment =
                (SpecialtyFragment) fm.findFragmentByTag(SpecialtyFragment.TAG);
        if (specialtyFragment == null) {
            // Создаем фрагмент Специальностей
            specialtyFragment = new SpecialtyFragment();
            android.support.v4.app.FragmentTransaction fr = fm.beginTransaction();
            fr.replace(R.id.content_frame, specialtyFragment, SpecialtyFragment.TAG);
            fr.commit();
        } else {
            personList = specialtyFragment.getPersonList();
            specialtyList = specialtyFragment.getSpecialtyList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadRxData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.rxUnSubscribe();
        // Сохраняем во фрагменте списки Спец-тей и Работников
        specialtyFragment.setSpecialtyList(specialtyList);
        specialtyFragment.setPersonList(personList);
    }

    public void showRxResults(ApiResponse response) {
        personList = response.getResponse();
        specialtyList.clear();
        for (Person person : personList) {
            for (Specialty specialty : person.getSpecialty()) {
                if (!specialtyList.contains(specialty)) {
                    specialtyList.add(specialty);
                }
            }
        }
        specialtyFragment.notifyRefresh();
        hidePDialog();
    }

    protected void showRxInProcess(){
        pDialog = new ProgressDialog(this);
        // Показываем прогресс-диалог
        pDialog.setMessage(getResources().getString(R.string.loading));
        pDialog.show();
    }

    protected void showRxFailure(Throwable throwable){
        hidePDialog();
        Toast.makeText(this,
                getResources().getString(R.string.no_connection_message),
                Toast.LENGTH_LONG)
                .show();
    }

    public void changeFragment(int position) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.content_frame);
        android.support.v4.app.FragmentTransaction fr = fm.beginTransaction();
        if (frag instanceof SpecialtyFragment) {
            // Открываем фрагмент с работниками и передаем ему
            // выбранную специальность
            PersonFragment fragment = new PersonFragment();
            fragment.setSpecialty(specialtyList.get(position));
            fr.replace(R.id.content_frame,fragment,PersonFragment.TAG);
        } else {
            // Открываем фрагмент с информацией о работнике и передаем ему
            // объект Person этого работника
            DetailFragment fragment = new DetailFragment();
            fragment.setPerson(((PersonFragment) frag).getPersonList().get(position));
            fr.replace(R.id.content_frame,fragment,DetailFragment.TAG);
        }
        fr.addToBackStack(null);
        fr.commit();
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    @SuppressWarnings("")
    public void shouldDisplayHomeUp(){
        //Включаем кнопку Вверх только, если есть записи в стэке возврата
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //Если нажата кнопка Вверх, выводим последний фрагмент из стэка возврата
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}