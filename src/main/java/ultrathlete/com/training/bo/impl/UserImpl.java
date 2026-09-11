package ultrathlete.com.training.bo.impl;

import ultrathlete.com.training.bo.User;
import ultrathlete.com.training.service.model.Model2;
import ultrathlete.com.training.service.model.Model3;
import ultrathlete.com.training.service.model.Model4;

import jakarta.persistence.*;

@Entity
@Table(name = "user", schema = "ultratraining")
public class UserImpl implements User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    @Column(unique = true)
    private String auth0Id;

    public String getAuth0Id() {
        return auth0Id;
    }

    public void setAuth0Id(String auth0Id) {
        this.auth0Id = auth0Id;
    }

    @OneToOne( cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="model2Id")
    private Model2  model2;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="model3Id")
    private Model3  model3;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="model4Id")
    private Model4 model4;

    public Model2 getModel2() {
        return model2;
    }

    public void setModel2(Model2 model2) {
        this.model2 = model2;
    }

    public Model3 getModel3() {
        return model3;
    }

    public void setModel3(Model3 model3) {
        this.model3 = model3;
    }

    public Model4 getModel4() {
        return model4;
    }

    public void setModel4(Model4 model4) {
        this.model4 = model4;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }
}
