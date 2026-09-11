package ultrathlete.com.training.bo;

import ultrathlete.com.training.service.model.Model2;
import ultrathlete.com.training.service.model.Model3;
import ultrathlete.com.training.service.model.Model4;

public interface User {
    int getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getAuth0Id();
    Model2 getModel2();
    Model3 getModel3();
    Model4 getModel4();
}
