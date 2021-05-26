package kartikey.saran.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import kartikey.saran.instagram.Adapters.PostAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostDetail extends AppCompatActivity {

    List<String> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        TextView textView = findViewById(R.id.postDetail_txtView_comments);
        int id = getIntent().getIntExtra("id",0);
        String postUrl = getIntent().getStringExtra("url");
        String postLikes = getIntent().getStringExtra("likes");
        String username = getIntent().getStringExtra("username");
        String caption = getIntent().getStringExtra("caption");
        String profileUrl = getIntent().getStringExtra("profile");




        if(id == 0) {
            finish();
        } else {
            //Can pass the id show that particular post comments
            new DownloadImageTask((ImageView) findViewById(R.id.postDetail_imgView_profile_Img)).execute(profileUrl);
            new DownloadImageTask((ImageView) findViewById(R.id.postDetail_imgView_post_image)).execute(postUrl);

            ((TextView) findViewById(R.id.postDetail_txtView_noOfLikes)).setText(Html.fromHtml("<b>"+postLikes+" likes"+"</b>"));
            ((TextView) findViewById(R.id.postDetail_txtView_profile_Username)).setText(Html.fromHtml("<b>"+username+"</b>"));
            ((TextView) findViewById(R.id.postDetail_txtView_noOfLikes)).setText(Html.fromHtml("<b>"+username+"</b>"+caption));

            loadComments();
            for(String s : comments) {
                textView.append(Html.fromHtml(s)+"\n");
            }

        }
    }
    private void loadComments() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://instagram47.p.rapidapi.com/post_comments?postid=2435143128484144113")
                .get()
                .addHeader("x-rapidapi-key", "3cda199ee2msh551f9c12dd4f68ap107863jsn3ba325dcc77c")
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

    private void loadData(JSONObject json) {
        Log.e("JSON Body Comment", json.toString());
        comments = new ArrayList<>();
        try{
            JSONArray commentsArray = json.getJSONArray("comments");
            for(int i = 0; i<commentsArray.length();i++) {
                JSONObject user = commentsArray.getJSONObject(i).getJSONObject("user");
                String username = user.getString("username");
                String comment = commentsArray.getJSONObject(i).getString("text");
                comments.add("<b>"+username+"</b>"+comment);
            }
        } catch (JSONException e) {
            Log.e("debugg", e.getMessage());
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}