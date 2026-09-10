package ultrathlete.com.training.bo;

import javax.persistence.*;

@Entity
@Table(name = "prediction", schema = "ultratraining")
public class Prediction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;
    private double distance;
    private Integer minTime;
    private Integer maxTime;
    private Integer status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name="sessionId" )
    private PredictedSession session;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Integer getMinTime() {
        return minTime;
    }

    public void setMinTime(Integer minTime) {
        this.minTime = minTime;
    }

    public Integer getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Integer maxTime) {
        this.maxTime = maxTime;
    }
}
