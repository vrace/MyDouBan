package com.practice.mydouban;

import android.app.Fragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class BookListFragment extends Fragment {


    public BookListFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);
        final List<Book> books = new Books().getBooks(DataFetcher.readDataFromFile(getActivity()));

        final ListView bookList = (ListView) rootView.findViewById(R.id.bookList);

        bookList.setAdapter(new MyBookListAdapter(books, getActivity()));
        return rootView;
    }

    static class MyBookListAdapter extends BaseAdapter {
        private List<Book> books;
        private Context context;

        MyBookListAdapter(List<Book> books, Context context) {
            this.books = books;

            this.context = context;
        }

        @Override
        public int getCount() {
            return books.size();

        }

        @Override
        public Book getItem(int position) {
            return books.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View bookView = LayoutInflater.from(context).inflate(R.layout.list_item_book, parent, false);
            final ImageView bookCover = (ImageView) bookView.findViewById(R.id.bookCover);
            final TextView bookName = (TextView) bookView.findViewById(R.id.bookName);
            final RatingBar rating = (RatingBar) bookView.findViewById(R.id.rating);
            final TextView bookInfo = (TextView) bookView.findViewById(R.id.bookInfo);

            final Book book = getItem(position);

            bookCover.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_default_cover));
            bookName.setText(book.getTitle());
            rating.setRating((float) (book.getRating() / 2));
            bookInfo.setText(book.getInformation());
            return bookView;
        }
    }

}

