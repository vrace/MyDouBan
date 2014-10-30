package com.practice.mydouban;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONObject;

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

        new AsyncTask<String, Void, List<Book>>() {
            @Override
            protected List<Book> doInBackground(String... strings) {
                JSONObject data = DataFetcher.readDataFromUrl(strings[0]);
                return new Books().getBooks(data);
            }
            @Override
            protected void onPostExecute(List<Book> books) {
                super.onPostExecute(books);
                bookList.setAdapter(new MyBookListAdapter(books, getActivity()));
            }
        }.execute("https://api.douban.com/v2/book/search?tag=Computer");

        //bookList.setAdapter(new MyBookListAdapter(books, getActivity()));
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

            final ViewHolder viewHolder;

            if (convertView == null)
            {
                View bookView = LayoutInflater.from(context).inflate(R.layout.list_item_book, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.bookCover = (ImageView) bookView.findViewById(R.id.bookCover);
                viewHolder.bookName = (TextView) bookView.findViewById(R.id.bookName);
                viewHolder.rating = (RatingBar) bookView.findViewById(R.id.rating);
                viewHolder.bookInfo = (TextView) bookView.findViewById(R.id.bookInfo);

                bookView.setTag(viewHolder);
                convertView = bookView;
            }
            else
            {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            final Book book = getItem(position);

            //viewHolder.bookCover.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_default_cover));

            viewHolder.imageUrl = book.getImage();

            ImageLoader.loadImage(book.getImage(), new ImageLoader.ImageLoaderListener() {

                @Override
                public void onImageLoaded(Bitmap bitmap, final String url) {
                    if (url.equals(viewHolder.imageUrl) && bitmap != null)
                        viewHolder.bookCover.setImageBitmap(bitmap);
                }
            });

            viewHolder.bookName.setText(book.getTitle());
            viewHolder.rating.setRating((float) (book.getRating() / 2));
            viewHolder.bookInfo.setText(book.getInformation());

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView bookCover;
        TextView bookName;
        RatingBar rating;
        TextView bookInfo;
        String imageUrl;
    }

}

