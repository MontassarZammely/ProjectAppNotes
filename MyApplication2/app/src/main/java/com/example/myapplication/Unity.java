package com.example.myapplication;

import static java.lang.String.format;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.SimpleDateFormat;

public class Unity {

    static CollectionReference getCurrentUserReference(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Notes").document(user.getUid()).collection("my_note");
    }

    static String timestampToString(Timestamp timestamp){
       return  new SimpleDateFormat ("MM/dd/yyYY").format(timestamp.toDate());
    }
}
