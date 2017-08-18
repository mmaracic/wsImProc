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
@Table(name = "ws_im_proc.image_conversion")
public class ImageConversion implements Serializable {
    
    @Id
    @GeneratedValue(generator = "id_generator")
    @SequenceGenerator(name = "id_generator", sequenceName = "ws_im_proc.seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private BigInteger id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "access_date", nullable = false)
    private Date accesDate;
    
    @ManyToOne
    private User user;
    
    @OneToMany(mappedBy = "image")
    private List<ImagePoint> points;
    
    @Column(name = "im_width", nullable = false)
    private int imWidth;
    
    @Column(name = "im_height", nullable = false)
    private int imHeight;
    
    @Column(name = "original_iamge", nullable = false)
    private byte[] originalImage;
    
    @Column(name = "vector_image", nullable = false)
    private byte[] vectorImage;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Date getAccesDate() {
        return accesDate;
    }

    public void setAccesDate(Date accesDate) {
        this.accesDate = accesDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getImWidth() {
        return imWidth;
    }

    public void setImWidth(int imWidth) {
        this.imWidth = imWidth;
    }

    public int getImHeight() {
        return imHeight;
    }

    public void setImHeight(int imHeight) {
        this.imHeight = imHeight;
    }

    public byte[] getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(byte[] originalImage) {
        this.originalImage = originalImage;
    }

    public byte[] getVectorImage() {
        return vectorImage;
    }

    public void setVectorImage(byte[] vectorImage) {
        this.vectorImage = vectorImage;
    }

    public List<ImagePoint> getPoints() {
        return points;
    }

    public void setPoints(List<ImagePoint> points) {
        this.points = points;
    }
}
