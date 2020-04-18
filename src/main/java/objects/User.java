package objects;

public class User {
    public String username;
    public String passHash;

    public User(String username, String passHash) throws Exception {
        if (username == null || passHash == null) throw new Exception("Form is incomplete");
        this.username = username;
        this.passHash = passHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.username.equals(user.username) &&
                this.passHash.equals(user.passHash);
    }

}
