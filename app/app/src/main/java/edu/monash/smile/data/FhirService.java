package edu.monash.smile.data;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.monash.smile.data.model.PatientReference;

public class FhirService {
    private static final String TAG = "FhirService";
    final private OkHttpClient client;
    private static String BASE_URL = "http://hapi-fhir.erc.monash.edu:8080/baseDstu3/";

    public FhirService(OkHttpClient client) {
        this.client = client;
    }

    private void loadJson(HttpUrl.Builder urlBuilder, Callback callback) {
        HttpUrl url = urlBuilder.build();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

    public void loadPatientIds(Integer practitionerId, final FhirCallback<Set<PatientReference>> callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "Encounter").newBuilder();
        urlBuilder.addQueryParameter("participant", "Practitioner/" + practitionerId);

        loadJson(urlBuilder, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Set<PatientReference> references = new HashSet<>();

                try {
                    JSONObject json = new JSONObject(response.body().string());
                    JSONArray entries = json.getJSONArray("entry");

                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject entry = (JSONObject) entries.get(i);

                        String reference = entry
                                .getJSONObject("resource")
                                .getJSONObject("subject")
                                .getString("reference");

                        references.add(new PatientReference(reference));
                    }

                    callback.onResponse(references);
                } catch (JSONException e) {
                    callback.onFailure(e);
                }

            }
        });
    }
}
