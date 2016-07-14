package ru.solomatin.specialty.Model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Десериализатор для Person-объектов
 */
public class PersonDeserializer implements JsonDeserializer<Person> {
    @Override
    public Person deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        final JsonObject jsonObject = json.getAsJsonObject();

        // Имя
        String fn = jsonObject.get("f_name").getAsString();
        String f_name="";
        if (fn.length()>0) {
            f_name = fn.substring(0, 1).toUpperCase();
        }
        if (fn.length()>1) {
            f_name = f_name + fn.toLowerCase().substring(1);
        }

        // Фамилия
        String ln = jsonObject.get("l_name").getAsString();
        String l_name="";
        if (ln.length()>0) {
            l_name = ln.substring(0, 1).toUpperCase();
        }
        if (ln.length()>1) {
            l_name = l_name + ln.toLowerCase().substring(1);
        }

        int age = 0;
        final JsonElement birthdayObj = jsonObject.get("birthday");
        String birthday;
        if (birthdayObj.isJsonNull()) {
            birthday = "-";
        } else {
            String birthdayStr = jsonObject.get("birthday").getAsString();
            if (birthdayStr.equals("")) {
                birthday = "-";
            } else {
                boolean formattable = false;
                SimpleDateFormat formatter =
                        new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                try {
                    if (birthdayStr.substring(2,3).equals("-")) {
                        formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        formattable = true;
                    } else if (birthdayStr.substring(4,5).equals("-")) {
                        formattable = true;
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }

                Date date = null;
                if (formattable) {
                    try {
                        date = formatter.parse(birthdayStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (date != null) {
                    age = countAge(date);
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy",Locale.ENGLISH);
                    birthday = df.format(date);
                } else {
                    birthday = "-";
                }
            }
        }

        // Фотография
        final JsonElement avatr_urlObj = jsonObject.get("avatr_url");
        String avatr_url;
        if (avatr_urlObj.isJsonNull()) {
            avatr_url = "empty";
        } else {
            avatr_url = jsonObject.get("avatr_url").getAsString();
            if (avatr_url.equals("")) {
                avatr_url = "empty";
            }
        }

        // Специальности работника десериализуем встроенным обработчиком
        final Specialty[] sp = context.deserialize(jsonObject.get("specialty"), Specialty[].class);
        final ArrayList<Specialty> specialty = new ArrayList<>(Arrays.asList(sp));

        final Person person = new Person();
        person.setF_name(f_name);
        person.setL_name(l_name);
        person.setBirthday(birthday);
        person.setAge(age);
        person.setAvatr_url(avatr_url);
        person.setSpecialty(specialty);
        return person;
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
}
