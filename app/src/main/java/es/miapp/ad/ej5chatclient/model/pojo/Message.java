package es.miapp.ad.ej5chatclient.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private long id;

    private String userMessage;

    private String userSend;

    private String userReceiver;

    private String messageDate;

    public Message(String userMessage, String userSend, String userReceiver, String messageDate) {
        this.userMessage = userMessage;
        this.userSend = userSend;
        this.userReceiver = userReceiver;
        this.messageDate = messageDate;
    }
}
