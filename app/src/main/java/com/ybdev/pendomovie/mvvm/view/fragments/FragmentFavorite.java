package com.ybdev.pendomovie.mvvm.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.ybdev.pendomovie.R;
import com.ybdev.pendomovie.adapter.FavoriteAdapter;
import com.ybdev.pendomovie.mvvm.view_model.FavoriteViewModel;
import java.util.ArrayList;

public class FragmentFavorite extends Fragment {

   protected View view;
   private FavoriteAdapter favoriteAdapter;
   private RecyclerView fragmentFavorite_RecyclerView;
   private MaterialToolbar fragmentFavorite_topAppBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_favorite, container, false);

        findViews();
        initRecyclerView();
        recyclerViewListener();
        topBarListener();
        observe();
        return view;
    }

    /**
     * attach ItemTouchHelper to the RecyclerView and listen to users swipe interactions
     */
    private void recyclerViewListener() {

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override//no op
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) { return false; }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                showDialog(viewHolder);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(fragmentFavorite_RecyclerView);
    }

    private void showDialog(RecyclerView.ViewHolder viewHolder) {
        String message = "Are you sure you want to delete '"
                + favoriteAdapter.getMovie(viewHolder.getAdapterPosition()).getTitle() +
                "' from you favorites list?";

        new AlertDialog.Builder(getContext())
                .setTitle("Delete")
                .setMessage(message)
                .setIcon(R.drawable.app_icon)
                .setPositiveButton("Confirm", (dialog, which) ->
                {
                    FavoriteViewModel.getInstance().deleteMovieFromFavorites(favoriteAdapter.getMovie(viewHolder.getAdapterPosition()));
                    favoriteAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                })
                .setNegativeButton("Cancel", (dialog, which) -> favoriteAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                .show();
    }


    /**
     * observe data from Room db using the viewModel
     */
    private void observe() {
        FavoriteViewModel.getInstance().favoriteMoviesList.observe(getViewLifecycleOwner(), singleMovieModels -> {
            if (singleMovieModels != null){
                favoriteAdapter.clearArray();
                favoriteAdapter.updateMovieArray(singleMovieModels);
            }
        });
    }

    /**
     * pop the view from the stack and show the main page
     */
    private void topBarListener() {
        fragmentFavorite_topAppBar.setNavigationOnClickListener(v ->
                NavHostFragment.findNavController(FragmentFavorite.this).popBackStack());
    }

    private void initRecyclerView() {
        int numOfColumns = 2;
        favoriteAdapter = new FavoriteAdapter(new ArrayList<>(), getContext());
        fragmentFavorite_RecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        fragmentFavorite_RecyclerView.setAdapter(favoriteAdapter);
    }

    private void findViews() {
        fragmentFavorite_RecyclerView = view.findViewById(R.id.fragmentFavorite_RecyclerView);
        fragmentFavorite_topAppBar = view.findViewById(R.id.fragmentFavorite_topAppBar);
    }
}