package com.example.roommvp.view.adapter;

import com.example.roommvp.database.entity.Person;

public interface OnItemClickListener {
    void clickItem(Person person);

    void clickLongItem(Person person);
}
