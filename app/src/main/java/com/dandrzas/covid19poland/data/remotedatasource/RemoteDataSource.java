package com.dandrzas.covid19poland.data.remotedatasource;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dandrzas.covid19poland.data.domain.Covid19HistoricalData;
import com.dandrzas.covid19poland.data.domain.Covid19TodayData;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import io.reactivex.Observable;

public class RemoteDataSource implements RemoteDataSourceIF {
    private static RemoteDataSource ourInstance = new RemoteDataSource();
    private static RequestQueue queue;
    private static final String URLTodayData = "https://corona.lmao.ninja/countries/Poland";
    private final String URLHistoricalData = "https://corona.lmao.ninja/v2/historical/Poland";
    private static Covid19TodayData dataToday;
    private static Covid19HistoricalData dataHistorical;

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
    public Observable<Covid19TodayData> downloadTodayData() {

        return  Observable.create(emitter -> {

                // Request a response from the provided URLTodayData.
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLTodayData, null,
                        (response) -> {
                            VolleyLog.d(response.toString());
                            try {

                                if (dataToday == null) dataToday = new Covid19TodayData();
                                dataToday.setCasesAll((int)response.get("cases"));
                                dataToday.setCasesToday((int)response.get("todayCases"));
                                dataToday.setCuredAll((int)response.get("recovered"));
                                dataToday.setDeathsAll((int)response.get("deaths"));
                                dataToday.setDeathsToday((int)response.get("todayDeaths"));

                                emitter.onNext(dataToday);
                                emitter.onComplete();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                dataToday = null;
                                emitter.onError(e);
                            }
                        },
                        (error) -> {
                            emitter.onError(error);
                        });
                queue.add(jsonObjectRequest);

        });
    }

    @Override
    public Covid19TodayData getTodayData() {
        return dataToday;
    }

    @Override
    public Observable<Covid19HistoricalData> downloadHistoricalData() {

        return Observable.create((emitter -> {

            // Request a response from the provided URLHistoricalData.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLHistoricalData, null,
                    (response) -> {
                        VolleyLog.d(response.toString());
                        try {

                            if (dataHistorical == null) dataHistorical = new Covid19HistoricalData();
                            JSONObject timeline = (JSONObject)response.get("timeline");

                            // get cases history
                            JSONObject cases = (JSONObject) timeline.get("cases");
                            Iterator<String> casesNames = cases.keys();
                            HashMap<String,Integer> casesHistoryMap = new LinkedHashMap<>();

                            while(casesNames.hasNext()){
                                String key = casesNames.next();
                                casesHistoryMap.put(key, (Integer)cases.get(key));
                            }

                            dataHistorical.setHistoryCasesAll(casesHistoryMap);

                            // get deaths history
                            JSONObject deaths = (JSONObject) timeline.get("deaths");
                            Iterator<String> deathsNames = deaths.keys();
                            HashMap<String,Integer> deathsHistoryMap = new LinkedHashMap<>();

                            while(deathsNames.hasNext()){
                                String key = deathsNames.next();
                                deathsHistoryMap.put(key, (Integer)deaths.get(key));
                            }

                            dataHistorical.setHistoryDeathsAll(deathsHistoryMap);

                            // get cured history
                            JSONObject cured = (JSONObject) timeline.get("recovered");
                            Iterator<String> curedNames = cured.keys();
                            HashMap<String,Integer> curedHistoryMap = new LinkedHashMap<>();

                            while(curedNames.hasNext()){
                                String key = curedNames.next();
                                curedHistoryMap.put(key, (Integer)cured.get(key));
                            }

                            dataHistorical.setHistoryCuredAll(curedHistoryMap);

                            emitter.onNext(dataHistorical);
                            emitter.onComplete();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            dataHistorical = null;
                            emitter.onError(e);
                        }
                    },
                    (error) -> {
                        emitter.onError(error);
                    });
            queue.add(jsonObjectRequest);
        }));

    }

    @Override
    public Covid19HistoricalData getHistoricalData() {
        return dataHistorical;
    }
}
