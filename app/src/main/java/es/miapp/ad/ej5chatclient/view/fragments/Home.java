/*
 * Copyright (c) 2021. ArseneLupin0.
 *
 * Licensed under the GNU General Public License v3.0
 *
 * https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Permissions of this strong copyleft license are conditioned on making available complete source
 * code of licensed works and modifications, which include larger works using a licensed work,
 * under the same license. Copyright and license notices must be preserved. Contributors provide
 * an express grant of patent rights.
 */

package es.miapp.ad.ej5chatclient.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import es.miapp.ad.ej5chatclient.databinding.FragmentHomeBinding;

public class Home extends Fragment {

    public static String userNameHome;
    boolean firstTimeApp = true;
    private FragmentHomeBinding b;

    public static void getSnackTop(ViewBinding b, String message, int time) {
        Snackbar snack = Snackbar.make(b.getRoot().getRootView(), message, time);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentHomeBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        getUser();
    }

    private void getUser() {
        if (firstTimeApp) {
            b.etLoginUser.setEndIconOnClickListener(this::onClick_etLoginUser);
        }
    }

    private void onClick_etLoginUser(View v) {
        b.tietLoginUser.setEnabled(false);

        if (Objects.requireNonNull(b.etLoginUser.getEditText()).getText().toString().isEmpty()) {
            getSnackTop(b, "Ponga un nombre de usuario", Snackbar.LENGTH_SHORT);
            b.tietLoginUser.setEnabled(true);
            return;
        }

        Log.v("XYZ", b.etLoginUser.getEditText().getText().toString() + " <- Nombre");
        b.tietLoginUser.setEnabled(false);
        userNameHome = Objects.requireNonNull(b.etLoginUser.getEditText()).getText().toString();

        GlobalChat fragment = new GlobalChat();
        Bundle bundle = new Bundle();
        bundle.putString("userNameHome", "Jusep");
        fragment.setArguments(bundle);

//        b.etLoginUser.setEnabled(false);
        firstTimeApp = false;
    }
}