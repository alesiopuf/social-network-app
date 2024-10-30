package ubb.scs.map.domain;

import java.time.LocalDateTime;

public class Friendship extends Entity<Tuple<Long, Long>> {

    private LocalDateTime date;

    public Friendship(Tuple<Long, Long> id) {
        super.setId(id);
        date = LocalDateTime.now();
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

    @Override
    public String toString() {
        return "Friendship{" +
                "date=" + date +
                ", first=" + getFirst() +
                ", second=" + getSecond() +
                '}';
    }
}
