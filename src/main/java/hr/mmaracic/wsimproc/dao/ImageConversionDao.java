/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.dao;

import java.util.List;
import hr.mmaracic.wsimproc.model.ImageConversion;
import hr.mmaracic.wsimproc.model.User;

/**
 *
 * @author Marijo
 */
public interface ImageConversionDao {
    
    List<ImageConversion> getAll();
    
    List<ImageConversion> getByUser(User user);
    
    ImageConversion create(ImageConversion image);    
}
