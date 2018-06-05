package ba.unsa.etf.rma.rijad.rmaspirala;

import android.media.UnsupportedSchemeException;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DohvatiKnjige extends AsyncTask<String, Integer, Void> {

    private ArrayList<Knjiga> knjige=new ArrayList<>();

    public interface IDohvatiKnjigeDone {
        public void onDohvatiDone(ArrayList<Knjiga> knjige);
    }

    private IDohvatiKnjigeDone pozivatelj;
    public DohvatiKnjige(IDohvatiKnjigeDone p) { pozivatelj=p; }

    @Override
    protected Void doInBackground(String... params) {

        String[] poJedno=params[0].split(";");

        for(int i=0;i<poJedno.length;i++) {
            String query = null;
            try {
                query = URLEncoder.encode(poJedno[i], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String url1 = "https://www.googleapis.com/books/v1/volumes?q="+query+"&maxResults=5";

            try {
                URL url = new URL(url1);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String rezultat = convertStreamToString(in);

                JSONObject jo = new JSONObject(rezultat);
                JSONArray items = jo.getJSONArray("items");
                for (int j = 0; j < items.length(); j++) {
                    JSONObject book = items.getJSONObject(j);

                    String id=null;
                    try {
                        id = book.getString("id");
                    } catch(JSONException e) {
                        id="No value for id";
                    }


                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                    String nazivKnjige = null;
                    try {
                        nazivKnjige=volumeInfo.getString("title");
                    } catch(JSONException e) {
                        nazivKnjige="No value for title";
                    }

                    ArrayList<Autor> autori=new ArrayList<>();

                    JSONArray authors=null;
                    try {
                        authors=volumeInfo.getJSONArray("authors");
                        for (int k = 0; k < authors.length(); k++) {
                            autori.add(new Autor(authors.getString(k), id));
                        }
                    } catch(JSONException e) {
                        autori.add(new Autor("No value for authors", id));
                    }

                    String opis=null;
                    try {
                        opis=volumeInfo.getString("description");
                    } catch(JSONException e) {
                        opis="No value for description";
                    }

                    String datumObjavljivanja=null;
                    try {
                        datumObjavljivanja=volumeInfo.getString("publishedDate");
                    } catch(JSONException e) {
                        datumObjavljivanja="No value for publishedDate";
                    }

                    JSONObject image = null;
                    String urlSlika=null;
                    try {
                        image=volumeInfo.getJSONObject("imageLinks");
                        urlSlika=image.getString("smallThumbnail");
                    } catch(JSONException e) {
                        urlSlika="http://www.ipwatchdog.com/wp-content/uploads/2017/09/no-value.jpg";
                    }
                    URL slika = new URL(urlSlika);

                    int brojStranica = 0;
                    try {
                        brojStranica=volumeInfo.getInt("pageCount");
                    } catch(JSONException e) {
                        brojStranica=-1;
                    }

                    knjige.add(new Knjiga(id,nazivKnjige,autori,opis,datumObjavljivanja,slika,brojStranica));
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }




    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pozivatelj.onDohvatiDone(knjige);
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line+"\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }
}
