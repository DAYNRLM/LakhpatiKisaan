package com.nrlm.lakhpatikisaan.network.vollyCall;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nrlm.lakhpatikisaan.utils.AppUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyService {
    VolleyResult mResultCallback = null;
    Context mContext;
    AppUtils appUtility;

    public static VolleyService volleyService ;
    public static VolleyService getInstance(Context context)
    {
        if (volleyService == null)
            volleyService = new VolleyService(context);
        return volleyService;
    }
    public VolleyService(Context mContext) {
        this.mContext = mContext;
        appUtility = AppUtils.getInstance();
    }

    public void postDataVolley(String requestType, String url, JSONObject sendObj, VolleyResult mResultCallback) {
        this.mResultCallback = mResultCallback;
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);
            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST, url, sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                }
            }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-type", "application/json");
                headers.put("securityToken", "n{j5Y[<!Ps*HWCWg");
                return headers;
            }};
            // SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObj);
            jsonObj.setRetryPolicy(new DefaultRetryPolicy(50000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.getCache().clear();
            queue.add(jsonObj);

        } catch (Exception e) {
            appUtility.showLog("post request Exception:- "+e,VolleyService.class);
        }
    }



}
