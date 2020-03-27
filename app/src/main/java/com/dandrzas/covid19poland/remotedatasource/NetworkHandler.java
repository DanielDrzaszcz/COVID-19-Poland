package com.dandrzas.covid19poland.remotedatasource;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dandrzas.covid19poland.model.Covid19Data;

import org.json.JSONException;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class NetworkHandler implements NetworkHandlerIF {
    private static NetworkHandler ourInstance = new NetworkHandler();
    private Observable<Covid19Data> covid19DataEmitter = Observable.create(new Covid19DataEmitter());
    private static RequestQueue queue;
    private final String URL = "https://corona.lmao.ninja/countries/Poland";

    private NetworkHandler() {
    }

    public static void createInstance(Context context) {
        ourInstance = new NetworkHandler();
        queue = Volley.newRequestQueue(context); // Instantiate the RequestQueue.
    }

    public static NetworkHandler getInstance() {
        return ourInstance;
    }

    @Override
    public Observable<Covid19Data> downloadData() {
        return  covid19DataEmitter;
    }

    private class Covid19DataEmitter implements ObservableOnSubscribe<Covid19Data> {

        @Override
        public void subscribe(ObservableEmitter<Covid19Data> emitter) {
            // Request a response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                    (response) -> {
                        VolleyLog.d(response.toString());
                        try {
                            Covid19Data responseData = new Covid19Data();
                            responseData.setCasesAll((int)response.get("cases"));
                            responseData.setCasesToday((int)response.get("todayCases"));
                            responseData.setCuredAll((int)response.get("recovered"));
                            responseData.setDeathsAll((int)response.get("deaths"));
                            responseData.setDeathsToday((int)response.get("todayDeaths"));

                            emitter.onNext(responseData);
                            emitter.onComplete();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> {
                        emitter.onError(new Throwable());
                    });
            queue.add(jsonObjectRequest);

        }
    }
}
