package kartikey.saran.instagram.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import kartikey.saran.instagram.Models.Post;
import kartikey.saran.instagram.PostDetail;
import kartikey.saran.instagram.R;
import kartikey.saran.instagram.Utils.Image;
import android.content.Context;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<Post> posts;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profileImage;
        private final TextView profileUsername;
        private final ImageView profileVerified;
        private final ImageView post;
        private final Button postLike;
        private final Button postComment;
        private final Button postMessage;
        private final TextView postNoOfLike;
        private final TextView postNoOfComments;
        private final TextView postcaption;
        private final TextView postHighlightedComment;
        private final VideoView postVideo;
        private final LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);

            profileImage = view.findViewById(R.id.item_post_imgView_profile_Img);
            profileUsername = view.findViewById(R.id.item_post_txtView_profile_Username);
            profileVerified = view.findViewById(R.id.item_post_imgView_profile_Verified);
            post = view.findViewById(R.id.item_post_imgView_post_image);
            postLike = view.findViewById(R.id.item_post_btn_like);
            postComment = view.findViewById(R.id.item_post_btn_comment);
            postMessage = view.findViewById(R.id.item_post_btn_pm);
            postNoOfLike = view.findViewById(R.id.item_post_txtView_noOfLikes);
            postNoOfComments = view.findViewById(R.id.item_post_noOfComments);
            postcaption = view.findViewById(R.id.item_post_txtView_caption);
            postHighlightedComment = view.findViewById(R.id.item_post_highlight_comment);
            postVideo = view.findViewById(R.id.item_post_vidView_post_video);
            linearLayout = view.findViewById(R.id.item_post_cL_lL_head);
        }

    }

    public PostAdapter(ArrayList<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_post, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Post innerPost = posts.get(position);
        new DownloadImageTask((ImageView) viewHolder.profileImage).execute(innerPost.getProfileUrl());
        viewHolder.profileUsername.setText(innerPost.getUsername());
        if(innerPost.getMediaType() == 1) {
            viewHolder.postVideo.setVisibility(View.GONE);
            viewHolder.post.setVisibility(View.VISIBLE);
            new DownloadImageTask((ImageView) viewHolder.post).execute(innerPost.getPostUrl());
        } else {
            viewHolder.post.setVisibility(View.GONE);
            viewHolder.postVideo.setVisibility(View.VISIBLE);
            viewHolder.postVideo.setVideoPath(innerPost.getPostUrl());
        }

        if(innerPost.isVerified()) {
            viewHolder.profileVerified.setVisibility(View.VISIBLE);
        }

        viewHolder.postNoOfLike.setText(Html.fromHtml("<b>"+innerPost.getNoOfLikes()+" likes"+"</b>"));
        viewHolder.postcaption.setText(Html.fromHtml("<b>"+innerPost.getUsername()+"</b> "+innerPost.getCaption()));
        viewHolder.postNoOfComments.setText("View all "+innerPost.getNoOfComments()+"comments");
        viewHolder.postHighlightedComment.setText(Html.fromHtml("<b>"+innerPost.getCommentUsername()+"</b> "+innerPost.getSelectedComments()));
        viewHolder.postLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.postLike.setBackgroundResource(R.drawable.ic_liked_24);
            }
        });
        viewHolder.postNoOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetail.class);
                intent.putExtra("id", innerPost.getId());
                intent.putExtra("username", innerPost.getUsername());
                intent.putExtra("caption", innerPost.getCaption());
                intent.putExtra("likes", innerPost.getNoOfLikes());
                intent.putExtra("profile", innerPost.getProfileUrl());
                intent.putExtra("url", innerPost.getPostUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
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
