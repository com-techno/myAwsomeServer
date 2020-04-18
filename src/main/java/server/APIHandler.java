package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.MyDatabase;
import database.PoorDatabase;
import util.HashUtils;

import java.io.IOException;

import static util.ArticleUtils.*;
import static util.DecodeUtils.writeJson;
import static util.StreamUtils.readStream;
import static util.UserUtils.signIn;
import static util.UserUtils.signUp;

public class APIHandler implements HttpHandler {

    MyDatabase database = new PoorDatabase();
    Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String[] path = exchange.getRequestURI().getPath().substring(1).split("/");
            String json = readStream(exchange.getRequestBody());
            Headers headers = exchange.getRequestHeaders();
            switch (path[0]) {
                case "sign_up":
                    signUp(exchange, gson, database, json);
                    break;
                case "sign_in":
                    signIn(exchange, gson, database, json);
                    break;
                case "new_article":
                    checkToken(headers);
                    addArticle(exchange, gson, database);
                    break;
                case "get_article":
                    checkToken(headers);
                    getArticle(exchange, gson, database);
                    break;
                case "get_articles":
                    checkToken(headers);
                    getArticles(exchange, gson, database);
                    break;
                case "leave_comment":
                    checkToken(headers);
                    leaveComment(exchange, gson, database);
                    break;
                default:
                    exchange.sendResponseHeaders(404, 0);
            }
        } catch (Exception e) {
            writeJson(exchange, "error", e.getMessage());
        } finally {
            exchange.close();
        }
    }

    private void checkToken(Headers headers) throws Exception {
        String token = headers.getFirst("Token");
        if (token == null) throw new Exception("Token expected");
        if (!HashUtils.checkToken(token)) throw new Exception("Invalid token");
    }
}


