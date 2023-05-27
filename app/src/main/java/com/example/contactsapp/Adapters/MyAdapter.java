package com.example.contactsapp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.example.contactsapp.Contact;
import com.example.contactsapp.ContactDetails;
import com.example.contactsapp.R;


import java.util.LinkedList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private LinkedList<Contact> contacts;
    private Context context;

    public MyAdapter(LinkedList<Contact> contacts, Context context) {
        this.contacts = new LinkedList<>(contacts);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_layout, parent, false);
        return new MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        holder.identification.setText(contact.getFirstNameContact() + " " + contact.getLastNameContact());
        holder.tel.setText(contact.getPhoneNumberContact());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        /*Glide.with(context)
                .load(contact.getImg_url())
                .apply(requestOptions)
                .into(holder.photo);*/
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView photo;
        public TextView identification;
        public TextView tel;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            photo = itemLayoutView.findViewById(R.id.contact_photo);
            identification = itemLayoutView.findViewById(R.id.fullName);
            tel = itemLayoutView.findViewById(R.id.phoneNumberContact);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Contact contact = contacts.get(position);
                Intent intent = new Intent(context, ContactDetails.class);
                intent.putExtra("contact", contact);
                context.startActivity(intent);
            }
        }
    }
}
