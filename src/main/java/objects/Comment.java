package objects;

import java.util.Calendar;

public class Comment {


    String author;
    String body;
    TimeStamp publishTime;
    TimeStamp editTime = null;
    int article;
    int likes;
    int dislikes;

    public Comment(String body, User user) {
        this.publishTime = new TimeStamp(Calendar.getInstance());
        this.author = user.username;
        this.body = body;
    }

    public int getArticle() {
        return article;
    }

    public void setPublishTime(TimeStamp publishTime) {
        this.publishTime = publishTime;
    }

    public boolean editComment(String body, User user){
        if (!user.username.equals(this.author)) return false;
        this.body = body;
        this.editTime = new TimeStamp(Calendar.getInstance());
        return true;
    }

    public String getAuthor() {
        return author;
    }

    public boolean changeLikes(int opCode){
        boolean ok = true;
        switch (opCode){
            case -2:
                if (dislikes!=0) dislikes--;
                else ok = false;
                break;
            case -1:
                dislikes++;
                break;
            case 1:
                likes++;
                break;
            case 2:
                if (likes!=0) likes--;
                else ok = false;
                break;
            default:
                ok = false;
        }
        return ok;
    }
}
