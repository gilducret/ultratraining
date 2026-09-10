package ultrathlete.com.training.model;

import jakarta.persistence.*;

@Entity
@Table(name = "uservariables", schema = "ultratraining")
public class UserVariable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;


}
