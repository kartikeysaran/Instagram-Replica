package kartikey.saran.instagram.Models;

import org.json.JSONObject;

public class Post {
    private String username;
    private String name;
    private String postUrl;
    private boolean liked;
    private String profileUrl;
    private String caption;
    private String selectedComments;
    private String noOfComments;
    private String noOfLikes;
    private String commentUsername;
    private boolean verified;
    private int mediaType;
    private int height;
    private String id;

    public String getId() {
        return id;
    }



    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    private int width;

    public Post(String username, String name, String postUrl, boolean liked, String profileUrl, String caption, String selectedComments,
                String noOfComments, String noOfLikes, String commentUsername, boolean verified, int mediaType, int width, int height, String id) {

        this.username = username;
        this.id = id;
        this.mediaType = mediaType;
        this.name = name;
        this.postUrl = postUrl;
        this.liked = liked;
        this.profileUrl = profileUrl;
        this.caption = caption;
        this.selectedComments = selectedComments;
        this.noOfComments = noOfComments;
        this.noOfLikes = noOfLikes;
        this.commentUsername = commentUsername;
        this.verified = verified;
        this.height = height;
        this.width = width;

    }

    public boolean isVerified() {
        return verified;
    }

    public String getNoOfComments() {
        return noOfComments;
    }

    public String getNoOfLikes() {
        return noOfLikes;
    }

    public String getCommentUsername() {
        return commentUsername;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public boolean isLiked() {
        return liked;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getCaption() {
        return caption;
    }

    public int getMediaType() {
        return mediaType;
    }

    public String getSelectedComments() {
        return selectedComments;
    }
}
