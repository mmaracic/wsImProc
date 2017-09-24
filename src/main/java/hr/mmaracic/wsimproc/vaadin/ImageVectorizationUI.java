/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.mmaracic.wsimproc.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import hr.mmaracic.wsimproc.service.ImageConversionService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Marijo
 */
@Theme("valo")
@SpringUI
public class ImageVectorizationUI extends UI {
    
    @Autowired
    ImageConversionService imageConversionService;

    @Override
    protected void init(VaadinRequest request) {

        // The root of the component hierarchy
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull(); // Use entire window

        setContent(content); // Attach to the UI
        
        // Add some component
        content.addComponent(new Label("Hello!"));
        
        // Layout inside layout
        HorizontalLayout hor = new HorizontalLayout();

        hor.setSizeFull(); // Use all available space
        // Couple of horizontally laid out components
        Tree tree = new Tree("My Tree");

        hor.addComponent(tree);
        Table table = new Table("My Table");
        table.setSizeFull();
        
        hor.addComponent(table);
        hor.setExpandRatio(table,1); // Expand to fill
        content.addComponent(hor);

        content.setExpandRatio(hor,1); // Expand to fill    
    }
}
