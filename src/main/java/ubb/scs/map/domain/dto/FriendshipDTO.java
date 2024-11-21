package ubb.scs.map.domain.dto;

public class FriendshipDTO {
    private String to;
    private String date;
    private String status;

    public FriendshipDTO(String to, String date, String status) {
        this.to = to;
        this.date = date;
        this.status = status;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
