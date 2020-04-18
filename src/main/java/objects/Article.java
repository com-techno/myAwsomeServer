package objects;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import objects.forms.NewArticleForm;

public class Article {

    String author;
    String title;
    String icon;
    String body;
    TimeStamp publishTime;
    TimeStamp editTime = null;
    int id;
    int likes;
    int dislikes;
    List<Comment> comments = null;

    public Article(NewArticleForm newArticle) throws Exception {
        checkCompletion();
        this.author = newArticle.getAuthor();
        this.title = newArticle.getTitle();
        this.body = newArticle.getBody();
        this.icon = newArticle.getIcon();
        this.publishTime = new TimeStamp(Calendar.getInstance());
    }

    public void checkCompletion() throws Exception {
        if (author == null || title == null || body == null)throw new Exception("Form is incomplete");
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishTime(TimeStamp time) {
        this.publishTime = time;
    }

    public String getAuthor() {
        return author;
    }

    public void giveId(int id){
        this.id = id;
    }

    public void addComment(Comment comment){
        if (comments == null) comments = new LinkedList<>();
        comments.add(comment);
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
