package com.dandrzas.covid19poland.remotedatasource;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class NetworkHandler implements NetworkHandlerIF {
    private static NetworkHandler ourInstance = new NetworkHandler();
    private Observable<Integer> covid19DataEmitter = Observable.create(new Covid19DataEmitter());
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
    public Observable<Integer> downloadData() {
        return  covid19DataEmitter;
    }

    private class Covid19DataEmitter implements ObservableOnSubscribe<Integer> {

        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) {
            // Request a response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                    (response) -> {
                        VolleyLog.d(response.toString());
                        try {
                            emitter.onNext((Integer)response.get("cases"));
                            emitter.onNext((Integer)response.get("todayCases"));
                            emitter.onNext((Integer)response.get("recovered"));
                            emitter.onNext((Integer)response.get("deaths"));
                            emitter.onNext((Integer)response.get("todayDeaths"));
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
