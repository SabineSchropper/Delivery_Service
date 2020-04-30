package com.company.controller;

import com.company.model.model.Menu;
import com.company.model.repository.MenuRepository;
import com.company.view.MenuView;

import java.util.ArrayList;

public class MenuController {
    MenuRepository menuRepository = new MenuRepository();
    MenuView menuView = new MenuView();

    public void showMenuCard() {
        menuRepository.fetchMenus();
        menuRepository.fetchIngredients();
        ArrayList<Menu> menus = menuRepository.getMenus();
        menuView.viewMenu(menus);
    }
    public boolean askIfWantToSeeDetailsAndShowThem(){
        boolean wantsToSeeDetails = true;
        int menuNumber = menuView.askIfwantToSeeDetails();
        if(menuNumber != 0){
            showIngredients(menuNumber);
        }
        else{
            wantsToSeeDetails = false;
        }
        return wantsToSeeDetails;
    }
    public Menu showIngredients(int menuNumber){
        Menu menu = menuRepository.searchMenuInArrayList(menuNumber);
        menuView.viewIngredients(menu);
        return menu;
    }

}
