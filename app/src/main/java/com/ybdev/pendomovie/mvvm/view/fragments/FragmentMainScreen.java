package com.ybdev.pendomovie.mvvm.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
    private ProgressBar fragmentMainScreen_progressBar;
    private RecyclerView fragmentMainScreen_RecyclerView;
    private MovieAdapter movieAdapter;
    private boolean isLoading = true; //monitor when the application waits for a respond from the api

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

    /**
     * observe data from the viewModel
     * the viewModel fetches the data from the repository and update the view.
     */
    private void observe() {
        MainScreenViewModel.getInstance().movieListViewModel.observe(getViewLifecycleOwner(), resultBeans -> {
            progressBarVisibility(false);
            if (resultBeans != null) {
                if (MainScreenViewModel.getInstance().getFetchedSize() > movieAdapter.getItemCount()){
                    MainScreenViewModel.getInstance().setMaxPages(resultBeans.getTotal_pages());
                    movieAdapter.updateMovieArray(resultBeans.getResults());//
                    isLoading = true;
                }
            }
        });
    }

    /**
     * bring back the movie list from the viewModel in case the fragment was in the background
     */
    private void updateMovieList() {
        movieAdapter.updateMovieArray(MainScreenViewModel.getInstance().getFetchedMovies());
    }

    /************************** Listeners **************************/

    private void topBarListener() {

        //show the search fragment once the user clicks on the search icon
        fragmentMainScreen_topAppBar.setNavigationOnClickListener(v ->
                NavHostFragment.findNavController(FragmentMainScreen.this).
                        navigate(R.id.action_fragmentMainScreen_to_fragmentSearch));

        //show the favorites fragment once the user clicks on the favorite icon
        fragmentMainScreen_topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.moveToFavorite) {
                NavHostFragment.findNavController(FragmentMainScreen.this).
                        navigate(R.id.action_fragmentMainScreen_to_fragmentFavorite);
            }
            return true;
        });
    }

    /**
     * listen to which option is selected in the bottomNavigationView and tells the view model to fetch relevant data
     */
    private void bottomNavigationViewListener() {
        //set the middle icon (Top Rated) as selected
        fragmentMainScreen_bottomNavigationView.setSelectedItemId( R.id.top_rated);

        //replace the movie list to match the the item description
        fragmentMainScreen_bottomNavigationView.setOnItemSelectedListener(item -> {
            progressBarVisibility(true);
            if (item.getItemId() == R.id.top_rated)
                MainScreenViewModel.getInstance().setCategory(MyConstants.TOP_RATED);

            else if (item.getItemId() == R.id.popular)
                MainScreenViewModel.getInstance().setCategory(MyConstants.POPULAR);

            else if (item.getItemId() == R.id.now_playing)
                MainScreenViewModel.getInstance().setCategory(MyConstants.NOW_PLAYING);

            prepareViewForNewData();
            MainScreenViewModel.getInstance().getNewData();//fetch new movies
            return true;
        });

        //prevent the user from clicking on the same option twice and reload the same data
        fragmentMainScreen_bottomNavigationView.setOnItemReselectedListener(item -> {/* No op*/ });
    }

    /**
     *return to the top of the recycler view and clear the movie array
     */
    private void prepareViewForNewData(){
        fragmentMainScreen_RecyclerView.smoothScrollToPosition(0);// scroll all the way up
        movieAdapter.clearArray();
    }

    /**
     *shows a circular progress bar while fetching data from the api
     */
    private void progressBarVisibility(boolean visible){
        if (visible && !MainScreenViewModel.getInstance().isLastPage())
            fragmentMainScreen_progressBar.setVisibility(View.VISIBLE);
        else
            fragmentMainScreen_progressBar.setVisibility(View.INVISIBLE);
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
                  progressBarVisibility(true);
                  MainScreenViewModel.getInstance().getNewData();//call the viewModel to fetch new data
                  isLoading = false;
              }
          }
      });
    }


    /************************** init **************************/
    private void initRecyclerView() {
        int numOfColumns = 2;
        movieAdapter = new MovieAdapter(new ArrayList<>(), getContext());
        fragmentMainScreen_RecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        fragmentMainScreen_RecyclerView.setAdapter(movieAdapter);
    }

    private void findViews() {
        fragmentMainScreen_topAppBar = view.findViewById(R.id.fragmentMainScreen_topAppBar);
        fragmentMainScreen_bottomNavigationView = view.findViewById(R.id.fragmentMainScreen_bottomNavigationView);
        fragmentMainScreen_RecyclerView = view.findViewById(R.id.fragmentMainScreen_RecyclerView);
        fragmentMainScreen_progressBar = view.findViewById(R.id.fragmentMainScreen_progressBar);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentMainScreen_bottomNavigationView.setSelectedItemId( R.id.top_rated);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainScreenViewModel.getInstance().setCategory(MyConstants.TOP_RATED);
        MainScreenViewModel.getInstance().getNewData();
    }
}