package ch.heigvd.wordhuntclient.activities.Login;

/**
 * Created by paulnta on 27.05.15.
 */
public class AccessToken {

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private int status;
    private String token;
    private static AccessToken instance = null;

    private AccessToken(){}

    public static AccessToken getInstance(){
        if(instance == null)
            instance = new AccessToken();

        return instance;
    }

}
