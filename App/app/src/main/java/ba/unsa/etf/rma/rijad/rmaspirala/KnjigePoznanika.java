package ba.unsa.etf.rma.rijad.rmaspirala;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class KnjigePoznanika extends IntentService {

    public static int STATUS_START=0;
    public static int STATUS_FINISH=1;
    public static int STATUS_ERROR=2;

    public KnjigePoznanika() {
        super(null);
    }

    public KnjigePoznanika(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver=intent.getParcelableExtra("receiver");

        Bundle b=new Bundle();

        receiver.send(STATUS_START,Bundle.EMPTY);

        ArrayList<Knjiga> knjige=new ArrayList<>();

        String idKorisnika=intent.getStringExtra("idKorisnika");
        try {
            idKorisnika = URLEncoder.encode(idKorisnika, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url1="https://www.googleapis.com/books/v1/users/"+idKorisnika+"/bookshelves";

        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);

            JSONObject rez = new JSONObject(rezultat);
            JSONArray shelves = rez.getJSONArray("items");
            for(int i=0;i<shelves.length();i++) {
                JSONObject bookshelf=shelves.getJSONObject(i);
                String idShelf=bookshelf.getString("id");

                try {
                    idShelf = URLEncoder.encode(idShelf, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String link="https://www.googleapis.com/books/v1/users/"+idKorisnika+"/bookshelves/"+idShelf+"/volumes";

                URL novi=new URL(link);
                HttpURLConnection urlConnection1=(HttpURLConnection)novi.openConnection();
                InputStream in1=new BufferedInputStream((urlConnection1.getInputStream()));
                String rezultat1=convertStreamToString(in1);

                JSONObject jo = new JSONObject(rezultat1);
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
            }

        } catch (IOException | JSONException e) {
            receiver.send(STATUS_ERROR,Bundle.EMPTY);
        }

        b.putParcelableArrayList("listaKnjiga",knjige);
        receiver.send(STATUS_FINISH,b);
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
