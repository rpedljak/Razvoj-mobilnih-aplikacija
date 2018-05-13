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

import java.util.ArrayList;
import java.util.List;

public class KnjigeFragment extends Fragment {

    private ArrayList<Knjiga> knjige;
    private KnjigaAdapter adapter;
    private KontejnerKnjige kontejner;
    private ArrayList<Knjiga> knjigeZaPrikazat;
    private ArrayList<String> kategorije;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_lista_knjiga_akt,container,false);

        kategorije=getArguments().getStringArrayList("kategorije");

        ListView lv=(ListView)v.findViewById(R.id.listaKnjiga);
        knjigeZaPrikazat=new ArrayList<>();

        kontejner=(KontejnerKnjige)getArguments().getSerializable("knjige");
        knjige=kontejner.getKnjige();

        if(getArguments().containsKey("zanr")) {
            String zanr=getArguments().getString("zanr");

            for(Knjiga k : knjige) {
                if(zanr.equals(k.getZanr())) {
                    knjigeZaPrikazat.add(k);
                }
            }
        }
        else if(getArguments().containsKey("autor")) {
            Autor a=(Autor)getArguments().getSerializable("autor");
            for(Knjiga k : knjige) {
                if(a.getNazivAutora().equals(k.getImeAutora())) {
                    knjigeZaPrikazat.add(k);
                }
            }
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
                }
                else {
                    knjigeZaPrikazat.get(i).setZaObojiti(true);
                }
                adapter.notifyDataSetChanged();
            }
        });

        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListeFragment lf=new ListeFragment();
                Bundle arg=new Bundle();
                kontejner.setKnjige(knjige);

                arg.putStringArrayList("kategorije", kategorije);
                arg.putSerializable("knjige", kontejner);

                lf.setArguments(arg);

                getFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager().beginTransaction().replace(R.id.fragment1,lf).commit();
            }
        });
    }
}
