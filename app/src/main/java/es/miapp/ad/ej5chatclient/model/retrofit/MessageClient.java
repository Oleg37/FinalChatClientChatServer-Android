package es.miapp.ad.ej5chatclient.model.retrofit;

import java.util.ArrayList;

import es.miapp.ad.ej5chatclient.model.pojo.Message;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MessageClient {

    @GET("message")
    Call<ArrayList<Message>> getMessages();

    @GET("message/{id}")
    Call<Message> getMessageById(@Path("id") long id);

    @POST("message")
    Call<Message> insertMessage(@Body Message message);

    @PUT("message/{id}")
    Call<Boolean> updateMessage(@Path("id") long id, @Body Message message);

    @DELETE("message/{id}")
    Call<Message> deleteMessageById(@Path("id") long id);

    @GET("getPrivateMessage/{userSend}/{userReceiver}")
    Call<ArrayList<Message>> getPrivateMessage(@Path("userSend") String userSend, @Path("userReceiver") String userReceiver);

    @DELETE("destroyAllMSG")
    Call<ArrayList<Message>> deleteAll();
}
