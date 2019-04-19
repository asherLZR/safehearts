package edu.monash.smile.data;

import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;

public interface FhirCallback<T> {
    void onFailure(Exception e);

    void onResponse(T response) throws IOException, JSONException;
}
