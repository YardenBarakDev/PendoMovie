package com.ybdev.pendomovie.mvvm.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ybdev.pendomovie.R;
import com.ybdev.pendomovie.adapter.MovieAdapter;
import com.ybdev.pendomovie.mvvm.model.MovieList;
import com.ybdev.pendomovie.mvvm.view_model.MainScreenViewModel;
import com.ybdev.pendomovie.util.MyConstants;
import java.util.ArrayList;

public class FragmentMainScreen extends Fragment {

    protected View view;
    private BottomNavigationView fragmentMainScreen_bottomNavigationView;
    private RecyclerView fragmentMainScreen_RecyclerView;
    private final ArrayList<MovieList.ResultBean> movieList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private boolean isLoading = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        findViews();
        initRecyclerView();
        MainScreenViewModel.getInstance().setCategory(MyConstants.NOW_PLAYING);
        observe();
        RecyclerViewListener();
        bottomNavigationViewListener();
        return view;
    }

    /**
     * observe data from the viewModel
     * the viewModel will fetch the data from the repository and update the view.
     */
    private void observe() {
        MainScreenViewModel.getInstance().getMovieList().observe(getViewLifecycleOwner(), resultBeans -> {
            if (resultBeans != null) {
                movieList.addAll(resultBeans);
                movieAdapter.setMovieArray(movieList);
                isLoading = true;
            }
        });
    }

    private void initRecyclerView() {
        int numOfColumns = 2;
        movieAdapter = new MovieAdapter(new ArrayList<>(), getContext());
        fragmentMainScreen_RecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        fragmentMainScreen_RecyclerView.setAdapter(movieAdapter);
    }

    private void bottomNavigationViewListener() {
        fragmentMainScreen_bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.top_rated)
                MainScreenViewModel.getInstance().setCategory(MyConstants.TOP_RATED);

            else if (item.getItemId() == R.id.popular)
                MainScreenViewModel.getInstance().setCategory(MyConstants.POPULAR);

            else if (item.getItemId() == R.id.now_playing)
                MainScreenViewModel.getInstance().setCategory(MyConstants.NOW_PLAYING);

            MainScreenViewModel.getInstance().setPage(0);
            movieList.clear();
            MainScreenViewModel.getInstance().getMovieList();
            return true;
        });

        //prevent the user to click on the same option twice
        fragmentMainScreen_bottomNavigationView.setOnItemReselectedListener(item -> {/* No op*/ });
    }

    /**
     * check if the user swipe all the way down.
     * once the user reached to the last item, new data will be fetched from the api
     * and will be presented in the RecyclerView
     */
    private void RecyclerViewListener(){
      fragmentMainScreen_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
          @Override
          public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
              super.onScrolled(recyclerView, dx, dy);
              GridLayoutManager gridLayoutManager = (GridLayoutManager)fragmentMainScreen_RecyclerView.getLayoutManager();
              if (isLoading && gridLayoutManager != null && movieAdapter.getItemCount() -1 == gridLayoutManager.findLastVisibleItemPosition()){
                  movieAdapter.removeLast();// remove the last null from the list
                  MainScreenViewModel.getInstance().getMovieList();//call the viewModel to fetch new data
                  isLoading = false;
              }
          }
      });
    }

    private void findViews() {
        fragmentMainScreen_bottomNavigationView = view.findViewById(R.id.fragmentMainScreen_bottomNavigationView);
        fragmentMainScreen_RecyclerView = view.findViewById(R.id.fragmentMainScreen_RecyclerView);
    }
}