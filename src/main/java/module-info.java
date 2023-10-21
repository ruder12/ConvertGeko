module com.geko.convertgeko {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.apache.poi.ooxml;
    requires itextpdf;


    opens com.geko.convertgeko to javafx.fxml;
    exports com.geko.convertgeko;
    exports com.geko.convertgeko.Controller;
    opens com.geko.convertgeko.Controller to javafx.fxml;
}