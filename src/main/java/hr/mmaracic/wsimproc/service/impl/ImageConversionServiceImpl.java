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
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
public class ImageConversionServiceImpl implements ImageConversionService {

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
    public ImageConversion performConversion(BufferedImage image, List<ImagePoint> points, User user) {
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

            //Add polygons
            List<Polygon> polys = executeConversionAlgorithm(image, points);
            for(Polygon poly: polys){
                svgGenerator.drawPolygon(poly);
            }
            
//            svgGenerator.drawImage(image, 0, 0, new ImageObserver() {
//                @Override
//                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                }
//            });

            // Finally, stream out SVG to a file using
            // UTF-8 encoding.
            boolean useCSS = true; // we want to use CSS style attributes
            File outFile = new File("svgTest.svg");
            FileOutputStream fop = new FileOutputStream(outFile);
            out = new OutputStreamWriter(fop, "UTF-8");
            svgGenerator.stream(out, useCSS);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ImageConversionService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SVGGraphics2DIOException ex) {
            Logger.getLogger(ImageConversionService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImageConversionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ImageConversionService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return conversion;
    }

    private List<Polygon> executeConversionAlgorithm(BufferedImage image, List<ImagePoint> points) {
        List<Polygon> polygons = new ArrayList<>();
        
        for(ImagePoint point: points){
            Set<Point> usedPoints = new HashSet<>();
            Set<Point> edgePoints = new HashSet<>();
            Set<Point> seedPoints = new HashSet<>();
            seedPoints.add(new Point(point.getX(), point.getY()));
            
            Color c = new Color(image.getRGB(point.getX(), point.getY()));
            Set<Color> colorSet = new HashSet<>();
            colorSet.add(c);
            expandSeed(usedPoints, edgePoints, seedPoints, image, colorSet);
            
            int[] x = new int[edgePoints.size()];
            int[] y = new int[edgePoints.size()];
            
            Iterator<Point> it = edgePoints.iterator();
            for(int j=0; j<edgePoints.size();j++){
                Point p = it.next();
                x[j] = p.x;
                y[j] = p.y;
            }
            
            Polygon poly = new Polygon(x,y,x.length);
            polygons.add(poly);
        }
        return polygons;
    }

    private void expandSeed(Set<Point> usedPoints, Set<Point> edgePoints, Set<Point> seedPoints, BufferedImage image, Set<Color> colors) {
        while (seedPoints.size() > 0) {
            Point p = seedPoints.iterator().next();
            seedPoints.remove(p);
            usedPoints.add(p);

            int[] neighbourX = {0, 1, 0, -1};
            int[] neighbourY = {1, 0, -1, 0};
            int newNeighbours = 0;
            for (int i = 0; i < neighbourX.length; i++) {
                try {
                    Point n1 = new Point(p.x + neighbourX[i], p.y + neighbourY[i]);
                    Color c1 = new Color(image.getRGB(n1.x, n1.y));
                    if (colors.contains(c1) && !usedPoints.contains(n1)) {
                        seedPoints.add(p);
                        newNeighbours++;
                    }
                } catch (Exception ex) {

                }
            }
            if (newNeighbours == 0){
                edgePoints.add(p);
            }
        }
    }
}
