package com.example.usuarios_del_mundo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.usuarios_del_mundo.Models.User;
import com.example.usuarios_del_mundo.R;
import com.example.usuarios_del_mundo.UserDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Usa los métodos correctos del modelo de datos
        holder.nameTextView.setText(user.getName().getFirst() + " " + user.getName().getLast());
        holder.emailTextView.setText(user.getEmail());
        holder.countryTextView.setText(user.getLocation().getCountry());

        // Cargar imagen de perfil
        Picasso.get().load(user.getPicture().getLarge()).into(holder.imageView);

        // Cuando el usuario haga clic en el ítem, abrir la actividad de detalles
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra("firstName", user.getName().getFirst());
                intent.putExtra("lastName", user.getName().getLast());
                intent.putExtra("city", user.getLocation().getCity());
                intent.putExtra("country", user.getLocation().getCountry());
                intent.putExtra("latitude", Double.parseDouble(user.getLocation().getCoordinates().getLatitude()));
                intent.putExtra("longitude", Double.parseDouble(user.getLocation().getCoordinates().getLongitude()));
                intent.putExtra("imageUrl", user.getPicture().getLarge());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView, emailTextView, countryTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profileImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            countryTextView = itemView.findViewById(R.id.locationTextView);
        }
    }
}
