package ba.unsa.etf.rma.rijad.rmaspirala;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static ba.unsa.etf.rma.rijad.rmaspirala.R.id.dPretraga;
import static ba.unsa.etf.rma.rijad.rmaspirala.R.id.tekstPretraga;

public class KategorijeAkt extends AppCompatActivity {
    private ArrayList<String> kategorije;
    private ArrayAdapter<String> adapter1;
    private ArrayList<Knjiga> knjige;
    private KontejnerKnjige kontejnerKnjige;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije_akt);

        final Button kategorija = (Button) findViewById(R.id.dDodajKategoriju);
        kategorija.setEnabled(false);
        final EditText pretraga = (EditText) findViewById(R.id.tekstPretraga);
        Button dodajKnjigu = (Button) findViewById(R.id.dDodajKnjigu);
        Button pretrazi = (Button) findViewById(R.id.dPretraga);
        final ListView lista = (ListView) findViewById(R.id.listaKategorija);

        kategorije = new ArrayList<>();
        kategorije.add("Roman");
        kategorije.add("Poezija");

        knjige = new ArrayList<>();

        Bitmap b=BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.rijad);
        ByteArrayOutputStream stream1=new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,100,stream1);

        knjige.add(new Knjiga("Rijad Pedljak", "Neki roman", stream1.toByteArray(), "Roman"));

        b=BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.tolstoj);
        ByteArrayOutputStream stream2=new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,100,stream2);

        knjige.add(new Knjiga("Lav Tolstoj", "Ana Karenjina", stream2.toByteArray(), "Roman"));

        b=BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.hajn);
        ByteArrayOutputStream stream3=new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,100,stream3);

        knjige.add(new Knjiga("Hajnrih Hajn", "Lorelaj", stream3.toByteArray(), "Poezija"));

        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, kategorije);
        lista.setAdapter(adapter1);

        pretrazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter1.getFilter().filter(pretraga.getText().toString(), new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                        if (i == 0) kategorija.setEnabled(true);
                        else kategorija.setEnabled(false);
                    }
                });
            }
        });

        kategorija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter1.add(pretraga.getText().toString());
                kategorije.add(pretraga.getText().toString());
                adapter1.notifyDataSetChanged();
                pretraga.setText("");
                kategorija.setEnabled(false);
                adapter1.getFilter().filter("");
            }
        });

        dodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent novi = new Intent(KategorijeAkt.this, DodavanjeKnjigeAkt.class);
                novi.putStringArrayListExtra("kategorije", kategorije);
                kontejnerKnjige=new KontejnerKnjige(knjige);
                novi.putExtra("knjige", kontejnerKnjige);
                KategorijeAkt.this.startActivityForResult(novi, 1);

            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent novi = new Intent(KategorijeAkt.this, ListaKnjigaAkt.class);
                kontejnerKnjige=new KontejnerKnjige(knjige);
                novi.putExtra("knjige", kontejnerKnjige);
                novi.putExtra("zanr", adapter1.getItem(i).toString());
                KategorijeAkt.this.startActivityForResult(novi,2);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==Activity.RESULT_OK) {
            kontejnerKnjige=(KontejnerKnjige) data.getSerializableExtra("knjige");
            knjige=kontejnerKnjige.getKnjige();
        }
        if(requestCode==2 && resultCode==Activity.RESULT_OK) {
            kontejnerKnjige=(KontejnerKnjige) data.getSerializableExtra("knjige");
            knjige=kontejnerKnjige.getKnjige();
        }
    }
}
