package ultrathlete.com.training.service;

import ultrathlete.com.training.bo.impl.Locations;
import ultrathlete.com.training.bo.impl.ResultLocations;

public interface LocationService {

    ResultLocations getElevation(Locations locations);
}
