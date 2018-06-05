package ba.unsa.etf.rma.rijad.rmaspirala;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/* Task koji iz URL-a prikazuje Bitmap u ImageView */

public class URLToBitmapTask extends AsyncTask<URL, String, Bitmap> {

    private final static String TAG="Fail";
    private ImageView imageView;

    public URLToBitmapTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(URL... params) {
        Bitmap bitmap = null;
        try {
            URL url = params[0];
            bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
