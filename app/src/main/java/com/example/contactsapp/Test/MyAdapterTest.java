package com.example.contactsapp.Test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsapp.Contact;
import com.example.contactsapp.R;

import java.util.ArrayList;

public class MyAdapterTest extends RecyclerView.Adapter<MyAdapterTest.MyViewHolder> {

    Context context;
    ArrayList<Contact> contactArrayList;

    public MyAdapterTest(Context context, ArrayList<Contact> contactArrayList) {
        this.context = context;
        this.contactArrayList = contactArrayList;
    }

    @NonNull
    @Override
    public MyAdapterTest.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
        View itemLayoutView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item,
                        parent, false);
        MyViewHolder vh = new MyViewHolder(itemLayoutView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterTest.MyViewHolder holder, int position) {

        Contact contact = contactArrayList.get(position);

        holder.firstName.setText(contact.getFirstNameContact());
        holder.lastName.setText(contact.getLastNameContact());
        holder.phoneNumber.setText(contact.getPhoneNumberContact());

    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView firstName, lastName, phoneNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.firstNameContact);
            lastName = itemView.findViewById(R.id.lastNameContact);
            phoneNumber = itemView.findViewById(R.id.phoneNumberContact);

        }
    }
}
