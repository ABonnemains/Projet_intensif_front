package fr.ensicaen.projetintensif;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class Recherche extends DialogFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recherche, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final Button rechercheButton = (Button) getView().findViewById(R.id.button_search);
        rechercheButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Recherche", Toast.LENGTH_LONG).show();
            }
        });
    }
}
