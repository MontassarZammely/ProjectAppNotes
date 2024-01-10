package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteActivity extends AppCompatActivity {

    EditText TitleNote,contentNote;
    ImageButton AddBtn;
    TextView titleTextView,ContentEditText,pageTitleTextView;

    String title,content,docId;

    boolean isEditMode = false;

    TextView DeleteTextviewBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        TitleNote=findViewById(R.id.note_title_id);
        contentNote=findViewById(R.id.content_text_id);
        AddBtn=findViewById(R.id.save_btn_id);
        titleTextView=findViewById(R.id.note_title_id);
        ContentEditText=findViewById(R.id.content_text_id);
        pageTitleTextView = findViewById(R.id.title_id);
        DeleteTextviewBTN= findViewById(R.id.deleteBTN);

        // get data :
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");
        if (docId !=null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleTextView.setText(title);
        ContentEditText.setText(content);

        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            // delete button twali visible
            DeleteTextviewBTN.setVisibility(View.VISIBLE);
        }
        AddBtn.setOnClickListener((v)->saveNote());

        DeleteTextviewBTN.setOnClickListener((v) -> deleteNoteFromFirebase());

    }

    void saveNote(){
        String Note_title=TitleNote.getText().toString();
        String Note_Content=contentNote.getText().toString();

        if(Note_title.isEmpty()){
            TitleNote.setError("Title is Required");
            return ;
        }

        // Model class pour qu'on ajoute el note fel firebase

        Note note = new Note();
        note.setTitle(Note_title);
        note.setContent(Note_Content);
        note.setTimestamp(Timestamp.now());

        SaveToFireBase(note);
    }

    void SaveToFireBase(Note note){
        // save the note fl collection as documents ( fi west getCurrentUserReference bech tetsab el note as document)
        DocumentReference documentReference;

        // modification :

        if (isEditMode)
        {
            documentReference = Unity.getCurrentUserReference().document(docId);

        }
        else { // creation
            documentReference = Unity.getCurrentUserReference().document();
        }
        // add the note to the database
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // note added
                    Toast.makeText(NoteActivity.this,"Note added",Toast.LENGTH_SHORT).show();
                    finish();
                }else { // note not added
                    Toast.makeText(NoteActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    void deleteNoteFromFirebase(){

        DocumentReference documentReference;

        documentReference = Unity.getCurrentUserReference().document(docId);


        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // note deleted
                    Toast.makeText(NoteActivity.this,"Note deleted ",Toast.LENGTH_SHORT).show();
                    finish();
                }else { // note not deleted
                    Toast.makeText(NoteActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    }

