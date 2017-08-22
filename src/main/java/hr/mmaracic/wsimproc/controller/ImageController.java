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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Marijo
 */
@RestController
@RequestMapping("/image")
public class ImageController {

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
            Logger.getLogger(ImageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @RequestMapping(method = POST, value = "/points")
    public void uploadPoints(@RequestBody List<ImagePoint> points, HttpServletResponse response, HttpSession session) {

        session.setAttribute("points", points);

        response.setStatus(HttpServletResponse.SC_SEE_OTHER);
        response.setHeader("Location", "/");
    }

}
