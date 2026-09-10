package ultrathlete.com.training.service;

import ultrathlete.com.training.bo.impl.SessionFileImpl;
import ultrathlete.com.training.bo.impl.UserImpl;

import java.util.Collection;
import java.util.List;

public interface FileStorageService {
    void save(SessionFileImpl[] files, UserImpl user);
    Collection<SessionFileImpl> getFiles(UserImpl user);
    void delete(List<Integer> fileIds, UserImpl user);
}
