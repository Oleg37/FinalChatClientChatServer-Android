package es.miapp.ad.ej5chatclient.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import es.miapp.ad.ej5chatclient.model.Repository;
import lombok.experimental.Delegate;

public class ViewModel extends AndroidViewModel {

    @Delegate(types = Repository.class)
    private final Repository repository;

    public ViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
    }
}
