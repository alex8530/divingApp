package com.diverapp.diverapp.Admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.diverapp.diverapp.R;
import com.stripe.android.view.CardInputWidget;

public class CardActivity extends AppCompatActivity {

    CardInputWidget mCardInputWidget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);

//        Card cardToSave = mCardInputWidget.getCard();
//        if (cardToSave == null) {
//            mErrorDialogHandler.showError("Invalid Card Data");
//        }
    }
}
