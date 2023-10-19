package com.jnu.student;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class RecycleViewBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Book> bookList;

    public RecycleViewBookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);


        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BookViewHolder bookViewHolder = (BookViewHolder) holder;
        Book book = bookList.get(position);
        bookViewHolder.bind(book);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewBookCover;
        private TextView textViewBookTitle;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBookCover = itemView.findViewById(R.id.image_view_book_cover);
            textViewBookTitle = itemView.findViewById(R.id.text_view_book_title);
        }

        public void bind(Book book) {
            imageViewBookCover.setImageResource(book.getCoverResourceId());
            textViewBookTitle.setText(book.getTitle());
        }
    }
}