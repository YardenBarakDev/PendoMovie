package com.ybdev.pendomovie.mvvm.view.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.ybdev.pendomovie.R;
import com.ybdev.pendomovie.adapter.SearchAdapter;
import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.mvvm.model.MovieSearchModel;
import com.ybdev.pendomovie.mvvm.view_model.SearchViewModel;
import java.util.ArrayList;

public class FragmentSearch extends Fragment {

    protected View view;
    private MaterialToolbar fragmentSearch_topAppBar;
    private AutoCompleteTextView fragmentSearch_autoCompleteTextView;
    private RecyclerView fragmentSearch_RecyclerView;
    private ArrayAdapter<String> possibleResults;
    private SearchAdapter searchAdapter;
    private final ArrayList<MovieList.ResultBean> movieList = new ArrayList<>();
    private boolean searchData = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_search, container, false);
        findViews();
        initRecyclerView();
        initArrayAdapter();
        topBarListener();
        autoCompleteTextViewListener();
        observeMatchList();
        observeRelatedMovies();
        return view;
    }

    private void observeMatchList() {
            SearchViewModel.getInstance().movieNameSearch().observe(getViewLifecycleOwner(), resultBeans -> {
                if (resultBeans != null) {
                    possibleResults.clear();
                    for (MovieSearchModel.ResultBean s : resultBeans) {
                        possibleResults.add(s.getName());
                        fragmentSearch_autoCompleteTextView.setAdapter(possibleResults);
                    }
                }
            });
    }

    private void observeRelatedMovies(){
        SearchViewModel.getInstance().getRelatedMovies().observe(getViewLifecycleOwner(), resultBeans -> {
            if (resultBeans != null){
                movieList.clear();
                movieList.addAll(resultBeans);
                for (int i = 0; i < movieList.size(); i++) {
                    Log.d("kkkk", movieList.get(i).getTitle());
                }
                searchAdapter.setMovieArray(movieList);
            }
        });
    }
    private void autoCompleteTextViewListener() {
        fragmentSearch_autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1 && s.length()%2 == 0){
                    SearchViewModel.getInstance().setQuery(s.toString());
                    SearchViewModel.getInstance().movieNameSearch();
                    searchData = true;
                }
                else
                    searchData = false;
            }

        });

        fragmentSearch_autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                SearchViewModel.getInstance().setQueryForRelated(possibleResults.getItem(position));
                SearchViewModel.getInstance().getRelatedMovies();
        });
    }

    private void initArrayAdapter() {
        possibleResults = new ArrayAdapter<>(getContext(), R.layout.search_cell, R.id.search_movie_name);
        fragmentSearch_autoCompleteTextView.setAdapter(possibleResults);
    }

    /**
     * pop the view from the stack and show the main page
     */
    private void topBarListener() {
        fragmentSearch_topAppBar.setNavigationOnClickListener(v ->{
            NavHostFragment.findNavController(FragmentSearch.this).popBackStack();
            possibleResults.clear();
            searchAdapter.setMovieArray(new ArrayList<>());
            fragmentSearch_RecyclerView.smoothScrollToPosition(0);
        });
    }

    private void initRecyclerView() {
        int numOfColumns = 2;
        searchAdapter = new SearchAdapter(new ArrayList<>(), getContext());
        fragmentSearch_RecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        fragmentSearch_RecyclerView.setAdapter(searchAdapter);
    }

    private void findViews() {
        fragmentSearch_topAppBar = view.findViewById(R.id.fragmentSearch_topAppBar);
        fragmentSearch_autoCompleteTextView = view.findViewById(R.id.fragmentSearch_autoCompleteTextView);
        fragmentSearch_RecyclerView = view.findViewById(R.id.fragmentSearch_RecyclerView);

    }
}