package kartikey.saran.instagram;

import android.util.Log;
import android.widget.VideoView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kartikey.saran.instagram.Models.Post;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostDetailViewModel extends ViewModel {

    private MutableLiveData<List<String>> comments = new MutableLiveData<>();

    public void setComments(List<String> comment) {
        comments.postValue(comment);
    }

    public LiveData<List<String>> getComments() {
        return comments;
    }

    public void loadComments() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://instagram47.p.rapidapi.com/post_comments?postid=2435143128484144113")
                .get()
                .addHeader("x-rapidapi-key", "bc5f19866fmsh79f87c0b6e8528dp10b8a8jsncc1be7f47585")
                .addHeader("x-rapidapi-host", "instagram47.p.rapidapi.com")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("debugg", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    loadData(new JSONObject(response.body().string()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadData(JSONObject json) {
        Log.e("JSON Body Comment", json.toString());
        List<String> comment1 = new ArrayList<>();
        try{
            JSONObject body = json.getJSONObject("body");
            JSONArray commentsArray = body.getJSONArray("comments");
            for(int i = 0; i<commentsArray.length();i++) {
                JSONObject user = commentsArray.getJSONObject(i).getJSONObject("user");
                String username = user.getString("username");
                String comment = commentsArray.getJSONObject(i).getString("text");
                comment1.add("<b>"+username+"</b> "+comment);
            }
        } catch (JSONException e) {
            Log.e("debugg", e.getMessage());
        }

        setComments(comment1);
    }
}
