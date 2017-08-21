/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.service.impl;

import hr.mmaracic.wsimproc.dao.ImageConversionDao;
import hr.mmaracic.wsimproc.model.ImageConversion;
import hr.mmaracic.wsimproc.model.ImagePoint;
import hr.mmaracic.wsimproc.model.User;
import hr.mmaracic.wsimproc.service.ImageConversionService;
import hr.mmaracic.wsimproc.service.UserService;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 *
 * @author Marijo
 */
@Service
@Transactional
public class ImageConversionServiceImpl implements ImageConversionService{
    
    @Autowired
    private ImageConversionDao conversionDao;
    
    @Autowired
    private UserService userService;

    @Override
    public List<ImageConversion> getAllCinversions() {
        List<ImageConversion> allConversions = conversionDao.getAll();
        return allConversions;
    }

    @Override
    public List<ImageConversion> getConversionForCurrentUser() {
        User user = userService.getCurrentUser();
        List<ImageConversion> conversions = conversionDao.getByUser(user);
        return conversions;
    }

    @Override
    public ImageConversion performConversion(Image image, List<ImagePoint> points, User user) {
        ImageConversion conversion = new ImageConversion();
        Writer out = null;
        try {
            // Get a DOMImplementation.
            DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
            // Create an instance of org.w3c.dom.Document.
            String svgNS = "http://www.w3.org/2000/svg";
            Document document = domImpl.createDocument(svgNS, "svg", null);
            // Create an instance of the SVG Generator.
            SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
            
            //Add content
            svgGenerator.drawImage(image, 0, 0, new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            
            // Finally, stream out SVG to the standard output using
            // UTF-8 encoding.
            boolean useCSS = true; // we want to use CSS style attributes
            out = new OutputStreamWriter(System.out, "UTF-8");
            svgGenerator.stream(out, useCSS);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ImageConversionService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SVGGraphics2DIOException ex) {
            Logger.getLogger(ImageConversionService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ImageConversionService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return conversion;
    }    
}
