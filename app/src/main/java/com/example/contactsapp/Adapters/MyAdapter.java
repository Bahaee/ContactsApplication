package com.example.contactsapp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;


import com.example.contactsapp.Contact;
import com.example.contactsapp.ContactDetails;
import com.example.contactsapp.R;


import java.util.LinkedList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private LinkedList<Contact> contacts;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(LinkedList<Contact> contacts, Context context) {
        this.contacts = new LinkedList<Contact>() ;
        this.contacts.addAll( contacts );
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // create a new view
        View itemLayoutView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_layout,
                        parent, false);
        MyViewHolder vh = new MyViewHolder(itemLayoutView );
        return vh;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // - get element from your dataset at this position
        holder.contact=contacts.get(position);

        // - replace the contents of the view with that element
        holder.identification.setText(contacts.get(position).getFirstNameContact()+" "+contacts.get(position).getLastNameContact());
        holder.tel.setText(contacts.get(position).getPhoneNumberContact());
        // Reference to an image file in Cloud Storage
        //StorageReference storageReference =
                //FirebaseStorage.getInstance().getReferenceFromUrl(contacts.get(position).getImg_url());
        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        //Glide.with(context /* context */)
               // .load(storageReference)
                //.into(holder.photo);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return contacts.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        Contact contact;
        public TextView identification;
        public TextView tel;

        // Context is a reference to the activity that contain the the recycler view
        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            identification =itemLayoutView.findViewById(R.id.fullName);
            tel= itemLayoutView.findViewById(R.id.phoneNumberContact);
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent myintent= new Intent(context, ContactDetails.class);
            myintent.putExtra("contact", contact);
            context.startActivity(myintent);

        }


        @Override
        public boolean onLongClick(View view) {
            /*PopupMenu popup = new PopupMenu(context, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.contact_popup_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId()==R.id.sms)
                        System.out.println("Send SMS");

                    return true;
                }
            });


            popup.show();*/
            return false;
        }


    }
}
