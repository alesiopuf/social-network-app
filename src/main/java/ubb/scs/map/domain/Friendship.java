package ubb.scs.map.domain;

import java.time.LocalDateTime;

public class Friendship extends Entity<Tuple<Long, Long>> {

    private LocalDateTime date;

    private Status status;

    public Friendship(Tuple<Long, Long> id) {
        super.setId(id);
        date = LocalDateTime.now();
        status = Status.PENDING;
    }

    public Friendship(Tuple<Long, Long> id, LocalDateTime date, Status status) {
        super.setId(id);
        this.date = date;
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getFirst() {
        return super.getId().getFirst();
    }

    public Long getSecond() {
        return super.getId().getSecond();
    }

    public void setFirst(Long first) {
        super.getId().setFirst(first);
    }

    public void setSecond(Long second) {
        super.getId().setSecond(second);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "date=" + date +
                ", first=" + getFirst() +
                ", second=" + getSecond() +
                '}';
    }
}
