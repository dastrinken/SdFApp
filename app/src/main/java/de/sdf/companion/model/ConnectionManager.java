package de.sdf.companion.model;

import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.sdf.companion.model.RESTClient.KeyValuePairs;
import de.sdf.companion.model.RESTClient.Request;
import de.sdf.companion.model.RESTClient.RequestMethods;

public class ConnectionManager {
    /* use ConnectionManager only as singleton */
    private static ConnectionManager singleton = null;

    public static ConnectionManager getInstance() {
        if (singleton == null) singleton = new ConnectionManager(new AsyncRESTRequest());
        return singleton;
    }

    public static ConnectionManager getInstance(AsyncRESTRequest asyncRESTRequest) {
        if (singleton == null) singleton = new ConnectionManager(asyncRESTRequest);
        return singleton;
    }

    private final AsyncRESTRequest asyncRESTRequest;
    public ConnectionManager(AsyncRESTRequest asyncRESTRequest) {
        this.asyncRESTRequest = asyncRESTRequest;
    }

    /* URIs */
    //TODO: Change URI values when page goes online
    private static final String URI = "https://sdf-rg.de";
    private static final String URI_LOGIN = URI + "/system/database.php";

    /* Log in */
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void login(String username, String password, ConnectionInterface listener) {
        final Request request = new Request(URI_LOGIN, RequestMethods.POST);
        request.setBody(new KeyValuePairs(new String[] {"login", String.valueOf(true), "android", String.valueOf(true), "username", username, "password", password}));
        asyncRESTRequest.execute(request, (response) -> {
            final boolean success = (response != null) && (!response.isEmpty());
            if(success) {
                authToken = response.get(0);
            }
            listener.onLoginProcessed(success);
        });
    }

    /* main logic REST handler */
    interface RESTResponseHandler {
        void processResponse(List<String> response);
    }

    private static class AsyncRESTRequest {
        private final Executor executor;
        private final Handler handler;
        private final RESTClient restClient;

        AsyncRESTRequest(Executor executor, Handler handler, RESTClient restClient) {
            this.executor = executor;
            this.handler = handler;
            this.restClient = restClient;
        }

        AsyncRESTRequest() {
            this(Executors.newSingleThreadExecutor(), new Handler(Looper.getMainLooper()), new RESTClient());
        }

        void execute(Request request, RESTResponseHandler responseHandler) {
            executor.execute(() -> {
                final List<String> response = doInBackground(request);
                handler.post(() -> onPostExecute(responseHandler, response));
            });
        }

        List<String> doInBackground(Request request) {
            return restClient.httpRequest(request);
        }

        private void onPostExecute(RESTResponseHandler responseHandler, List<String> response) {
            if (responseHandler != null) responseHandler.processResponse(response);
        }
    }
}
