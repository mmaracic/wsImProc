/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.dao.impl;

import java.util.List;
import hr.mmaracic.wsimproc.base.GenericDao;
import hr.mmaracic.wsimproc.dao.ImageConversionDao;
import hr.mmaracic.wsimproc.model.ImageConversion;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Marijo
 */
@Repository
public class ImageConversionDaoImpl extends GenericDao<ImageConversion> implements ImageConversionDao{

    @Override
    public List<ImageConversion> getAll() {
        return getAll(ImageConversion.class);
    }
}
