package com.ybdev.pendomovie.mvvm.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
        topBarListener();
        observe();
        return view;
    }

    private void observe() {
        FavoriteViewModel.getInstance().favoriteMoviesList.observe(getViewLifecycleOwner(), singleMovieModels -> {
            if (singleMovieModels != null){
                favoriteAdapter.clearArray();
                favoriteAdapter.setMovieArray(singleMovieModels);
            }
        });
    }

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