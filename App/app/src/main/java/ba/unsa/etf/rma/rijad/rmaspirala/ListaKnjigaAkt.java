package ba.unsa.etf.rma.rijad.rmaspirala;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListaKnjigaAkt extends AppCompatActivity {
    private ArrayList<Knjiga> knjige;
    private KnjigaAdapter adapterKnjige;
    private KontejnerKnjige k;
    private ArrayList<Knjiga> knjigeZanr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);

        final ListView lista=(ListView)findViewById(R.id.listaKnjiga);

        k=(KontejnerKnjige) getIntent().getSerializableExtra("knjige");
        knjige=k.getKnjige();
        knjigeZanr=new ArrayList<>();
        final String zanr=getIntent().getStringExtra("zanr");

        for(int i=0;i<knjige.size();i++) {
            if((knjige.get(i).getZanr().equals(zanr))) {
                knjigeZanr.add(knjige.get(i));
            }
        }

        adapterKnjige=new KnjigaAdapter(getApplicationContext(),knjigeZanr);
        lista.setAdapter(adapterKnjige);

        Button dugme=(Button)findViewById(R.id.dPovratak);

        dugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pocetni=new Intent();
                for(int i=0;i<knjige.size();i++) {
                    if((!knjige.get(i).getZanr().equals(zanr))) {
                        knjigeZanr.add(knjige.get(i));
                    }
                }
                k.setKnjige(knjigeZanr);
                pocetni.putExtra("knjige", k);
                setResult(Activity.RESULT_OK, pocetni);
                finish();
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(knjigeZanr.get(i).isZaObojiti()) {
                    knjigeZanr.get(i).setZaObojiti(false);
                }
                else {
                    knjigeZanr.get(i).setZaObojiti(true);
                }
                adapterKnjige.notifyDataSetChanged();
            }
        });
    }
}
