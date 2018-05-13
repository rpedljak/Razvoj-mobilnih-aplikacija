package ba.unsa.etf.rma.rijad.rmaspirala;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import javax.crypto.AEADBadTagException;

/**
 * Created by Rijad on 25-Mar-18.
 */

public class KnjigaAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Knjiga> knjige;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=View.inflate(context,R.layout.element_lista_knjiga,null);
        ImageView slika=(ImageView)v.findViewById(R.id.eNaslovna);
        TextView naziv=(TextView)v.findViewById(R.id.eNaslov);
        TextView autor=(TextView)v.findViewById(R.id.eAutor);
        LinearLayout lejaut=(LinearLayout)v.findViewById(R.id.lejaut);

        Bitmap b= BitmapFactory.decodeByteArray(knjige.get(i).getSlika(),0,knjige.get(i).getSlika().length);

        slika.setImageBitmap(b);
        naziv.setText(knjige.get(i).getNazivKnjige());
        autor.setText(knjige.get(i).getImeAutora());

        if(knjige.get(i).isZaObojiti()) lejaut.setBackgroundColor(0xffaabbed);

        v.setTag(knjige.get(i).getIdKnjige());
        return v;
    }
}
