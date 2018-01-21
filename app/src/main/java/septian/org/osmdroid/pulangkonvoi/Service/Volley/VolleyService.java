package septian.org.osmdroid.pulangkonvoi.Service.Volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import septian.org.osmdroid.pulangkonvoi.Service.Volley.Interface.VolleyCallback;


public class VolleyService {
    VolleyCallback callback = null;

    Context mContext;
    RequestQueue queue;

    public VolleyService(VolleyCallback callback, Context mContext) {
        this.callback = callback;
        this.mContext = mContext;
        this.queue = Volley.newRequestQueue(mContext);
    }



//    public void postDataFile()
    public void postDataVolley(final String requestType, String url, final Map sendObj){
        try {
            Log.d("asd", "postDataVolley: "+url);
            StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(callback != null)
                                try {
                                    Log.d("asd", "onResponse: "+response);
                                    callback.notifySuccess(requestType,new JSONObject(response));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(callback != null)
                        if (error instanceof NetworkError) {
                            callback.notifyError("NetworkError",error);
                        } else if (error instanceof ServerError) {
                            callback.notifyError(requestType,error);
                        } else if (error instanceof AuthFailureError) {
                            callback.notifyError(requestType,error);
                        } else if (error instanceof ParseError) {
                            callback.notifyError(requestType,error);
                        } else if (error instanceof NoConnectionError) {
                            callback.notifyError("NoConnectionError",error);
                            Log.e("", "onErrorResponse: "+error);
                        } else if (error instanceof TimeoutError) {
                            callback.notifyError(requestType,error);
                        }
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return sendObj;
                }
            };
            strRequest.setRetryPolicy(new DefaultRetryPolicy( 30000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(strRequest);

        }catch(Exception e){

        }
    }

    public void getDataVolley(final String requestType, String url){
        try {
            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(callback != null)
                        callback.notifySuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error instanceof NetworkError) {
                        callback.notifyError("NetworkError",error);
                    } else if (error instanceof ServerError) {
                        callback.notifyError(requestType,error);
                        Log.d("asd", "onErrorResponse server: "+error);
                    } else if (error instanceof AuthFailureError) {
                        callback.notifyError(requestType,error);
                        Log.d("asd", "onErrorResponse notif: "+error);
                    } else if (error instanceof ParseError) {
                        callback.notifyError(requestType,error);
                        Log.d("asd", "onErrorResponse parse: "+error);
                    } else if (error instanceof NoConnectionError) {
                        callback.notifyError("NoConnectionError",error);
                        Log.d("asd", "onErrorResponse: "+error);
                    } else if (error instanceof TimeoutError) {
                        callback.notifyError("nope",error);
                        Log.d("asd", "onErrorResponse: "+error);

                    } else if (error.toString().equals("com.android.volley.TimeoutError")) {
                        callback.notifyError("nope",error);
                    }
                }
            });
            jsonObj.setRetryPolicy(new DefaultRetryPolicy( 30000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObj);
        }catch(Exception e){
            Log.d("asd", "getDataVolley: "+e);
            callback.notifyError("no", null);
        }
    }
    public void stopVolley(){
        queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }
}
