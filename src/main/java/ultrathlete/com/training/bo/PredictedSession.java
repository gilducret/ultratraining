package ultrathlete.com.training.bo;

import ultrathlete.com.training.bo.impl.UserImpl;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "predictedsession", schema = "ultratraining")
public class PredictedSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private Date date;

    // Raw GPX file — kept until post-race cleanup
    @Column(name = "gpxdata", columnDefinition = "bytea")
    private byte[] gpxData;

    // JSON prediction for mobile download — kept permanently
    @Column(name = "sessiondata", columnDefinition = "bytea")
    private byte[] sessionData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserImpl user;

    @Column(name = "status")
    private String status; // "DRAFT", "READY"

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public byte[] getGpxData() { return gpxData; }
    public void setGpxData(byte[] gpxData) { this.gpxData = gpxData; }

    public byte[] getSessionData() { return sessionData; }
    public void setSessionData(byte[] sessionData) {
        this.sessionData = sessionData;
    }

    public UserImpl getUser() { return user; }
    public void setUser(UserImpl user) { this.user = user; }
}