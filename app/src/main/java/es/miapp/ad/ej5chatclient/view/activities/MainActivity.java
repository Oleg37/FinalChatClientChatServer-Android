package es.miapp.ad.ej5chatclient.view.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import es.miapp.ad.ej5chatclient.R;
import es.miapp.ad.ej5chatclient.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        init();
    }

    private void init() {
        initDrawer();
    }

    private void initDrawer() {
        setSupportActionBar(b.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(b.drawerLayout).build();

        NavigationUI.setupWithNavController(b.toolbar, navController, appBarConfiguration);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(b.navigationViewMain, navController);

        toolbarCollab(navController);
    }

    private void toolbarCollab(NavController navController) {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            b.appBarLayout.setExpanded(false, true);
            b.appBarLayout.setNestedScrollingEnabled(false);
        });
    }
}