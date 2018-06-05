package ba.unsa.etf.rma.rijad.rmaspirala;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FragmentPreporuci extends Fragment {

    private ArrayList<String> kategorije;
    private KontejnerKnjige k;

    private ArrayList<String> kontakti=new ArrayList<>();
    private ArrayList<String> mailovi=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Knjiga knjiga;
    Spinner spin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_preporuci,container,false);

        if(getArguments().containsKey("knjiga")) knjiga=(Knjiga)getArguments().getSerializable("knjiga");
        if(getArguments().containsKey("knjige")) {
            k= (KontejnerKnjige) getArguments().getSerializable("knjige");
        }
        if(getArguments().containsKey("kategorije")) kategorije=getArguments().getStringArrayList("kategorije");

        ImageView slika = (ImageView) v.findViewById(R.id.slikaPreporuci);
        TextView naziv = (TextView) v.findViewById(R.id.nazivPreporuci);
        TextView autor = (TextView) v.findViewById(R.id.autorPreporuci);
        TextView datum = (TextView) v.findViewById(R.id.datumPreporuci);
        TextView opis = (TextView) v.findViewById(R.id.opisPreporuci);
        TextView stranice = (TextView) v.findViewById(R.id.brojPreporuci);

        if (knjiga.getSlikaUri()!=null) {
            Picasso.get().load(knjiga.getSlikaUri()).into(slika);
        } else {
            Picasso.get().load(knjiga.getSlika().toString()).into(slika);
            //new URLToBitmapTask(slika).execute(knjige.get(i).getSlika());
        }


        naziv.setText(knjiga.getNaziv());
        String autori="";
        int j;
        for(j=0;j<knjiga.getAutori().size()-1;j++) {
            autori+=knjiga.getAutori().get(j).getImeiPrezime()+" ,";
        }
        autori+=knjiga.getAutori().get(j).getImeiPrezime();
        autor.setText(autori);
        if(knjiga.getDatumObjavljivanja()!=null) {
            datum.setText(knjiga.getDatumObjavljivanja());
        } else {
            datum.setText("No published date");
        }

        if(knjiga.getOpis()!=null) {
            opis.setText(knjiga.getOpis());
        } else {
            opis.setText("No description");
        }

        if(knjiga.getBrojStranica()!=0) {
            stranice.setText(Integer.toString(knjiga.getBrojStranica()));
        } else {
            stranice.setText(Integer.toString(0));
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadContacts();

        spin=(Spinner)getView().findViewById(R.id.sKontakti);

        adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_expandable_list_item_1,mailovi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        Button preporuci=(Button)getView().findViewById(R.id.dPosalji);

        preporuci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

    }

    protected void sendEmail() {

        TextView naziv=(TextView)getView().findViewById(R.id.nazivPreporuci);
        TextView autor=(TextView)getView().findViewById(R.id.autorPreporuci);

        Log.i("Send email", "");
        String[] TO = {spin.getSelectedItem().toString()};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Preporuka za knjigu");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Zdravo "+kontakti.get(spin.getSelectedItemPosition())+", \nPročitaj knjigu "+knjiga.getNaziv()+" od "+knjiga.getAutori().get(0).getImeiPrezime()+"!");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            ListeFragment kf=new ListeFragment();
            Bundle arg=new Bundle();
            arg.putSerializable("knjige", k);
            arg.putStringArrayList("kategorije", kategorije);

            kf.setArguments(arg);
            getFragmentManager().beginTransaction().replace(R.id.fragment1,kf).commit();

            Log.i("Finished sending email", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadContacts(){
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cursor1 = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                while (cursor1.moveToNext()){
                    String ime = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String email = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    if(email!=null){
                        kontakti.add(ime);
                        mailovi.add(email);
                    }
                }
                cursor1.close();
            }
        }
        cursor.close();
    }
}
