/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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
@Table(name = "ws_im_proc.image_point")
public class ImagePoint implements Serializable {
    
    @Id
    @GeneratedValue(generator = "id_generator")
    @SequenceGenerator(name = "id_generator", sequenceName = "ws_im_proc.seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private BigInteger id;
    
    @ManyToOne
    private ImageConversion image;
    
    @Column(name = "x", nullable = false)
    private int x;
    
    @Column(name = "y", nullable = false)
    private int y;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public ImageConversion getImage() {
        return image;
    }

    public void setImage(ImageConversion image) {
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
