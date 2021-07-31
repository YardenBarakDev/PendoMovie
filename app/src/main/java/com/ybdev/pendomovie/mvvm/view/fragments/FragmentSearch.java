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
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.ybdev.pendomovie.R;
import com.ybdev.pendomovie.adapter.SearchAdapter;
import com.ybdev.pendomovie.mvvm.model.MovieSearchModel;
import com.ybdev.pendomovie.mvvm.view_model.SearchViewModel;
import java.util.ArrayList;

public class FragmentSearch extends Fragment {

    protected View view;
    private ProgressBar fragmentSearch_progressBar;
    private MaterialToolbar fragmentSearch_topAppBar;
    private AutoCompleteTextView fragmentSearch_autoCompleteTextView;
    private RecyclerView fragmentSearch_RecyclerView;
    private ArrayAdapter<String> possibleResults;
    private SearchAdapter searchAdapter;
    private boolean isLoading = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_search, container, false);
        findViews();
        initRecyclerView();
        initArrayAdapter();
        topBarListener();
        recyclerViewListener();
        autoCompleteTextViewListener();
        observeMatchList();
        observeRelatedMovies();
        return view;
    }

    /**
     * this method will observe the liveData from the viewModel and update the array adapter
     * according to the search results
     */
    private void observeMatchList() {
            SearchViewModel.getInstance().movieNameSearch().observe(getViewLifecycleOwner(), resultBeans -> {
                if (resultBeans != null) {
                    possibleResults.clear();
                    for (MovieSearchModel.ResultBean s : resultBeans) {
                        possibleResults.add(s.getName());
                        fragmentSearch_autoCompleteTextView.setAdapter(possibleResults);
                    }
                    isLoading = true;
                }
            });
    }

    /**
     * this method will observe the liveData from the viewModel and update the movie list
     * according to the search results
     */
    private void observeRelatedMovies(){
        SearchViewModel.getInstance().movieListViewModel.observe(getViewLifecycleOwner(), resultBeans -> {
            progressBarVisibility(false);
            if (resultBeans != null){
                SearchViewModel.getInstance().setMaxPages(resultBeans.getTotal_pages());
                searchAdapter.setMovieArray(resultBeans.getResults());
                isLoading = true;
            }
        });
    }

    /**
     * this method will initiate the recyclerView scroll listener
     * and will ask the viewModel to ask the next page from the api.
     *
     * it will only ask if we haven't reached the last page and the user
     * is at the bottom of the list
     */
    private void recyclerViewListener() {
        fragmentSearch_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager gridLayoutManager = (GridLayoutManager)fragmentSearch_RecyclerView.getLayoutManager();
                if (searchAdapter.getItemCount() > 0 && isLoading && gridLayoutManager != null && searchAdapter.getItemCount() -1 == gridLayoutManager.findLastVisibleItemPosition()){
                    isLoading = false;
                    progressBarVisibility(true);
                    SearchViewModel.getInstance().getNewData();//call the viewModel to fetch new data

                }
            }
        });
    }

    /**
     * this function will create listeners for the autoCompleteTextView
     * and will handle user interaction with the autoCompleteTextView
     */
    private void autoCompleteTextViewListener() {

        /*
        a query will be sent to the api only if the text size is bigger then 2 and divided by 2
         */
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
                }
            }

        });

        //when the user click on an item from the list
        fragmentSearch_autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            prepareNextSearch();
            SearchViewModel.getInstance().setQueryForRelated(possibleResults.getItem(position));
                SearchViewModel.getInstance().getNewData();
        });

        //when the user click on the search option
        fragmentSearch_autoCompleteTextView.setOnClickListener(v -> {
            prepareNextSearch();
            SearchViewModel.getInstance().setQueryForRelated(fragmentSearch_autoCompleteTextView.getText().toString());
            SearchViewModel.getInstance().getNewData();
        });
    }

    /**
     *shows a circular progress bar until we get respond from the api
     */
    private void progressBarVisibility(boolean visible){
        if (visible)
            fragmentSearch_progressBar.setVisibility(View.VISIBLE);
        else
            fragmentSearch_progressBar.setVisibility(View.INVISIBLE);
    }
    /**
     * clear all the data between searches
     */
    private void prepareNextSearch(){
        progressBarVisibility(true);
        fragmentSearch_RecyclerView.smoothScrollToPosition(0);
        searchAdapter.removeData();
    }

    /**
     * initiate the array adapter for the autoCompleteTextView
     */
    private void initArrayAdapter() {
        possibleResults = new ArrayAdapter<>(getContext(), R.layout.search_cell, R.id.search_movie_name);
        fragmentSearch_autoCompleteTextView.setAdapter(possibleResults);
    }

    /**
     * pop the view from the stack and show the main page
     */
    private void topBarListener() {
        fragmentSearch_topAppBar.setNavigationOnClickListener(v ->
                NavHostFragment.findNavController(FragmentSearch.this).popBackStack());
    }

    /**
     * initiate the recycler view
     */
    private void initRecyclerView() {
        int numOfColumns = 2;
        searchAdapter = new SearchAdapter(new ArrayList<>(), getContext());
        fragmentSearch_RecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        fragmentSearch_RecyclerView.setAdapter(searchAdapter);
    }

    private void findViews() {
        fragmentSearch_progressBar = view.findViewById(R.id.fragmentSearch_progressBar);
        fragmentSearch_topAppBar = view.findViewById(R.id.fragmentSearch_topAppBar);
        fragmentSearch_autoCompleteTextView = view.findViewById(R.id.fragmentSearch_autoCompleteTextView);
        fragmentSearch_RecyclerView = view.findViewById(R.id.fragmentSearch_RecyclerView);
    }
}