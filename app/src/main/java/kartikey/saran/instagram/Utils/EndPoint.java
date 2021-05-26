package kartikey.saran.instagram.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class EndPoint{
    private static String head = "https://instagram47.p.rapidapi.com/";
    private String url;
    Context mContext;

    public static void callApi(final Context context, final String url, final onCallApiResponse callback) {
        OkHttpClient client = new OkHttpClient();
        final Request.Builder request = new okhttp3.Request.Builder().url(head+url).get();
        request.addHeader("x-rapidapi-key", "3cda199ee2msh551f9c12dd4f68ap107863jsn3ba325dcc77c");
        request.addHeader("x-rapidapi-host", "instagram47.p.rapidapi.com");
        client.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("debugg", "DATABASE HELPER ERROR : " + e.toString());
                        callback.onError(e.toString(), 0);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.code() == 200){
                            try{
                                JSONObject responseObject = new JSONObject(response.body().string());
                                callback.onResponse(responseObject);
                            }catch (Exception e){
                                Log.e("debugg", e.toString());
                                callback.onError(e.toString(), 200);
                            }
                        }else{
                            Log.e("debugg", response.code() + ", " + String.valueOf(response.body()));
                            callback.onError(response.code() + ", " + String.valueOf(response.body()), response.code());
                        }
                    }
                });
            }
        });
    }

    public interface onCallApiResponse{
        void onResponse(JSONObject object);
        void onError(String errorMessage, Integer errorCode);
    }

}

