package com.example.abhishek.bookshareapp.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abhishek.bookshareapp.R;
import com.example.abhishek.bookshareapp.api.BooksAPI;
import com.example.abhishek.bookshareapp.api.NetworkingFactory;
import com.example.abhishek.bookshareapp.api.models.Book;
import com.example.abhishek.bookshareapp.api.models.GoodreadsResponse;
import com.example.abhishek.bookshareapp.api.models.Search;
import com.example.abhishek.bookshareapp.ui.adapter.BooksAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListFragment extends Fragment {

    private RecyclerView resultsList;
    List<Book> bookList = new ArrayList<>();
    BooksAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Search sr;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_list_fragment, container, false);

        resultsList = (RecyclerView) view.findViewById(R.id.results_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        resultsList.setLayoutManager(mLayoutManager);
        context = getActivity();

        adapter = new BooksAdapter(getActivity(), bookList, new BooksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Toast.makeText(context, book.getBookDetails().getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        resultsList.setAdapter(adapter);

        return view;
    }

    public void getBooks(String query, String field, String key) {

        Toast.makeText(getActivity(), "getbooks", Toast.LENGTH_SHORT).show();

        BooksAPI api = NetworkingFactory.getGRInstance().getBooksApi();
        Call<GoodreadsResponse> call = api.getBooks(query, field, key);
        call.enqueue(new Callback<GoodreadsResponse>() {
            @Override
            public void onResponse(Call<GoodreadsResponse> call, Response<GoodreadsResponse> response) {
                if (response.body() != null) {
                    Log.d("searchresp", response.toString());
                    sr = response.body().getSearch();
                    bookList.clear();
                    bookList.addAll(sr.getBooks());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GoodreadsResponse> call, Throwable t) {
                Log.d("searchresp", "searchOnFail " + t.toString());
            }
        });

    }

}