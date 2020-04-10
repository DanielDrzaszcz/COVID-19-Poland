package com.dandrzas.covid19poland.data.remotedatasource;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dandrzas.covid19poland.data.domain.Covid19Data;
import org.json.JSONException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;

public class RemoteDataSource implements RemoteDataSourceIF {
    private static RemoteDataSource ourInstance = new RemoteDataSource();
    private static RequestQueue queue;
    private static final String URL = "https://corona.lmao.ninja/countries/Poland";
    private static Covid19Data data;

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

        if (data == null) data = new Covid19Data();

        return  Observable.create(emitter -> {

                // Request a response from the provided URL.
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                        (response) -> {
                            VolleyLog.d(response.toString());
                            try {
                                //Covid19Data responseData = new Covid19Data();
                                data.setCasesAll((int)response.get("cases"));
                                data.setCasesToday((int)response.get("todayCases"));
                                data.setCuredAll((int)response.get("recovered"));
                                data.setDeathsAll((int)response.get("deaths"));
                                data.setDeathsToday((int)response.get("todayDeaths"));
                                HashMap<String, Integer> casesHistoryAll = new LinkedHashMap<>();
                                //casesHistoryAll.put("3/25/20", 1051);
                                //casesHistoryAll.put("3/26/20", 1221);
                                //casesHistoryAll.put("3/27/20", 1389);
                                //casesHistoryAll.put("3/28/20", 1638);
                                //casesHistoryAll.put("3/29/20", 1862);
                                //casesHistoryAll.put("3/30/20", 2055);
                                //casesHistoryAll.put("3/31/20", 2311);
                                //casesHistoryAll.put("4/01/20", 2554);
                                //casesHistoryAll.put("4/02/20", 2946);
                                casesHistoryAll.put("4/03/20", 3383);
                                casesHistoryAll.put("4/04/20", 3627);
                                casesHistoryAll.put("4/05/20", 4102);
                                casesHistoryAll.put("4/06/20", 4413);
                                casesHistoryAll.put("4/07/20", 4848);
                                casesHistoryAll.put("4/08/20", 5205);
                                casesHistoryAll.put("4/09/20", 5575);

                                data.setHistoryCasesAll(casesHistoryAll);
                                emitter.onNext(data);
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

    @Override
    public Covid19Data getData() {
        return data;
    }
}
