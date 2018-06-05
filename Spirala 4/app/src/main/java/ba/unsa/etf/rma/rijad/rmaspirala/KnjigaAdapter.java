package ba.unsa.etf.rma.rijad.rmaspirala;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rijad on 25-Mar-18.
 */

public class KnjigaAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Knjiga> knjige;
    private OnPreporuciClick opc;

    public KnjigaAdapter(Context context, ArrayList<Knjiga> knjige) {
        this.context = context;
        this.knjige = knjige;
    }

    @Override
    public int getCount() {
        return knjige.size();
    }

    @Override
    public Object getItem(int i) {
        return knjige.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.element_lista_knjiga, null);
        ImageView slika = (ImageView) v.findViewById(R.id.eNaslovna);
        TextView naziv = (TextView) v.findViewById(R.id.eNaziv);
        TextView autor = (TextView) v.findViewById(R.id.eAutor);
        TextView datum = (TextView) v.findViewById(R.id.eDatumObjavljivanja);
        TextView opis = (TextView) v.findViewById(R.id.eOpis);
        TextView stranice = (TextView) v.findViewById(R.id.eBrojStranica);
        LinearLayout lejaut = (LinearLayout) v.findViewById(R.id.lejaut);

        if (knjige.get(i).getSlikaUri() != null) {
            Picasso.get().load(knjige.get(i).getSlikaUri()).into(slika);
        } else {
            Picasso.get().load(knjige.get(i).getSlika().toString()).into(slika);
            //new URLToBitmapTask(slika).execute(knjige.get(i).getSlika());
        }


        naziv.setText(knjige.get(i).getNaziv());
        String autori="";
        int j;
        for(j=0;j<knjige.get(i).getAutori().size()-1;j++) {
            autori+=knjige.get(i).getAutori().get(j).getImeiPrezime()+", ";
        }
        autori+=knjige.get(i).getAutori().get(j).getImeiPrezime();
        autor.setText(autori);
        if(knjige.get(i).getDatumObjavljivanja()!=null) {
            datum.setText(knjige.get(i).getDatumObjavljivanja());
        } else {
            datum.setText("No published date");
        }

        if(knjige.get(i).getOpis()!=null) {
            opis.setText(knjige.get(i).getOpis());
        } else {
            opis.setText("No description");
        }

        if(knjige.get(i).getBrojStranica()!=0) {
            stranice.setText(Integer.toString(knjige.get(i).getBrojStranica()));
        } else {
            stranice.setText(Integer.toString(0));
        }

        if (knjige.get(i).isZaObojiti())
            lejaut.setBackgroundColor(context.getResources().getColor(R.color.bojaZaKnjige));

        Button preporuci=(Button)v.findViewById(R.id.dPreporuci);

        try {
            opc=(OnPreporuciClick)context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString());
        }

        preporuci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opc.onRecommendClick(knjige.get(i));
            }
        });

        v.setTag(knjige.get(i).getId());
        return v;
    }

    public interface OnPreporuciClick {
        public void onRecommendClick(Knjiga k);
    }
}
