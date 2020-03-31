package com.dandrzas.covid19poland.model.remotedatasource;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dandrzas.covid19poland.model.domain.Covid19Data;
import org.json.JSONException;
import io.reactivex.Observable;

public class RemoteDataSource implements RemoteDataSourceIF {
    private static RemoteDataSource ourInstance = new RemoteDataSource();
    private static RequestQueue queue;
    private static final String URL = "https://corona.lmao.ninja/countries/Poland";

    private RemoteDataSource() {
    }

    public static void createInstance(Context context) {
        ourInstance = new RemoteDataSource();
        queue = Volley.newRequestQueue(context); // Instantiate the RequestQueue.
    }

    public static RemoteDataSource getInstance() {
        return ourInstance;
    }

    @Override
    public Observable<Covid19Data> downloadData() {

        return  Observable.create(emitter -> {

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

        });
    }
}
