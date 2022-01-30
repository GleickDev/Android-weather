package com.gleickapps.weather.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Classe auxiliar para construir uma janela popup personalizada
 */
public class MyPopup {

    protected MainActivity mainActivity;
    protected int resource;
    protected View popupView;
    protected PopupWindow popupWindow;
    protected View myView;

    public MyPopup(MainActivity mainActivity, int resource) {
        this.mainActivity = mainActivity;
        this.resource = resource;
    }

    /**
     * Parâmetros de layout
     * @param view View
     */
    @SuppressLint("InflateParams")
    protected void showPopupWindow(View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        popupView = inflater.inflate(resource, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        dimBehind(popupWindow);
        myView = view;

        initialization();
    }

    /**
     * Inicialização dos elementos
     */
    protected void initialization() {
    }

    /**
     * Escurece a área abaixo do popup
     * @param popupWindow
     */
    protected void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.4f;
        wm.updateViewLayout(container, p);
    }
}
