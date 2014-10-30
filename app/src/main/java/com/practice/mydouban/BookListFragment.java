package com.practice.mydouban;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class BookListFragment
        extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private MyBookListAdapter adapter;
    private Boolean hasMoreItem = false;
    private Boolean isLoading = false;

    public BookListFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new MyBookListAdapter(getActivity());
        final ListView bookList = (ListView) rootView.findViewById(R.id.bookList);
        bookList.setAdapter(adapter);

        bookList.setOnScrollListener(new ListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0 ) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    if (lastVisibleItem == totalItemCount && !isLoading && hasMoreItem) {
                        doLoadMore();
                    }
                }
            }
        });

        doRefresh();

        return rootView;
    }

    private String makeDoubanUrl(final String tag, int count, int start)
    {
        return String.format("https://api.douban.com/v2/book/search?tag=%s&count=%d&start=%d",
                Uri.encode(tag), count, start);
    }

    private void doRefresh() {
        new AsyncTask<String, Void, Books>() {
            @Override
            protected Books doInBackground(String... strings) {
                JSONObject data = DataFetcher.readDataFromUrl(strings[0]);
                return new Books(data);
            }
            @Override
            protected void onPostExecute(Books books) {
                super.onPostExecute(books);
                adapter.clear();
                adapter.addAll(books.getBooks());
                swipeRefreshLayout.setRefreshing(false);
                hasMoreItem = books.getTotal() > adapter.getCount();
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                swipeRefreshLayout.setRefreshing(true);
            }
        }.execute(makeDoubanUrl("Computer", 20, 0));
    }

    private void doLoadMore() {
        new AsyncTask<String, Void, Books>() {
            @Override
            protected Books doInBackground(String... strings) {
                JSONObject data = DataFetcher.readDataFromUrl(strings[0]);
                return new Books(data);
            }
            @Override
            protected void onPostExecute(Books books) {
                super.onPostExecute(books);
                adapter.addAll(books.getBooks());
                isLoading = false;
                hasMoreItem = books.getTotal() > adapter.getCount();
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isLoading = true;
            }
        }.execute(makeDoubanUrl("Computer", 20, adapter.getCount()));
    }

    @Override
    public void onRefresh() {
        doRefresh();
    }

    static class MyBookListAdapter extends BaseAdapter {
        private List<Book> books;
        private Context context;

        MyBookListAdapter(Context context) {
            this.books = new LinkedList<Book>();
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

        public void addAll(List<Book> books) {
            this.books.addAll(books);
            notifyDataSetChanged();
        }

        public void clear() {
            books.clear();
            notifyDataSetChanged();
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

