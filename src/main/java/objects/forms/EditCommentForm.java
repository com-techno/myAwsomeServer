package objects.forms;

public class EditCommentForm {

    int articleId;
    int commentId;
    String username;
    String body;

    public int getCommentId() {
        return commentId;
    }

    public int getArticleId() {
        return articleId;
    }

    public String getUsername() {
        return username;
    }

    public String getBody() {
        return body;
    }
}
