package marumaru.v01.kingofmemorization.domain;

public class Card {

    private Long cno;
    private String userID;
    private String reg_date;
    private String title;
    private String category;
    private Long num_star;

    public Long getCno() {
        return cno;
    }

    public void setCno(Long cno) {
        this.cno = cno;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getNum_star() {
        return num_star;
    }

    public void setNum_star(Long num_star) {
        this.num_star = num_star;
    }

    public String getCategory() {return category; }

    public void setCategory(String category) {this.category = category; }
}
