/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Marijo
 */
@Entity
@Table(name = "ws_im_proc.users")
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "id_generator")
    @SequenceGenerator(name = "id_generator", sequenceName = "ws_im_proc.seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private BigInteger id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_access", nullable = false)
    private Date lastAccess;

    @OneToMany(mappedBy = "user")
    private List<ImageConversion> images;
    
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public List<ImageConversion> getImages() {
        return images;
    }

    public void setImages(List<ImageConversion> images) {
        this.images = images;
    }
}
