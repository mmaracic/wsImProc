/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.controller;

import hr.mmaracic.wsimproc.model.ImageConversion;
import hr.mmaracic.wsimproc.model.ImagePoint;
import hr.mmaracic.wsimproc.model.User;
import hr.mmaracic.wsimproc.service.ImageConversionService;
import hr.mmaracic.wsimproc.service.UserService;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Marijo
 */
@Controller
@RequestMapping("/image")
public class ImageController {
    
    static final Logger log = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageConversionService conversionService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = POST, value = "/upload")
    public void uploadImage(@RequestBody MultipartFile file, HttpServletResponse response, HttpSession session) {

        User user = userService.getCurrentUser();
        List<ImagePoint> points = (List<ImagePoint>) session.getAttribute("points");

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            ImageConversion conversion = conversionService.performConversion(image, points, user);

            session.removeAttribute("points");

            response.setStatus(HttpServletResponse.SC_SEE_OTHER);
            response.setHeader("Location", "/");

            response.setContentType("image/svg+xml");
            response.setHeader("Content-Disposition", "attachment; filename=\"output.svg\"");
            ServletOutputStream outStr = response.getOutputStream();
            outStr.write(conversion.getVectorImage());
            outStr.flush();
        } catch (IOException ex) {
            log.error(ex.getLocalizedMessage());
        }
    }

    @RequestMapping(method = POST, value = "/points", consumes="application/x-www-form-urlencoded")
    public void uploadPoints(@RequestBody MultiValueMap<String, String> kvps, HttpServletResponse response, HttpSession session) {
        List<ImagePoint> points = new ArrayList<>();
        String strPoints = kvps.get("points").get(0);
        String[] tempPoints = strPoints.split("\\s*;\\s*");
        for(String tempPoint: tempPoints){
            String[] coordinates = tempPoint.split("\\s*,\\s*");
            try{
                ImagePoint point = new ImagePoint(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
                points.add(point);
            } catch (NumberFormatException ex){
                log.error(ex.getLocalizedMessage());
            }
        }
        if (points.size()>0){
            session.setAttribute("points", points);
        }

        response.setStatus(HttpServletResponse.SC_SEE_OTHER);
        response.setHeader("Location", "/");
    }
}
