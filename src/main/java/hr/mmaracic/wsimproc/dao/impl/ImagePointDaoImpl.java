/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.dao.impl;

import java.util.List;
import javax.persistence.Query;
import hr.mmaracic.wsimproc.base.GenericDao;
import hr.mmaracic.wsimproc.dao.ImagePointDao;
import hr.mmaracic.wsimproc.model.ImageConversion;
import hr.mmaracic.wsimproc.model.ImagePoint;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Marijo
 */
@Repository
public class ImagePointDaoImpl extends GenericDao<ImagePoint> implements ImagePointDao{

    @Override
    public List<ImagePoint> getPointsFormImage(ImageConversion image) {
        Query query = this.entityManager.createQuery("SELECT p  FROM ImagePoint AS p LEFT JOIN p.image AS im WHERE im.id = :ID");
        query.setParameter("ID", image.getId());
        List<ImagePoint> points = query.getResultList();
        return points;
    }
    
}
