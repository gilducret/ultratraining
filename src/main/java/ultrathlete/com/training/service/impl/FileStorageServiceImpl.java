package ultrathlete.com.training.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ultrathlete.com.training.bo.impl.SessionFileImpl;
import ultrathlete.com.training.bo.impl.UserImpl;
import ultrathlete.com.training.data.FileDAO;
import ultrathlete.com.training.service.FileStorageService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private FileDAO fileDAO;

    @Override
    public void save(SessionFileImpl[] files, UserImpl user) {
        if (user != null) {
            List<SessionFileImpl> toSave = new ArrayList<>();
            for (SessionFileImpl file : files) {
                file.setUser(user);
                toSave.add(file);
            }
            // Save all the files
            fileDAO.saveAll(toSave);
        }
    }

    public void delete(List<Integer> fileIds, UserImpl user) {
        fileDAO.deleteAllById(fileIds);
    }

    public Collection<SessionFileImpl> getFiles(UserImpl user) {
        if (user != null) {
            return fileDAO.loadFiles(user.getId());
        } else {
            return new ArrayList<>();
        }
    }
}
