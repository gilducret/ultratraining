package ultrathlete.com.training.bo.impl;

import ultrathlete.com.training.bo.SessionFile;

import jakarta.persistence.*;

@Entity
@Table(name = "file", schema = "ultratraining")
public class SessionFileImpl implements SessionFile {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;
    //@Lob
    @Column(name = "data", columnDefinition="bytea")
    private byte[] data;
    private long size;

    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name="userId" )
    private UserImpl user;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public UserImpl getUser() {
        return user;
    }

    public void setUser(UserImpl user) {
        this.user = user;
    }
}
