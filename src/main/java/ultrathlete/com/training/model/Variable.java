package ultrathlete.com.training.model;

import jakarta.persistence.*;

@Entity
@Table(name = "modelvariables", schema = "ultratraining")
public class Variable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
