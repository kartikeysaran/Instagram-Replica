package kartikey.saran.instagram.Utils;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;

public class Image {
    private String url;
    public Image(String url) {
        this.url = url;
    }

    public static Drawable displayImage(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}

