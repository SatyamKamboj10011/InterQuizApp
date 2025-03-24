package com.satyam.FinalProjectBackend.models;

import java.util.List;

public class OpenTDBResponse {

    private List<OpenTDBQuestion> results;

    public List<OpenTDBQuestion> getResults() {
        return results;
    }

    public void setResults(List<OpenTDBQuestion> results) {
        this.results = results;
    }
}
