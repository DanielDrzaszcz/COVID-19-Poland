package com.dandrzas.covid19poland.data.remotedatasource;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dandrzas.covid19poland.data.domain.Covid19Data;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;

public class RemoteDataSource implements RemoteDataSourceIF {
    private static RemoteDataSource ourInstance = new RemoteDataSource();
    private static RequestQueue queue;
    private static final String URLTodayData = "https://corona.lmao.ninja/countries/Poland";
    private final String URLHistoricalData = "https://corona.lmao.ninja/v2/historical/Poland";
    private static Covid19Data data;
    private static boolean request1DoneFlag, request2DoneFlag;

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
        request1DoneFlag = false;
        request2DoneFlag = false;

        return  Observable.create(emitter -> {

                // Request a response from the provided URLTodayData.
                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, URLTodayData, null,
                        (response) -> {
                            VolleyLog.d(response.toString());
                            try {
                                data.setCasesAll((int)response.get("cases"));
                                data.setCasesToday((int)response.get("todayCases"));
                                data.setCuredAll((int)response.get("recovered"));
                                data.setDeathsAll((int)response.get("deaths"));
                                data.setDeathsToday((int)response.get("todayDeaths"));

                                request1DoneFlag = true;
                                if(request2DoneFlag){
                                    emitter.onNext(data);
                                    emitter.onComplete();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                emitter.onError(e);

                            }
                        },
                        (error) -> {
                            emitter.onError(error);
                        });
                queue.add(jsonObjectRequest1);

            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, URLHistoricalData, null,
                    (response) -> {
                        VolleyLog.d(response.toString());
                        try {
                            JSONObject timeline = (JSONObject)response.get("timeline");
                            JSONObject cases = (JSONObject) timeline.get("cases");
                            Iterator<String> casesNames = cases.keys();
                            HashMap<String,Integer> casesHistoryMap = new LinkedHashMap<>();

                            while(casesNames.hasNext()){
                                String key = casesNames.next();
                                casesHistoryMap.put(key, (Integer)cases.get(key));
                            }

                            Log.d("APIv2Test: ", casesHistoryMap.keySet().toString());
                            data.setHistoryCasesAll(casesHistoryMap);

                            request2DoneFlag = true;
                            if(request1DoneFlag){
                                emitter.onNext(data);
                                emitter.onComplete();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            emitter.onError(e);

                        }
                    },
                    (error) -> {
                        emitter.onError(error);
                    });
            queue.add(jsonObjectRequest2);

        });
    }

    @Override
    public Covid19Data getData() {
        return data;
    }
}
