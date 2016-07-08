package ru.solomatin.specialty;

import ru.solomatin.specialty.Model.Specialty;
import ru.solomatin.specialty.Model.Person;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String url = "http://65apps.com/images/testTask.json";
    private ProgressDialog pDialog;
    private SpecialtyFragment specialtyFragment;
    public List<Person> personList;
    public List<Specialty> specialtyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Отслеживаем изменения стэка возврата
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();

        FragmentManager fm = getSupportFragmentManager();
        specialtyFragment =
                (SpecialtyFragment) fm.findFragmentByTag(SpecialtyFragment.TAG);
        if (specialtyFragment == null) {
            // Если фрагмент Спецальностей не создан,
            // инициализируем списки Спец-тей и Работников
            specialtyList = new ArrayList<>();
            personList = new ArrayList<>();

            // Создаем фрагмент Специальностей
            specialtyFragment = new SpecialtyFragment();
            android.support.v4.app.FragmentTransaction fr = fm.beginTransaction();
            fr.replace(R.id.content_frame,specialtyFragment,SpecialtyFragment.TAG);
            fr.commit();

            pDialog = new ProgressDialog(this);
            // Показываем прогресс-диалог
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.show();

            // Создаем Volley-запрос
            JsonObjectRequest personReq = new JsonObjectRequest(Request.Method.GET,url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Log.d(TAG, response.toString());
                            hidePDialog();
                            try {
                                parseResponce(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // Обновляем список Специальностей
                            specialtyFragment.notifyRefresh();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hidePDialog();
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.no_connection_message),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
            // Добавляем запрос к очереди запросов Volley
            AppController.getInstance().addToRequestQueue(personReq);
        } else {
            // Восстанавливаем загруженные данные из фрагмента
            specialtyList = specialtyFragment.getSpecialtyList();
            personList = specialtyFragment.getPersonList();
        }
    }

    /**
     * Парсинг полученного JSONObject
     * @param response Json объект полученный Volley
     * @throws JSONException
     */
    private void parseResponce(JSONObject response) throws JSONException {
        JSONArray responseArray = response.getJSONArray("response");

        for (int i = 0; i < responseArray.length(); i++) {
            try {
                JSONObject obj = responseArray.getJSONObject(i);
                Person person = new Person();

                // Имя
                if(!obj.isNull("f_name")) {
                    String f_name = obj.getString("f_name");
                    String st="";
                    if (f_name.length()>0) {
                        st = f_name.substring(0, 1).toUpperCase();
                    }
                    if (f_name.length()>1) {
                        st = st + f_name.toLowerCase().substring(1);
                    }
                    person.setF_name(st);
                } else {
                    person.setF_name("");
                }

                // Фамилия
                if(!obj.isNull("l_name")) {
                    String l_name = obj.getString("l_name");
                    String st="";
                    if (l_name.length()>0) {
                        st = l_name.substring(0, 1).toUpperCase();
                    }
                    if (l_name.length()>1) {
                        st = st + l_name.toLowerCase().substring(1);
                    }
                    person.setL_name(st);
                } else {
                    person.setL_name("");
                }

                // URL фотографии работника
                if(!obj.isNull("avatr_url")) {
                    person.setAvatr_url(obj.getString("avatr_url"));
                } else {
                    person.setAvatr_url("");
                }

                // День рождения
                if(!obj.isNull("birthday")) {
                    String birthdayString = obj.getString("birthday");
                    boolean formattable = false;
                    SimpleDateFormat formatter =
                            new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    try {
                        if (birthdayString.substring(2,3).equals("-")) {
                            formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                            formattable = true;
                        } else if (birthdayString.substring(4,5).equals("-")) {
                            formattable = true;
                        }
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }

                    Date date = null;
                    if (formattable) {
                        try {
                            date = formatter.parse(birthdayString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (date != null) {
                        person.setAge(countAge(date));
                        DateFormat df = new SimpleDateFormat("dd.MM.yyyy",Locale.ENGLISH);
                        person.setBirthday(df.format(date));
                    } else {
                        person.setBirthday("-");
                    }

                } else {
                    person.setBirthday("-");
                }

                // Специальность
                JSONArray specialtyArray = obj.getJSONArray("specialty");
                ArrayList<Specialty> specialties = new ArrayList<>();
                for (int j = 0; j < specialtyArray.length(); j++) {
                    JSONObject specialty = specialtyArray.getJSONObject(j);
                    Specialty sp = new Specialty();
                    sp.setSpecialty_id(specialty.getInt("specialty_id"));
                    sp.setName(specialty.getString("name"));
                    // Пополняем персональный список специальностей
                    specialties.add(sp);
                    // Пополняем общий список специальностей
                    if (!specialtyList.contains(sp)) {
                        specialtyList.add(sp);
                    }
                }
                person.setSpecialty(specialties);

                // Добавляем работника к общему списку работников
                personList.add(person);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Рассчитывает возраст
     * @param birthday Дата рождения
     * @return Возраст(лет)
     */
    public int countAge(Date birthday) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.setTime(birthday);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        return age;
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
        // Сохраняем во фрагменте списки Спец-тей и Работников
        specialtyFragment.setSpecialtyList(specialtyList);
        specialtyFragment.setPersonList(personList);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}