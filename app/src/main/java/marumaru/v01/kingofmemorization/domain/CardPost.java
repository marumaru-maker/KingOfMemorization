package marumaru.v01.kingofmemorization.domain;

import java.util.Date;

public class CardPost {

    private Long cno;      // PK
    private Long sno;       // 공유 no
    private String title;   // 제목
    private String reg_date;  // 등록일
    private String writer;  // 작성자
    private Long star;      // 공유 갯수
    private String category;// 카테고리 ',' 로 구분
    private String cnos;

    public Long getSno() { return sno; }

    public void setSno(Long sno) { this.sno = sno;}

    public String getCnos() { return cnos; }

    public void setCnos(String cnos) { this.cnos = cnos; }

    public Long getCno() {
        return cno;
    }

    public void setCno(Long cno) {
        this.cno = cno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public Long getStar() {
        return star;
    }

    public void setStar(Long star) {
        this.star = star;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
