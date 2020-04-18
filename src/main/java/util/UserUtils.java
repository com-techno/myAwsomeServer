package util;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import database.MyDatabase;
import objects.User;

import static util.DecodeUtils.writeJson;


public class UserUtils {

    public static void signUp(HttpExchange exchange, Gson gson, MyDatabase database, String json) throws Exception {
        User user = gson.fromJson(json, User.class);
        database.signUp(user);
        writeJson(exchange, "message", "User successfully registered");
    }

    public static void signIn(HttpExchange exchange, Gson gson, MyDatabase database, String json) throws Exception {
        writeJson(exchange, "token", database.signIn(gson.fromJson(json, User.class)));
    }
}
