package es.miapp.ad.ej5chatclient.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import es.miapp.ad.ej5chatclient.databinding.FragmentGlobalChatBinding;
import es.miapp.ad.ej5chatclient.model.pojo.Message;
import es.miapp.ad.ej5chatclient.util.AdminThread;
import es.miapp.ad.ej5chatclient.util.DateTransform;
import es.miapp.ad.ej5chatclient.view.adapters.MessageAdapter;
import es.miapp.ad.ej5chatclient.viewmodel.ViewModel;

public class GlobalChat extends Fragment {

    private final ArrayList<Message> messageList = new ArrayList<>();
    private FragmentGlobalChatBinding b;
    private Socket client;
    private DataInputStream flujoE;
    private DataOutputStream flujoS;
    private String userNameHome;
    private ViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentGlobalChatBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            userNameHome = bundle.getString("userNameHome");
        }

        userNameHome = userNameHome == null ? "OlegAndroid" : userNameHome;
        userNameHome = userNameHome.equals("OlegAndroid") ? Home.userNameHome : userNameHome;

        if (userNameHome == null) {
            Home.getSnackTop(b, "Es necesario tener un nombre", Snackbar.LENGTH_LONG);
            return;
        }

        initSession("10.0.2.2", 5000);

        init();
    }

    private void init() {
        MessageAdapter adapter = new MessageAdapter(getActivity(), messageList);
        b.rvMessages.setAdapter(adapter);
        b.rvMessages.setLayoutManager(new LinearLayoutManager(getActivity()));


        viewModel.getAllMessages();
        /*viewModel.getListMutableMessages().observe(requireActivity(), messages -> {
            messageList.clear();
            messageList.addAll(messages);
            messageList.forEach(message -> Log.v("XYZ", message.toString()));
            adapter.notifyDataSetChanged();
            if (messages.size() > 1) {
                b.rvMessages.smoothScrollToPosition(messages.size() - 1);
            }
        });*/

        viewModel.getListMutableMessages().observe(requireActivity(), messages -> {
            b.tvSetText.setText("");
            for (Message message : messages) {
                newMessage(message.getUserSend() + ": " + message.getUserMessage());
            }
        });

        b.btSendGlobal.setOnClickListener(this::onClickSend);
    }

    public void initSession(String ip, int port) {
        AdminThread.threadExecutorPool.execute(() -> {
            try {
                client = new Socket(ip, port);
                flujoE = new DataInputStream(client.getInputStream());
                flujoS = new DataOutputStream(client.getOutputStream());

                String msg;
                while (true) {
                    try {
                        msg = flujoE.readUTF();
                        msg = msg.substring(userNameHome.length() + 1);

                        if (msg.contains("Correctamente")) {
                            Home.getSnackTop(b, userNameHome + ", has entrado a la sala correctamente.", Snackbar.LENGTH_LONG);
                        } else {
                            viewModel.insertMessage(new Message(msg, userNameHome, null, DateTransform.dateSQLHHMMSS(new Date().getTime())));
                        }
                    } catch (IOException ex) {
                        Log.v("XYZ", "Ha ocurrido un error al leer los mensajes");
                        return;
                    }
                }

            } catch (IOException ex) {
                Log.v("XYZ", "Ha ocurrido un error al conectar con el server: " + ex.toString());
            }
        });
    }

    private void sendMsg(String message) {
        if (message.isEmpty()) {
            Toast.makeText(getContext(), "¡Ponga un mensjae!", Toast.LENGTH_SHORT).show();
            return;
        }

        AdminThread.threadExecutorPool.execute(() -> {
            try {
                Log.v("XYZ", message);
                flujoS.flush();
                flujoS.writeUTF(message);
            } catch (IOException ex) {
                Log.v("XYZ", "Ha ocurrido un error al enviar el mensaje");
            }
        });
    }

    private void onClickSend(View v) {
        sendMsg(userNameHome + ": " + b.etSendMessageGlobal.getText().toString());
        b.etSendMessageGlobal.setText("");
    }

    private void newMessage(String text) {
        requireActivity().runOnUiThread(() -> b.tvSetText.append("\n " + text));
    }
}