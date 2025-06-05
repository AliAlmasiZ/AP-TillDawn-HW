package io.github.AliAlmasiZ.tillDawn.controllers;

import io.github.AliAlmasiZ.tillDawn.Main;
import io.github.AliAlmasiZ.tillDawn.views.MainMenuView;

public class MainMenuController {
    private MainMenuView view;

    public void setView(MainMenuView view) {
        this.view = view;
    }

//    public void handleMainMenuButtons() {
//        if (view != null)
//            if(view.getPlayButton().isChecked()) {
//                Main.main.getScreen().dispose();
//            }
//    }
}
