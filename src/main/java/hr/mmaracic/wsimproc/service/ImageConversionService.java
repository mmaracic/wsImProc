/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.service;

import hr.mmaracic.wsimproc.model.ImageConversion;
import hr.mmaracic.wsimproc.model.ImagePoint;
import hr.mmaracic.wsimproc.model.User;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 *
 * @author Marijo
 */
public interface ImageConversionService {
    
    List<ImageConversion> getAllCinversions();
    
    List<ImageConversion> getConversionForCurrentUser();
    
    ImageConversion performConversion(BufferedImage image, List<ImagePoint> points, User user);    
}
