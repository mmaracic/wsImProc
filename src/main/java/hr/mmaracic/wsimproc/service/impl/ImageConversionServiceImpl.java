/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.service.impl;

import hr.mmaracic.wsimproc.dao.ImageConversionDao;
import hr.mmaracic.wsimproc.dao.ImagePointDao;
import hr.mmaracic.wsimproc.model.ImageConversion;
import hr.mmaracic.wsimproc.model.ImagePoint;
import hr.mmaracic.wsimproc.model.User;
import hr.mmaracic.wsimproc.service.ImageConversionService;
import hr.mmaracic.wsimproc.service.UserService;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.python.icu.util.Calendar;
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
    private ImagePointDao imagePointDao;

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
        Calendar cal = Calendar.getInstance();
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
            Map<Polygon, Color> polys = executeConversionAlgorithm(image, points);
            for(Map.Entry<Polygon, Color> entry: polys.entrySet()){
                svgGenerator.setPaint(entry.getValue());
                svgGenerator.fillPolygon(entry.getKey());
                svgGenerator.setColor(Color.black);
                svgGenerator.drawPolygon(entry.getKey());
            }
            
            // Finally, stream out SVG to a byte stream
            // UTF-8 encoding.
            boolean useCSS = true; // we want to use CSS style attributes
            ByteArrayOutputStream svgBos = new ByteArrayOutputStream();
            out = new OutputStreamWriter(svgBos, "UTF-8");            
            svgGenerator.stream(out, useCSS);
            
            //bytestream for original image
            ByteArrayOutputStream pngBos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", pngBos);
            
            //conversion object
            conversion.setAccesDate(cal.getTime());
            conversion.setImHeight(image.getHeight());
            conversion.setImWidth(image.getWidth());
            conversion.setOriginalImage(pngBos.toByteArray());
            conversion.setUser(user);
            conversion.setVectorImage(svgBos.toByteArray());
            
            //persist
            conversionDao.create(conversion);
            for(ImagePoint point: points){
                point.setImage(conversion);
                imagePointDao.create(point);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ImageConversionService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SVGGraphics2DIOException ex) {
            Logger.getLogger(ImageConversionService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
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

    private Map<Polygon, Color> executeConversionAlgorithm(BufferedImage image, List<ImagePoint> points) {
        Map<Polygon, Color> polygons = new HashMap<>();
        
        for(ImagePoint point: points){
            Set<Point> edgePoints = new HashSet<>();
            Set<Point> seedPoints = new HashSet<>();
            seedPoints.add(new Point(point.getX(), point.getY()));
            
            Color c = new Color(image.getRGB(point.getX(), point.getY()));
            Set<Color> colorSet = new HashSet<>();
            colorSet.add(c);
            colorSet.add(Color.black);
            expandSeed(edgePoints, seedPoints, image, colorSet);
            
            List<Point> edges = new ArrayList<>(edgePoints);
            Collections.sort(edges, (Point o1, Point o2) -> {
                double angle1 = Math.atan2(o1.y-point.getY(), o1.x-point.getX());
                double angle2 = Math.atan2(o2.y-point.getY(), o2.x-point.getX());
                return (angle1>angle2)?-1:1;
            });
            
            int[] x = new int[edges.size()];
            int[] y = new int[edges.size()];
            
            for(int j=0; j<edges.size();j++){
                Point p = edges.get(j);
                x[j] = p.x;
                y[j] = p.y;
            }
            
            Polygon poly = new Polygon(x,y,x.length);
            polygons.put(poly, c);
        }
        return polygons;
    }

    private void expandSeed(Set<Point> edgePoints, Set<Point> seedPoints, BufferedImage image, Set<Color> colors) {
        Set<Point> usedPoints = new HashSet<>();
        Deque<Point> queuedSeedPoints = new ArrayDeque<>(seedPoints);
        while (seedPoints.size() > 0) {
            Point p = queuedSeedPoints.getFirst();
            queuedSeedPoints.removeFirst();
            seedPoints.remove(p);
            usedPoints.add(p);

            int[] neighbourX = {0, 1, 0, -1};
            int[] neighbourY = {1, 0, -1, 0};
            int newNeighbours = 0;
            for (int i = 0; i < neighbourX.length; i++) {
                try {
                    Point n1 = new Point(p.x + neighbourX[i], p.y + neighbourY[i]);
                    Color c1 = new Color(image.getRGB(n1.x, n1.y));
                    if (colors.contains(c1) && !usedPoints.contains(n1) && !seedPoints.contains(n1)) {
                        seedPoints.add(n1);
                        queuedSeedPoints.addLast(n1);
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
