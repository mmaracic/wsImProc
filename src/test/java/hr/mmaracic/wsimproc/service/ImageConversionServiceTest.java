/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.service;

import hr.mmaracic.wsimproc.model.ImageConversion;
import hr.mmaracic.wsimproc.model.ImagePoint;
import hr.mmaracic.wsimproc.model.User;
import hr.mmaracic.wsimproc.service.ImageConversionService;
import hr.mmaracic.wsimproc.service.UserService;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Marijo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-context.xml"})
@WebAppConfiguration
public class ImageConversionServiceTest {

    @Autowired
    private ImageConversionService conversionService;

    @Autowired
    private UserService userService;

    public ImageConversionServiceTest() {
    }

    //@Test
    public void getAllCinversions() {
        List<ImageConversion> conversions = conversionService.getAllCinversions();
        assertNotNull("List of conversions is null", conversions);
    }

    //@Test
    public void getConversionForCurrentUser() {
        List<ImageConversion> conversions = conversionService.getConversionForCurrentUser();
        assertNotNull("List of conversions is null", conversions);
    }

    @Test
    public void performConversion() throws IOException {
        Calendar cal = Calendar.getInstance();
        User user = userService.getUserByUsername("marijo");
        assertNotNull("Current user is null", user);

        List<ImagePoint> points = new ArrayList<>();
        points.add(new ImagePoint(31, 75));
        points.add(new ImagePoint(149, 37));
        points.add(new ImagePoint(132, 141));

        File imFile = new File(ImageConversionServiceTest.class.getClassLoader().getResource("sample.png").getFile());
        assertNotNull("Image file is null", imFile);

        BufferedImage image = ImageIO.read(imFile);
        assertNotEquals("Image width is 0", 0, image.getWidth());
        assertNotEquals("Image height is 0", 0, image.getHeight());

        ImageConversion conversion = conversionService.performConversion(image, points, user);
        assertNotNull("Conversion is null", conversion);

        File outFile = new File("svgTest_"+cal.getTimeInMillis()+".svg");
        FileOutputStream fop = new FileOutputStream(outFile);
        fop.write(conversion.getVectorImage());
        fop.close();

    }
}
