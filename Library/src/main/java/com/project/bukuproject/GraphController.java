package com.project.bukuproject;

import com.project.bukuproject.Model.Buku;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class GraphController implements Initializable {

    @FXML
    private Pane grafNode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Membuat graph (g) menggunakan SparseMultigraph dari JUNG Framework
        HelloController helloController = new HelloController();
        java.util.List<Buku> recommendedBooks = helloController.books(); // Mendapatkan daftar buku dari HelloController

        // Membuat graph menggunakan buku-buku yang didapatkan
        Graph<String, String> g = helloController.createGraphFromBooks(recommendedBooks);

        // Membuat layout
        FRLayout<String, String> layout = new FRLayout<>(g);

        // Membuat VisualizationViewer dari JUNG
        VisualizationViewer<String, String> vv = new VisualizationViewer<>(layout);
        vv.addPreRenderPaintable(new VisualizationViewer.Paintable() {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.GRAY);
                g2d.drawString("Panning: Hold down the mouse button and drag", 10, 15);
            }

            @Override
            public boolean useTransform() {
                return false;
            }
        });



        // Menambahkan SwingNode ke StackPane agar dapat ditambahkan ke grafNode
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(vv);

        // Menambahkan swingNode ke stackPane
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(swingNode);

        // Set the preferred size of SwingNode based on the AnchorPane
        grafNode.widthProperty().addListener((observable, oldValue, newValue) -> {
            swingNode.getContent().setPreferredSize(new Dimension(newValue.intValue(), newValue.intValue()));
        });
        grafNode.heightProperty().addListener((observable, oldValue, newValue) -> {
            swingNode.getContent().setPreferredSize(new Dimension(newValue.intValue(), newValue.intValue()));
        });

        // Menambahkan stackPane ke grafNode
        grafNode.getChildren().add(stackPane);

        // Set up the picking and dragging behavior
        DefaultModalGraphMouse<String, String> gm = new DefaultModalGraphMouse<>();
        gm.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(gm);
    }
}
