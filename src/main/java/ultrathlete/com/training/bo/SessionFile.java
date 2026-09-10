package ultrathlete.com.training.bo;

import ultrathlete.com.training.bo.impl.UserImpl;

public interface SessionFile {
    String getName();
    long getSize();
    byte[] getData();
    String getType();
    void setType(String type);
    void setName(String name);
    void setSize(long size);
    void setData(byte[] data);
    void setUser(UserImpl user);
    UserImpl getUser();
}
