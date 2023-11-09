package com.jnu.student;


import android.view.ContextMenu;
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

    public static class BookViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener{
        private TextView textViewCoin;
        private TextView textViewBookTitle;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCoin = itemView.findViewById(R.id.text_view_coin);
            textViewBookTitle = itemView.findViewById(R.id.text_view_book_title);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(Book book) {
            textViewCoin.setText(book.getCoin());
            textViewBookTitle.setText(book.getTitle());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("具体操作");
            menu.add(0, 0, this.getAdapterPosition(), "增加"+this.getAdapterPosition());
            menu.add(0, 1, this.getAdapterPosition(), "修改"+this.getAdapterPosition());
            menu.add(0, 2, this.getAdapterPosition(), "删除"+this.getAdapterPosition());
        }

    }
}
