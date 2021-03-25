package com.example.roommvp.view.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommvp.R;
import com.example.roommvp.view.edit.EditActivity;
import com.example.roommvp.database.AppDatabase;
import com.example.roommvp.database.entity.Person;
import com.example.roommvp.presenter.main.MainPresenterImp;
import com.example.roommvp.view.adapter.PeopleAdapter;
import com.example.roommvp.utils.Constants;
import com.example.roommvp.view.adapter.OnItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView, OnItemClickListener {

    private MainPresenterImp presenterImp;
    private PeopleAdapter adapter;
    private TextView mTextEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        //db
        AppDatabase db = AppDatabase.getDatabase(getApplication());
        presenterImp = new MainPresenterImp(this, db.personModel());

        //view
        FloatingActionButton fabAdd =  findViewById(R.id.fabAdd);
        mTextEmpty = findViewById(R.id.tv_empty);
        RecyclerView recyclerView = findViewById(R.id.rv_list);

        //adapter
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PeopleAdapter(this);
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenterImp.addNewPerson();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenterImp.populatePeople();
    }

    @Override
    public void showAddPerson() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    @Override
    public void setPersons(List<Person> persons) {
        mTextEmpty.setVisibility(View.GONE);
        adapter.setValues(persons);
    }

    @Override
    public void showEditScreen(long id) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(Constants.PERSON_ID, id);
        startActivity(intent);
    }

    @Override
    public void showDeleteConfirmDialog(Person person) {
        final long personId = person.id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Apakah anda yakin akan menghapus data ini?");

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenterImp.delete(personId);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public void showEmptyMessage() {
        mTextEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void clickItem(Person person) {
        presenterImp.openEditScreen(person);
    }

    @Override
    public void clickLongItem(Person person) {
        presenterImp.openConfirmDeleteDialog(person);
    }
}
