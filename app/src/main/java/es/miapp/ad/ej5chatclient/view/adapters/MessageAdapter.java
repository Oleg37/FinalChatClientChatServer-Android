package es.miapp.ad.ej5chatclient.view.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.miapp.ad.ej5chatclient.databinding.ItemMessageBinding;
import es.miapp.ad.ej5chatclient.model.pojo.Message;
import es.miapp.ad.ej5chatclient.view.fragments.Home;
import es.miapp.ad.ej5chatclient.viewmodel.ViewModel;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final ViewModel viewModel;
    private final ArrayList<Message> messageList;

    public MessageAdapter(Activity activity, ArrayList<Message> messages) {
        messageList = messages;
        viewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ViewModel.class);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.init(position);

        Log.v("XYZ", messageList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (messageList != null) {
            size = messageList.size();
        }
        return size;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public ItemMessageBinding b;

        public MessageViewHolder(@NonNull ItemMessageBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        public void init(int position) {

            if (Home.userNameHome.equals(messageList.get(position).getUserSend())) {
                b.clSend.setVisibility(View.VISIBLE);
                b.tvSendMessage.setText(messageList.get(position).getUserMessage());
            } else {
                b.clReceiver.setVisibility(View.VISIBLE);
                b.tvMessageReceiver.setText(messageList.get(position).getUserMessage());
                b.tvUserReceiver.setText(messageList.get(position).getUserSend());
//                b.button.setText(messageList.get(position).getUserSend());
            }
        }
    }
}