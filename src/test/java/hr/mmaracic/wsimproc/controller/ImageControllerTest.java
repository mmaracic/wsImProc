/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.python.google.common.io.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

/**
 * Spring MVC test
 * @author Marijo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
@WebAppConfiguration
public class ImageControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void checkController() {
        ServletContext servletContext = wac.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("imageController"));
    }

    @Test
    @Transactional //So the script would be rolled back https://stackoverflow.com/questions/12626502/rollback-transaction-after-test
    public void testController() throws UnsupportedEncodingException, Exception {        
        URL urlPoints = ResourceUtils.getURL("src/test/resources/Seeds.txt");
        String points = Files.readLines(new File(urlPoints.toURI()), Charset.forName("UTF-8")).stream().collect(Collectors.joining("\r\n")); //kvp form
        this.mockMvc.perform(MockMvcRequestBuilders.post("/image/points").param("points", points).contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(MockMvcResultMatchers.status().is3xxRedirection());

//        URL urlIm = ResourceUtils.getURL("src/test/resources/sample.png");
//        FileInputStream fisIm = new FileInputStream(new File(urlIm.toURI()));
//        MockMultipartFile mmpf = new MockMultipartFile("sample.png", fisIm);
//        //upload file and expect 200 as response
//        this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image/upload").file(mmpf).contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//        //this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image/upload").file(mmpf)).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//        //this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image/upload").content(mmpf.getBytes())).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
