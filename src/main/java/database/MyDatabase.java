package database;

import objects.*;

import java.util.List;

public interface MyDatabase {
    void signUp(User user) throws Exception;
    String signIn(User user) throws Exception;

    int addArticle(Article article) throws Exception;
    Article getArticle(int id) throws Exception;
    boolean deleteArticle(int id, User user);
    List<Article> getArticles();
    boolean addComment(int id, Comment comment);
    boolean like(int id, int like);
}