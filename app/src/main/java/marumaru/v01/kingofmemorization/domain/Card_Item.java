package marumaru.v01.kingofmemorization.domain;

public class Card_Item {

    private Long ino;
    private Long cno;
    private String item_key;
    private String item_value;

    public Long getIno() {
        return ino;
    }

    public void setIno(Long ino) {
        this.ino = ino;
    }

    public Long getCno() {
        return cno;
    }

    public void setCno(Long cno) {
        this.cno = cno;
    }

    public String getItem_key() {
        return item_key;
    }

    public void setItem_key(String item_key) {
        this.item_key = item_key;
    }

    public String getItem_value() {
        return item_value;
    }

    public void setItem_value(String item_value) {
        this.item_value = item_value;
    }
}
