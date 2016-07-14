package ru.solomatin.specialty.Model;

import java.util.List;

/**
 * Ответ от API
 */
public class ApiResponse {
    private List<Person> response;

    public List<Person> getResponse() {
        return response;
    }

    public void setResponse(List<Person> response) {
        this.response = response;
    }
}
