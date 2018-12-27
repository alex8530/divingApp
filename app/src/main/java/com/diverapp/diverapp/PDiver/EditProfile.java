package com.diverapp.diverapp.PDiver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.diverapp.diverapp.R;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_a_edit_profile);

        Spinner s1 =   findViewById(R.id.spinner);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.country_arrays, R.layout.row_spinner_item);
        s1.setAdapter(adapter);
    }
}
