package com.example.extrememe;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extrememe.adapters.MyMemesAdapter;
import com.example.extrememe.model.Category;
import com.example.extrememe.model.Meme;
import com.example.extrememe.services.CategoryService;
import com.example.extrememe.services.MemesService;
import com.example.extrememe.utils.LayoutUnitUtils;

import java.util.ArrayList;
import java.util.List;

public class MainFeedFragment extends Fragment {
    private final int SELECTED_CATEGORY = Color.MAGENTA;
    private final int UNSELECTED_CATEGORY = Color.GRAY;

    private List<Meme> allMemes = new ArrayList<>();
    private List<Meme> filteredMemes = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<String> selectedCategories = new ArrayList<>();
    private MyMemesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_feed, container, false);


        RecyclerView memesRv = view.findViewById(R.id.allMemes_rv);
        memesRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        memesRv.setLayoutManager(layoutManager);

        adapter = new MyMemesAdapter(getLayoutInflater());
        memesRv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        new CategoryService().getMemeCategories(categories -> {
            this.categories = categories;

            for (Category category : categories) {
                this.addCategoryButtonToView(category);
            }
            this.initRandomButton();
        });

        new MemesService().getAllMemes(memes -> {
            allMemes = memes;
            this.filterMemes();
        });
    }

    private void initRandomButton() {
        View v = getView().findViewById(R.id.random_button);
        ((Button)v).setOnClickListener(randomButton -> {
            for(String selectedCategoryId : selectedCategories) {
                getView().findViewById(selectedCategoryId.hashCode()).setBackgroundColor(UNSELECTED_CATEGORY);
            }
            this.selectCategory((Button)randomButton);
            this.filterMemes();
        });
    }

    private void filterMemes() {
        this.filteredMemes.clear();

        if(selectedCategories != null && selectedCategories.size() > 0) {
            for (Meme meme : allMemes) {
                if(meme.getCategories() != null) {
                    for (String categoryId : meme.getCategories()) {
                        for (String selectedCategoryId : selectedCategories) {
                            if (selectedCategoryId.equals(categoryId)) {
                                this.filteredMemes.add(meme);
                            }
                        }
                    }
                }
            }
        } else {
            this.filteredMemes = (List<Meme>) ((ArrayList<Meme>)allMemes).clone();
        }

        this.adapter.data = filteredMemes;
        adapter.notifyDataSetChanged();
    }

    private void addCategoryButtonToView(Category category) {
        final int categoryButtonWidth = 110;
        final int categoryButtonHeight = 50;
        final int margin = 5;

        Button myButton = new Button(this.getContext());
        myButton.setId(category.getId().hashCode());
        myButton.setText(category.getName());
        myButton.setWidth(LayoutUnitUtils.getInstance()
                .convertPixelToDpUnit(categoryButtonWidth, getResources().getDisplayMetrics()));
        myButton.setHeight(LayoutUnitUtils.getInstance()
                .convertPixelToDpUnit(categoryButtonHeight, getResources().getDisplayMetrics()));

        LinearLayout ll = (LinearLayout) getView().findViewById(R.id.categoriesPanel);
        int marginInDP = LayoutUnitUtils.getInstance()
                .convertPixelToDpUnit(margin, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(marginInDP, marginInDP, marginInDP, marginInDP);

        myButton.setOnClickListener(onClickCategory());

        myButton.setTag(category.getId());
        ll.addView(myButton, lp);
    }

    private View.OnClickListener onClickCategory() {
        return v -> {
            this.selectCategory((Button)v);
            this.filterMemes();
        };
    }

    private void selectCategory(Button button) {
        if(button.getText().equals("Random")) {
            button.setBackgroundColor(SELECTED_CATEGORY);
            this.selectedCategories.clear();
            return;
        }

        for (String selectedCategoryId : selectedCategories) {
            if(button.getTag().equals(selectedCategoryId)) {
                button.setBackgroundColor(UNSELECTED_CATEGORY);
                selectedCategories.remove(selectedCategoryId);
                return;
            }
        }

        button.setBackgroundColor(SELECTED_CATEGORY);
        unselectRandomButton(UNSELECTED_CATEGORY);
        selectedCategories.add(button.getTag().toString());
    }

    private void unselectRandomButton(int unselectedButtonColor) {
        View v = getView().findViewById(R.id.random_button);
        ((Button)v).setBackgroundColor(unselectedButtonColor);
    }
}