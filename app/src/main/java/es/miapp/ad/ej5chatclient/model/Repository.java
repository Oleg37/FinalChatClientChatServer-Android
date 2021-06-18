package es.miapp.ad.ej5chatclient.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.miapp.ad.ej5chatclient.model.pojo.Message;
import es.miapp.ad.ej5chatclient.model.retrofit.MessageClient;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Getter
@Setter
public class Repository {

    private final String RETROFIT_URL = "https://informatica.ieszaidinvergeles.org:10026/laraveles/ej5ChatServerChatClient/public/api/";
    private Context context;

    private Message message;

    private MessageClient messageClient;

    private Retrofit retrofit;

    private Socket client;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private String userNameCurrent;

    private MutableLiveData<List<Message>> listMutableMessages = new MutableLiveData<>();
    private MutableLiveData<List<Message>> listMutablePrivateMessages = new MutableLiveData<>();

    public Repository(Context context) {
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(RETROFIT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        messageClient = retrofit.create(MessageClient.class);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Message -> Insert - Update - Delete
    ///////////////////////////////////////////////////////////////////////////

    public void insertMessage(Message message) {
        Call<Message> request = messageClient.insertMessage(message);

        request.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.v("XYZ", "Agregado con éxito");
                getAllMessages();
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.v("XYZ - Insert", t.getMessage() + request);
                noInternet(t);
            }
        });
    }

    public void updateCallF(long id, Message message) {
        Call<Boolean> request = messageClient.updateMessage(id, message);

        request.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                Log.v("XYZ", "Modificado con éxito");
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.v("XYZ" + call.getClass(), t.getMessage());
                noInternet(t);
            }
        });
    }

    public void deleteMessage(long id) {
        Call<Message> request = messageClient.deleteMessageById(id);

        request.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.v("XYZ", "Eliminado con éxito");
                getAllMessages();
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.v("XYZ" + call.getClass(), t.getMessage());
                noInternet(t);
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // List Operations
    ///////////////////////////////////////////////////////////////////////////

    public void getAllMessages() {
        Call<ArrayList<Message>> call = messageClient.getMessages();

        call.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Message>> call, @NonNull Response<ArrayList<Message>> response) {
                listMutableMessages.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Message>> call, @NonNull Throwable t) {
                noInternet(t);
            }
        });
    }

    public void deleteAll() {
        Call<ArrayList<Message>> request = messageClient.deleteAll();

        request.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Message>> call, @NonNull Response<ArrayList<Message>> response) {
                Log.v("XYZ", "¡Todas las conversaciones eliminadas!");
                getAllMessages();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Message>> call, @NonNull Throwable t) {
                Log.v("XYZ" + call.getClass(), t.getMessage());
                noInternet(t);
            }
        });
    }

    public void getPrivateMessage(String userSend, String userReceiver) {
        Call<ArrayList<Message>> request = messageClient.getPrivateMessage(userSend, userReceiver);

        request.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Message>> call, @NonNull Response<ArrayList<Message>> response) {
                Log.v("XYZ", "¡Mensajes privados obtenidos!");
                getPrivateMessage(userSend, userReceiver);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Message>> call, @NonNull Throwable t) {
                Log.v("XYZ" + call.getClass(), t.getMessage());
                noInternet(t);
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // Extra Operations
    ///////////////////////////////////////////////////////////////////////////

    private void noInternet(@NotNull Throwable t) {
        if (Objects.requireNonNull(t.getMessage()).contains("Unable to resolve host")) {
            Toast.makeText(context, "No hay conexión a internet!", Toast.LENGTH_SHORT).show();
        }
    }
}
