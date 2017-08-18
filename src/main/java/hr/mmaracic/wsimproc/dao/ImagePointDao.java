/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.dao;

import java.util.List;
import hr.mmaracic.wsimproc.model.ImageConversion;
import hr.mmaracic.wsimproc.model.ImagePoint;

/**
 *
 * @author Marijo
 */
public interface ImagePointDao {
    
    List<ImagePoint> getPointsFormImage(ImageConversion image);
    
    ImagePoint create (ImagePoint point);    
}
