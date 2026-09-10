package ultrathlete.com.training.bo;

import ultrathlete.com.training.bo.impl.UserImpl;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "predictedsession", schema = "ultratraining")
public class PredictedSession {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;
    private Date date;
    @Column(name = "data", columnDefinition="bytea")
    private byte[] data;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name="userId" )
    private UserImpl user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public UserImpl getUser() {
        return user;
    }

    public void setUser(UserImpl user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }
}
