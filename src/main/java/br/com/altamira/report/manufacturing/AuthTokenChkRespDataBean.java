package br.com.altamira.report.manufacturing;

public class AuthTokenChkRespDataBean {

    private String accessToken;
    private String userName;
    private String lastName;
    private String loggedinSince;
    private String firstName;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLoggedinSince() {
        return loggedinSince;
    }

    public void setLoggedinSince(String loggedinSince) {
        this.loggedinSince = loggedinSince;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
