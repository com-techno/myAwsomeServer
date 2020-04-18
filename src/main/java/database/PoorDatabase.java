package database;

import com.google.gson.Gson;
import objects.Article;
import objects.Comment;
import objects.TimeStamp;
import objects.User;
import util.HashUtils;
import util.StreamUtils;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class PoorDatabase implements MyDatabase {

    File dataRoot = new File("data");
    File usersDir = new File("data" + File.separator + "users");
    File articlesDir = new File("data" + File.separator + "articles");

    Gson gson = new Gson();

    public PoorDatabase() {
        if (!usersDir.exists())
            usersDir.mkdirs();
        if (!articlesDir.exists())
            articlesDir.mkdirs();
    }


    @Override
    public void signUp(User user) throws Exception {
        File userFile = new File(usersDir, user.username);
        try {
            if (userFile.exists()) throw new Exception("A user with this id already exists");
            userFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(userFile);
            fos.write(gson.toJson(user).getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new Exception("Server error occurred");
        }
    }

    @Override
    public String signIn(User user) throws Exception {
        File userFile = new File(usersDir, user.username);
        if (userFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(userFile);
                User trueUser = gson.fromJson(StreamUtils.readStream(fis), User.class);
                if (user.equals(trueUser))
                    return HashUtils.getURLEncodedToken(user);
            } catch (Exception e) {
                throw new Exception("Server error occurred");
            }
        }
        throw new Exception("There is no user with this ID");
    }

    @Override
    public int addArticle(Article article) throws Exception {
        int currId = 0;
        File articleFile;
        do
            articleFile = new File(articlesDir, ++currId + "");
        while (articleFile.exists());
        FileOutputStream fos;
        try {
            articleFile.createNewFile();
            fos = new FileOutputStream(articleFile);
            article.giveId(currId);
            fos.write(gson.toJson(article).getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new Exception("Server error occurred");
        }
        return currId;
    }

    @Override
    public Article getArticle(int id) throws Exception {
        File articleFile = new File(articlesDir, id + "");
        if (articleFile.exists())
            try {
                return gson.fromJson(StreamUtils.readStream(new FileInputStream(articleFile)), Article.class);
            } catch (FileNotFoundException e) {
                throw new Exception("An server error occurred");
            }
        throw new Exception("An article with this ID does not exist");
    }

    @Override
    public boolean deleteArticle(int id, User user) {
        File articleFile = new File(articlesDir, id + "");
        boolean ok = false;
        if (articleFile.exists()) {
            try {
                Article target = gson.fromJson(StreamUtils.readStream(new FileInputStream(articleFile)), Article.class);
                if (target.getAuthor().equals(user.username)) {
                    ok = true;
                    articleFile.delete();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return ok;
    }

    @Override
    public List<Article> getArticles() {
        List<Article> articles = new LinkedList<>();
        for (File articleFile : articlesDir.listFiles())
            try {
                articles.add(gson.fromJson(StreamUtils.readStream(new FileInputStream(articleFile)), Article.class));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        return articles;
    }

    @Override
    public boolean addComment(int id, Comment comment) {
        File articleFile = new File(articlesDir, id + "");
        boolean ok = false;
        if (!articleFile.exists()) return false;
        try {
            Article target = gson.fromJson(StreamUtils.readStream(new FileInputStream(articleFile)), Article.class);
            comment.setPublishTime(new TimeStamp(Calendar.getInstance()));
            System.out.println(comment);
            target.addComment(comment);
            FileOutputStream fos = new FileOutputStream(articleFile);
            fos.write(gson.toJson(target).getBytes());
            fos.flush();
            fos.close();
            ok = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean like(int id, int like) {
        File articleFile = new File(articlesDir, id + "");
        boolean ok = false;
        if (articleFile.exists()) {
            try {
                Article target = gson.fromJson(StreamUtils.readStream(new FileInputStream(articleFile)), Article.class);
                ok = target.changeLikes(like);
                FileOutputStream fos = new FileOutputStream(articleFile);
                fos.write(gson.toJson(target).getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ok;
    }
}
