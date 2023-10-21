package com.geko.convertgeko.Utils;


import javafx.scene.control.ComboBox;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import javax.imageio.ImageIO;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;


public class Validations {


    public static String isSelectedFilter(Boolean Radiojpg_pdf, Boolean RadioUnir_pdf, Boolean RadioSeparar_pdf, Boolean RadioPng_pdf,Boolean Radiopdf_word) {
        if (!Radiojpg_pdf && !RadioUnir_pdf && !RadioSeparar_pdf && !RadioPng_pdf && !Radiopdf_word) {
            return "not";
        }
        if (Radiojpg_pdf) return "*.jpg";
        if (RadioUnir_pdf) return "*.pdf";
        if (RadioSeparar_pdf) return "*.pdf";
        if (RadioPng_pdf) return "*.png";
        if (Radiopdf_word) return "*.pdf";
        return "not";
    }

    public static void ConvertImageToPdf(String imagePath, String pdfPath, ComboBox hoja, ComboBox orientacion) {
        try {
            PDRectangle pdRectangle = null;
            float margin = 10;
            switch (hoja.getValue().toString()) {
                case "A1":
                    pdRectangle = PDRectangle.A1;
                    if (orientacion.getValue().equals("Horizontal")) {
                        PDRectangle pageSize = new PDRectangle(PDRectangle.A1.getHeight(), PDRectangle.A1.getWidth());
                        pdRectangle = pageSize;
                    }
                    break;
                case "A2":
                    pdRectangle = PDRectangle.A2;
                    if (orientacion.getValue().equals("Horizontal")) {
                        PDRectangle pageSize = new PDRectangle(PDRectangle.A2.getHeight(), PDRectangle.A2.getWidth());
                        pdRectangle = pageSize;
                    }
                    break;
                case "A3":
                    pdRectangle = PDRectangle.A3;
                    if (orientacion.getValue().equals("Horizontal")) {
                        PDRectangle pageSize = new PDRectangle(PDRectangle.A3.getHeight(), PDRectangle.A3.getWidth());
                        pdRectangle = pageSize;
                    }
                    break;
                case "A4":
                    pdRectangle = PDRectangle.A4;
                    if (orientacion.getValue().equals("Horizontal")) {
                        PDRectangle pageSize = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
                        pdRectangle = pageSize;
                    }
                    break;
                case "A5":
                    pdRectangle = PDRectangle.A5;
                    if (orientacion.getValue().equals("Horizontal")) {
                        PDRectangle pageSize = new PDRectangle(PDRectangle.A5.getHeight(), PDRectangle.A5.getWidth());
                        pdRectangle = pageSize;
                    }
                    break;
                case "Legal":
                    pdRectangle = pdRectangle.LEGAL;
                    if (orientacion.getValue().equals("Horizontal")) {
                        PDRectangle pageSize = new PDRectangle(PDRectangle.LEGAL.getHeight(), PDRectangle.LEGAL.getWidth());
                        pdRectangle = pageSize;
                    }
                    break;

            }

            PDDocument document = new PDDocument();
            PDPage page = new PDPage(pdRectangle);
            document.addPage(page);


            BufferedImage imag = ImageIO.read(new File(imagePath));
            if (imag != null) {
                PDImageXObject image = LosslessFactory.createFromImage(document, ImageIO.read(new File(imagePath)));
                PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true);
                PDRectangle pageSize = page.getMediaBox();


                float scale = Math.min(pageSize.getWidth() / image.getWidth(), pageSize.getHeight() / image.getHeight());
                contentStream.drawImage(image, 0, 150, image.getWidth() * scale, image.getHeight() * scale);
                contentStream.close();

                document.save(pdfPath);
                document.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String FilterExt(File file) {
        String fileNameWithExtension = file.getName();
        int lastDotIndex = fileNameWithExtension.lastIndexOf('.');

        if (lastDotIndex > 0) {
            String fileNameWithoutExtension = fileNameWithExtension.substring(0, lastDotIndex);
            return fileNameWithoutExtension;
        } else {
            // El archivo no tiene extensión
            return null;
        }
    }

    public static void splitPdf(String pdfPath, String outputDir, Boolean filtro, List<Integer> paginas) {
        try {
            try (PDDocument document = PDDocument.load(new File(pdfPath))) {
                Splitter splitter = new Splitter();
                List<PDDocument> pages = splitter.split(document);

                for (int i = 0; i < pages.size(); i++) {
                    if (filtro) {
                        for (int pag : paginas) {
                            if (pag-1 == i) {
                                try (PDDocument page = pages.get(i)) {
                                    System.out.println("page "+page.getNumberOfPages());
                                    page.save(outputDir + "/page_" + (i + 1) + "_Geko.pdf");
                                }
                            }

                        }

                    } else {
                        try (PDDocument page = pages.get(i)) {
                            page.save(outputDir + "/page_" + (i + 1) + "_Geko.pdf");
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    public static void mergePdf(List<String> pdfPaths, String outputPath) {
        try {
            PDFMergerUtility merger = new PDFMergerUtility();
            for (String pdfPath : pdfPaths) {
                merger.addSource(pdfPath);
            }
            merger.setDestinationFileName(outputPath + "\\GekoUnion.pdf");
            merger.mergeDocuments();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> StringToInteger(String hojas){
        List<Integer> paginas = new ArrayList<>();
        String[] hj =  hojas.split(",");
        for (String h: hj) {
            paginas.add(Integer.parseInt(h));
        }
        return paginas;
    }

    public static boolean PDFToWordConverter(String pdfFilePath,String wordFilePath) {
            try {

                PdfReader pdfReader = new PdfReader(new FileInputStream(pdfFilePath));
                XWPFDocument wordDocument = new XWPFDocument();

                for (int page = 1; page <= pdfReader.getNumberOfPages(); page++) {
                    String pageText = PdfTextExtractor.getTextFromPage(pdfReader, page, new SimpleTextExtractionStrategy());
                    XWPFParagraph paragraph = wordDocument.createParagraph();
                    paragraph.createRun().setText(pageText);
                }

                FileOutputStream fos = new FileOutputStream(wordFilePath);
                wordDocument.write(fos);
                fos.close();

                pdfReader.close();
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    public static boolean PDFToWordConverterFormat(String pdfFilePath,String wordFilePath) {
       return false;
    }



}
