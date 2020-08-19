package marumaru.v01.kingofmemorization.domain;

public class Criteria {

    private String category; // "나무,산,바다" 형태의 문자열
    private String userId; // 찾고 싶은 카드의 유저 아이디

    private boolean time_asc; // true 일 경우에는 시간 순서로 아닐 경우 내림차순(= 최신꺼부터)
    private boolean star_desc; // true 일 경우에는 별 갯수 내림차순 아닐 경우 오름차순
    private boolean writer_only; // true 일 경우에는 입력된 userId 게시물만

    public Criteria(){
        category = "";
        userId = "";
        time_asc = false;
        star_desc = false;
        writer_only = false;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isTime_asc() {
        return time_asc;
    }

    public void setTime_asc(boolean time_asc) {
        this.time_asc = time_asc;
    }

    public boolean isStar_desc() {
        return star_desc;
    }

    public void setStar_desc(boolean star_desc) {
        this.star_desc = star_desc;
    }

    public boolean isWriter_only() {
        return writer_only;
    }

    public void setWriter_only(boolean writer_only) {
        this.writer_only = writer_only;
    }
}
