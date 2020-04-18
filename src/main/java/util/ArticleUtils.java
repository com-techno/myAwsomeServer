package util;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.util.Map;

import database.MyDatabase;
import objects.Article;
import objects.Comment;
import objects.forms.NewArticleForm;
import objects.forms.Token;

import static util.DecodeUtils.writeJson;
import static util.StreamUtils.readStream;

public class ArticleUtils {

    public static void addArticle(HttpExchange exchange, Gson gson, MyDatabase database) throws Exception {
        String json = readStream(exchange.getRequestBody());
        Article article = new Article(gson.fromJson(json, NewArticleForm.class));
        writeJson(exchange, "id", database.addArticle(article) + "");
    }

    public static void getArticles(HttpExchange exchange, Gson gson, MyDatabase database) throws Exception {
        String s = exchange.getRequestURI().getQuery();
        Map<String, String> params = DecodeUtils.paramsToMap(s);
        boolean tokenTrue = HashUtils.checkToken(params.get("token"));
        if (tokenTrue)
            writeJson(exchange, gson.toJson(database.getArticles()));
        else throw new Exception("Invalid token");
    }

    public static void getArticle(HttpExchange exchange, Gson gson, MyDatabase database) throws Exception {
        String s = exchange.getRequestURI().getQuery();
        Map<String, String> params = DecodeUtils.paramsToMap(s);
        boolean tokenTrue = HashUtils.checkToken(params.get("token"));
        if (tokenTrue)
            writeJson(exchange, gson.toJson(database.getArticle(Integer.parseInt(params.get("id")))));
        else throw new Exception("Invalid token");
    }

    public static void leaveComment(HttpExchange exchange, Gson gson, MyDatabase database) throws Exception {
        String json = readStream(exchange.getRequestBody());
        Comment comment = gson.fromJson(json, Comment.class);
        Token token = gson.fromJson(json, Token.class);
        if (HashUtils.checkToken(token.token))
            database.addComment(comment.getArticle(), comment);
        else throw new Exception("Invalid token");
    }
}
