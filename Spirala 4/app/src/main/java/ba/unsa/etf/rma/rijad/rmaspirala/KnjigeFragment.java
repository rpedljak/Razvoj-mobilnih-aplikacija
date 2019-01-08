package ba.unsa.etf.rma.rijad.rmaspirala;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class KnjigeFragment extends Fragment {

    private KnjigaAdapter adapter;
    private ArrayList<Knjiga> knjigeZaPrikazat;
    private BazaOpenHelper bazaOpenHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_lista_knjiga_akt,container,false);

        bazaOpenHelper=new BazaOpenHelper(getActivity());

        ListView lv=(ListView)v.findViewById(R.id.listaKnjiga);
        knjigeZaPrikazat=new ArrayList<>();

        if(getArguments().containsKey("zanr")) {
            long id=bazaOpenHelper.dajKategoriju(getArguments().getString("zanr"));

            if(id==-1) {
                Toast.makeText(getActivity(), R.string.kategorija_ne_postoji, Toast.LENGTH_SHORT).show();
                knjigeZaPrikazat=new ArrayList<>();
            }
            else {
                knjigeZaPrikazat=bazaOpenHelper.knjigeKategorije(id);
            }

            /*for(Knjiga k : knjige) {
                if(zanr.equals(k.getZanr())) {
                    knjigeZaPrikazat.add(k);
                }
            }*/



        }
        else if(getArguments().containsKey("autor")) {
            Autor a=(Autor)getArguments().getSerializable("autor");
            long id=bazaOpenHelper.dajAutora(a.getImeiPrezime());

            if(id==-1) {
                Toast.makeText(getActivity(), R.string.autor_ne_postoji, Toast.LENGTH_SHORT).show();
                knjigeZaPrikazat=new ArrayList<>();
            }
            else {
                knjigeZaPrikazat=bazaOpenHelper.knjigeAutora(id);
            }

            /*for(Knjiga k : knjige) {
                if(a.getNazivAutora().equals(k.getImeAutora())) {
                    knjigeZaPrikazat.add(k);
                }
            }*/
        }

        adapter = new KnjigaAdapter(getActivity(), knjigeZaPrikazat);
        lv.setAdapter(adapter);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button povratak=(Button)getView().findViewById(R.id.dPovratak);
        ListView lv=(ListView)getView().findViewById(R.id.listaKnjiga);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(knjigeZaPrikazat.get(i).isZaObojiti()) {
                    knjigeZaPrikazat.get(i).setZaObojiti(false);
                    bazaOpenHelper.setBojaKnjige(knjigeZaPrikazat.get(i).getNaziv(), false);
                }
                else {
                    knjigeZaPrikazat.get(i).setZaObojiti(true);
                    bazaOpenHelper.setBojaKnjige(knjigeZaPrikazat.get(i).getNaziv(), true);
                }
                adapter.notifyDataSetChanged();
            }
        });

        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListeFragment lf=new ListeFragment();

                getFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager().beginTransaction().replace(R.id.fragment1,lf).commit();
            }
        });
    }
}
