package kartikey.saran.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import kartikey.saran.instagram.Models.Post;
import kartikey.saran.instagram.ui.home.HomeViewModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostDetail extends AppCompatActivity {

    List<String> comments;
    private PostDetailViewModel PostDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        PostDetailViewModel = new ViewModelProvider(this).get(PostDetailViewModel.class);
        TextView textView = findViewById(R.id.postDetail_txtView_comments);
        String id = getIntent().getStringExtra("id");
        String postUrl = getIntent().getStringExtra("url");
        String postLikes = getIntent().getStringExtra("likes");
        String username = getIntent().getStringExtra("username");
        String caption = getIntent().getStringExtra("caption");
        String profileUrl = getIntent().getStringExtra("profile");

        if(id.length() == 0) {
            finish();
        } else {
            //Can pass the id show that particular post comments
            new DownloadImageTask((ImageView) findViewById(R.id.postDetail_imgView_profile_Img)).execute(profileUrl);
            new DownloadImageTask((ImageView) findViewById(R.id.postDetail_imgView_post_image)).execute(postUrl);

            ((TextView) findViewById(R.id.postDetail_txtView_noOfLikes)).setText(Html.fromHtml("<b>"+postLikes+" likes"+"</b>"));
            ((TextView) findViewById(R.id.postDetail_txtView_profile_Username)).setText(Html.fromHtml("<b>"+username+"</b>"));
            ((TextView) findViewById(R.id.postDetail_txtView_noOfLikes)).setText(Html.fromHtml("<b>"+username+"</b>"+caption));

            PostDetailViewModel.loadComments();
            PostDetailViewModel.getComments().observe(this, new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> strings) {
                    textView.setText("");
                    for(String s:strings) {
                        textView.append(Html.fromHtml(s) +"\n");
                    }
                    findViewById(R.id.postDetail_progress).setVisibility(View.GONE);
                }
            });

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