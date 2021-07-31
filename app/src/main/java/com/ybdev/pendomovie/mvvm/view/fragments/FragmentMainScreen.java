package com.ybdev.pendomovie.mvvm.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ybdev.pendomovie.R;
import com.ybdev.pendomovie.adapter.MovieAdapter;
import com.ybdev.pendomovie.mvvm.view_model.MainScreenViewModel;
import com.ybdev.pendomovie.util.MyConstants;
import java.util.ArrayList;

public class FragmentMainScreen extends Fragment {

    protected View view;
    private MaterialToolbar fragmentMainScreen_topAppBar;
    private BottomNavigationView fragmentMainScreen_bottomNavigationView;
    private RecyclerView fragmentMainScreen_RecyclerView;
    private MovieAdapter movieAdapter;
    private boolean isLoading = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        findViews();
        initRecyclerView();
        updateMovieList();
        topBarListener();
        recyclerViewListener();
        bottomNavigationViewListener();
        observe();

        return view;
    }

    private void updateMovieList() {
        movieAdapter.setMovieArray(MainScreenViewModel.getInstance().getFetchedMovies());
    }

    private void topBarListener() {
        fragmentMainScreen_topAppBar.setNavigationOnClickListener(v ->
                NavHostFragment.findNavController(FragmentMainScreen.this).
                        navigate(R.id.action_fragmentMainScreen_to_fragmentSearch));
    }

    /**
     * observe data from the viewModel
     * the viewModel will fetch the data from the repository and update the view.
     */
    private void observe() {
        MainScreenViewModel.getInstance().movieListViewModel.observe(getViewLifecycleOwner(), resultBeans -> {
            if (resultBeans != null) {
                if (MainScreenViewModel.getInstance().getFetcehdSize() > movieAdapter.getItemCount()){
                    MainScreenViewModel.getInstance().setMaxPages(resultBeans.getTotal_pages());
                    movieAdapter.setMovieArray(resultBeans.getResults());//
                    Log.d("kkkk", "fetched size = " +MainScreenViewModel.getInstance().getFetcehdSize());
                    Log.d("kkkk", "adapter array size = " + movieAdapter.getItemCount());
                    isLoading = true;
                }
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

            fragmentMainScreen_RecyclerView.smoothScrollToPosition(0);// scroll all the way up
            movieAdapter.clearArray();
            MainScreenViewModel.getInstance().getNewData();//fetch new movies
            return true;
        });

        //prevent the user from clicking on the same option twice which reload the data again
        fragmentMainScreen_bottomNavigationView.setOnItemReselectedListener(item -> {/* No op*/ });
    }

    /**
     * check if the user swipe all the way down.
     * once the user reached to the last item, new data will be fetched from the api
     * and will be presented in the RecyclerView
     */
    private void recyclerViewListener(){
      fragmentMainScreen_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
          @Override
          public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
              super.onScrolled(recyclerView, dx, dy);
              GridLayoutManager gridLayoutManager = (GridLayoutManager)fragmentMainScreen_RecyclerView.getLayoutManager();
              if (movieAdapter.getItemCount() > 0 && isLoading && gridLayoutManager != null && movieAdapter.getItemCount() -1 == gridLayoutManager.findLastVisibleItemPosition()){
                  MainScreenViewModel.getInstance().getNewData();//call the viewModel to fetch new data
                  isLoading = false;
              }
          }
      });
    }

    private void findViews() {
        fragmentMainScreen_topAppBar = view.findViewById(R.id.fragmentMainScreen_topAppBar);
        fragmentMainScreen_bottomNavigationView = view.findViewById(R.id.fragmentMainScreen_bottomNavigationView);
        fragmentMainScreen_RecyclerView = view.findViewById(R.id.fragmentMainScreen_RecyclerView);
    }

}