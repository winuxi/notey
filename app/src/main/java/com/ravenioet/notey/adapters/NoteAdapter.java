package com.ravenioet.notey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.ravenioet.notey.R;
import com.ravenioet.notey.models.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private List<Note> books = new ArrayList<>();
    public OnItemClickListener listener;
    public Context context;
    int viewType;
    int listType;
    public NoteAdapter(Context context, int viewType, int listType){
        this.context = context;
        this.viewType = viewType;
        this.listType = listType;
    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if(this.viewType == 1){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_book_g,parent,false);
        }else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_book_h,parent,false);
        }
        return new NoteHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = books.get(position);
        holder.bookTitle.setText(note.getTitle());
        holder.bookDesc.setText(note.getBody());
        if(listType != 1){
            holder.locker.setVisibility(View.GONE);
        }
        if (note.getFlag() == 1) {
            holder.locker.setImageResource(R.drawable.ic_baseline_lock_open_24);
        }
        else if (note.getFlag() == 2) {
            holder.locker.setImageResource(R.drawable.ic_baseline_lock_24);
        }else {
            holder.locker.setVisibility(View.GONE);
        }
        /*RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_baseline_menu_book_24)
                .error(R.drawable.ic_baseline_menu_book_24)*//*.circleCrop()*//*;
        Glide.with(context).load(UserManager.loadIp(context)+book.getCover_image()).apply(options).into(holder.bookPic);
        RequestOptions option = new RequestOptions()
                .placeholder(R.drawable.ic_baseline_person_pin_24)
                .error(R.drawable.ic_baseline_person_pin_24)
                .circleCrop();
        Glide.with(context).load(UserManager.loadIp(context)+book.getPublisher_photo()).apply(option).into(holder.pubPic);
        */

    }
    private static boolean isNonNullAndChecked(@Nullable String uri) {
        return uri != null;
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
    public void setNote(List<Note> notifs){
        this.books = notifs;
        notifyDataSetChanged();
    }
    public class NoteHolder extends RecyclerView.ViewHolder {
        private TextView bookTitle, bookDesc;
        private ImageView bookPic, locker,pubPic;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookDesc = itemView.findViewById(R.id.book_desc);
            bookPic = itemView.findViewById(R.id.book_pic);
            locker = itemView.findViewById(R.id.lock);
            pubPic = itemView.findViewById(R.id.puber);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION){
                    listener.onItemClick(books.get(position));
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(Note book);
    }

    public void onItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
