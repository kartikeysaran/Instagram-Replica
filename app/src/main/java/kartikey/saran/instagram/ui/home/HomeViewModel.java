package kartikey.saran.instagram.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import kartikey.saran.instagram.Models.Post;
import kartikey.saran.instagram.Utils.EndPoint;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Context;
import android.util.Log;
import android.content.Context;
import android.widget.ProgressBar;


public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>();

    public void setPosts(ArrayList<Post> post) {
        posts.postValue(post);
    }

    public LiveData<ArrayList<Post>> getPosts() {
        return posts;
    }

    public void loadPosts() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://instagram47.p.rapidapi.com/user_posts?username=taneja.gaurav")
                .get()
                .header("x-rapidapi-key", "3cda199ee2msh551f9c12dd4f68ap107863jsn3ba325dcc77c")
                .header("x-rapidapi-host", "instagram47.p.rapidapi.com")
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
        Log.e("JSON", json.toString());
        ArrayList<Post> arrayList = new ArrayList<>();
            try{
                JSONObject bodyJSON = json.getJSONObject("body");
                JSONArray items = bodyJSON.getJSONArray("items");
                for(int i = 0; i<bodyJSON.getInt("num_results");i++) {
                    JSONObject object = items.getJSONObject(i);
                    JSONObject user = object.getJSONObject("user");
                    String username = user.getString("username");
                    String name = user.getString("full_name");
                    boolean verified = user.getBoolean("is_verified");
                    String profileUrl = user.getString("profile_pic_url");
                    String caption = object.getJSONObject("caption").getString("text");
                    String noOfComments = object.getString("comment_count");
                    String noOfLike = object.getString("like_count");
                    /*
                    String selectedComment = object.getJSONObject("comments").getJSONObject(0).getString("text");
                    String commentUsername = object.getJSONObject("comments").getJSONObject(0).getJSONObject("user").getString("username");

                     */
                    int id = object.getInt("id");
                    boolean liked = object.getBoolean("has_liked");
                    int mediaType = object.getInt("media_type");
                    String postUrl = null;
                    int width = 0;
                    int height = 0;
                    if (mediaType == 1) {
                        postUrl = object.getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(0).getString("url");
                        width = object.getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(0).getInt("width");
                        height = object.getJSONObject("image_versions2").getJSONArray("candidates").getJSONObject(0).getInt("height");
                        arrayList.add(new Post(username, name, postUrl, liked, profileUrl, caption, "Nice", noOfComments
                                , noOfLike, "abd", verified, mediaType, width, height, id));
                    }
                }
            } catch (JSONException e) {
                Log.e("debugg", e.getMessage());
            }
            setPosts(arrayList);
        }
    }

