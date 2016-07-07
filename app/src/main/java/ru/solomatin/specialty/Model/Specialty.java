package ru.solomatin.specialty.Model;

/**
 * Специальность
 */
public class Specialty {
    private int specialty_id;
    private String name;

    public Specialty() {
    }

    public Specialty(int specialty_id, String name) {
        this.specialty_id = specialty_id;
        this.name = name;
    }

    public int getSpecialty_id() {
        return specialty_id;
    }

    public void setSpecialty_id(int specialty_id) {
        this.specialty_id = specialty_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        boolean isEqual= false;
        if (object != null && object instanceof Specialty) {
            isEqual = (this.specialty_id == ((Specialty) object).specialty_id)
                    &&(this.name.equals(((Specialty) object).name));
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return this.specialty_id;
    }
}


