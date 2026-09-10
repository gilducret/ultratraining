package ultrathlete.com.training.model;

import javax.persistence.*;
import java.util.Set;

public class Model {

    private int id;
    private String name;

    private Set<Variable> variables;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
