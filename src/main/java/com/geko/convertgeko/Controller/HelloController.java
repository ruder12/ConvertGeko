package com.geko.convertgeko.Controller;

import com.geko.convertgeko.Utils.Opcion;
import com.geko.convertgeko.Utils.Validations;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.geko.convertgeko.Utils.Validations.*;


public class HelloController implements Initializable {
    public Button btnSelect;
    public Label labelFilePath;
    public RadioButton Radiojpg_pdf;
    public RadioButton RadioUnir_pdf;
    public RadioButton RadioSeparar_pdf;
    public RadioButton RadioPng_pdf;

    public ProgressBar progreesBar;
    public Button btnGenerar;
    public ComboBox comboBoxOrientacion;
    public ComboBox comboBoxHoja;
    public Button btnSaveArchivo;
    public Label labelSaveArchivo;
    public RadioButton filtro;
    public TextField textFiltroHojas;
    public RadioButton Radiopdf_word;
    public RadioButton Radioword_pdf;
    private Stage primaryStage;
    private FileChooser fileSelect;
    private File file;
    private List<File> fileList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filtro.setVisible(false);
        textFiltroHojas.setVisible(false);
        comboBoxOrientacion.setValue("Vertical");
        comboBoxOrientacion.getItems().addAll(Opcion.Orientacion);
        comboBoxHoja.setValue("A4");
        comboBoxHoja.getItems().addAll(Opcion.Hoja);

        labelSaveArchivo.setText(System.getProperty("user.home"));
        File archivo = new File("CovertWordPdf.py");
        if (!archivo.exists()) {
            System.out.println("OJO: ¡¡No existe el archivo de Python!!");
        } else {
            System.out.println("Si existe");
            System.out.println("patch file"+ archivo.getAbsoluteFile().getAbsolutePath());
        }
    }
    public void btnSelectAction(ActionEvent actionEvent) {

        primaryStage = new Stage();
        fileSelect = new FileChooser();
        String filter = Validations.isSelectedFilter(Radiojpg_pdf.isSelected(), RadioUnir_pdf.isSelected(), RadioSeparar_pdf.isSelected(), RadioPng_pdf.isSelected(),Radiopdf_word.isSelected(),Radioword_pdf.isSelected());
        if (!filter.equals("not")) {

            if (labelSaveArchivo.getText().equals("") || labelSaveArchivo.getText().equals(System.getProperty("user.home"))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Campo Vacio");
                alert.setHeaderText("Verifica la ruta para guardar el Documento");
                alert.setContentText("al parecer no ha Seleccionado ningun ruta de Guardado!");
                alert.showAndWait();
            } else {
                fileSelect.setInitialDirectory(new File(labelSaveArchivo.getText()));
                fileSelect.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Filtros", filter));
                if (RadioUnir_pdf.isSelected()) {
                    fileList = fileSelect.showOpenMultipleDialog(primaryStage);
                    labelFilePath.setText("Varios Archivos Seleccionado");

                } else {

                    file = fileSelect.showOpenDialog(primaryStage);
                    String nameFile = file.getName();
                    labelFilePath.setText(nameFile);
                }


            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Tenemos un error");
            alert.setHeaderText("Verifica tus Datos");
            alert.setContentText("al parecer no ha Seleccionado ningun tipo de archivo!");
            alert.showAndWait();
        }


    }
    public void btnGenerarAction(ActionEvent actionEvent) {
        Task<Void> tarea = crearTareaDemorada();
        new Thread(tarea).start();
    }
    public void btnSaveArchivoPressed(ActionEvent actionEvent) {
        primaryStage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecciona una carpeta");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home"))); // Directorio inicial
        try {
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                labelSaveArchivo.setText(selectedDirectory.getAbsolutePath());
            }
        } catch (Exception e) {

        }

    }
    public void RadioSeparar_pdfAction(MouseEvent mouseEvent) {
        filtro.setVisible(true);
        textFiltroHojas.setVisible(true);
    }
    public void filtroAction(ActionEvent actionEvent) {

            if (!filtro.isSelected()){
                textFiltroHojas.setDisable(true);
            }else{
                textFiltroHojas.setDisable(false);
            }


    }
    @FXML
    public void filtrohojasKeyType(KeyEvent keyEvent) {
        char key = keyEvent.getCharacter().charAt(0);
        if(!Character.isDigit(key)){
            keyEvent.consume();
        }


    }
    private void startProcess() {
        if (file != null || fileList != null) {
            if (Radiojpg_pdf.isSelected()) {
                String save = labelSaveArchivo.getText() + "\\" + FilterExt(file) + "_Geko.pdf";
                if (save != null) {
                    System.out.println("guardado en: " + save);
                    ConvertImageToPdf(file.getAbsolutePath(), save, comboBoxHoja, comboBoxOrientacion);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Muy Bien");
                    alert.setHeaderText("Documento Guardado en: ");
                    alert.setContentText("Ruta: " + save);
                    alert.showAndWait();
                }
            }
            if (RadioPng_pdf.isSelected()) {

                String save = labelSaveArchivo.getText() + "\\" + FilterExt(file) + "_Geko.pdf";
                if (save != null) {
                    System.out.println("guardado en: " + save);
                    ConvertImageToPdf(file.getAbsolutePath(), save, comboBoxHoja, comboBoxOrientacion);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Muy Bien");
                    alert.setHeaderText("Documento Guardado en: ");
                    alert.setContentText("Ruta: " + save);
                    alert.showAndWait();
                }
            }
            if (RadioSeparar_pdf.isSelected()) {

                String save = labelSaveArchivo.getText();
                if (save != null) {
                    List<Integer> paginas = Validations.StringToInteger(textFiltroHojas.getText());
                    if (filtro.isSelected() && paginas.isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Tenemos un error");
                        alert.setHeaderText("Verifica los datos de las paginas o desactiva el filtro");
                        alert.setContentText("al parecer no ha hecho nigun filtro!");
                        alert.showAndWait();
                    }else {
                        splitPdf(file.getAbsolutePath(), save, filtro.isSelected(), paginas);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Muy Bien");
                        alert.setHeaderText("Documento Guardado en: ");
                        alert.setContentText("Ruta: " + save);
                        alert.showAndWait();
                    }
                }
            }
            if (RadioUnir_pdf.isSelected()) {
                List<String> listPathPdf = new ArrayList<>();
                String save = labelSaveArchivo.getText();
                if (save != null) {
                    for (File path : fileList) {
                        listPathPdf.add(path.getAbsolutePath());
                    }
                    System.out.println("guardado en: " + save);
                    mergePdf(listPathPdf, save);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Muy Bien");
                    alert.setHeaderText("Documento Guardado en: ");
                    alert.setContentText("Ruta: " + save);
                    alert.showAndWait();
                }
            }
            if (Radiopdf_word.isSelected()) {
                Alert alertI = new Alert(Alert.AlertType.CONFIRMATION);
                alertI.setHeaderText(null);
                alertI.setTitle("Validacion");
                alertI.setHeaderText("Nota de aclaracion: ¿Estas de Acuerdo?");
                alertI.setContentText("Solo se copiara la informacion textual en el documento , los formatos e imagenes no se copiaran en el word.");
                Optional<ButtonType> action = alertI.showAndWait();

                if (action.get() == ButtonType.OK){
                    String save = labelSaveArchivo.getText()+ "\\" +"Geko_pdf_word.docx";
                    if (save != null) {

                        if(PDFToWordConverter(file.getAbsolutePath(), save)){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Muy Bien");
                            alert.setHeaderText("Documento Guardado en: ");
                            alert.setContentText("Ruta: " + save);
                            alert.showAndWait();
                        }

                    }
                }

            }
            if (Radioword_pdf.isSelected()) {
                Alert alertI = new Alert(Alert.AlertType.CONFIRMATION);
                alertI.setHeaderText(null);
                alertI.setTitle("Validacion");
                alertI.setHeaderText("¿Estas de Seguro?");
                Optional<ButtonType> action = alertI.showAndWait();

                if (action.get() == ButtonType.OK) {
                    String pathoutput = labelSaveArchivo.getText() + "\\" + "Geko_word_pdf.pdf";
                    if (pathoutput != null) {
                        File archivo = new File("CovertWordPdf.py");
                        if (!archivo.exists()) {
                            System.out.println("OJO: ¡¡No existe el archivo de Python!!");
                        }else{
                            System.out.println("patch file"+ archivo.getAbsoluteFile().getAbsolutePath());
                            if (WordToPDFConverter(file.getAbsolutePath(), pathoutput, Opcion.WORD_APLICATION, archivo.getAbsoluteFile().getAbsolutePath())) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Muy Bien");
                                alert.setHeaderText("Documento Guardado en: ");
                                alert.setContentText("Ruta: " + pathoutput);
                                alert.showAndWait();
                            }
                        }
                    }
                }

            }
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Tenemos un error");
            alert.setHeaderText("Verifica el archivo");
            alert.setContentText("al parecer no ha Seleccionado ningun archivo!");
            alert.showAndWait();
        }

    }
    private Task<Void> crearTareaDemorada() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Simular un proceso demorado

                    // Actualizar la interfaz gráfica desde el hilo de JavaFX
                    Platform.runLater(() -> {
                        progreesBar.setVisible(true);
                        startProcess();
                    });

                // Actualizar la interfaz gráfica después de completar el proceso
                Platform.runLater(() -> {
                    labelFilePath.setText("Seleccione un Archivo");
                    Radiojpg_pdf.selectedProperty().set(false);
                    RadioPng_pdf.selectedProperty().set(false);
                    RadioSeparar_pdf.selectedProperty().set(false);
                    RadioUnir_pdf.selectedProperty().set(false);
                    //labelSaveArchivo.setText(System.getProperty("user.home"));
                    progreesBar.setVisible(false);
                    System.out.println("Proceso completado");
                });

                return null;
            }
        };
    }
    public void Radiojpg_pdfOnAcction(ActionEvent actionEvent) {
        dividirhabilitar();
    }

    public void RadioUnir_PdfOnAcction(ActionEvent actionEvent) {
        dividirhabilitar();
    }

    public void RadioPng_pdfOnAcction(ActionEvent actionEvent) {
        dividirhabilitar();
    }

    public void Radiopdf_wordOnAction(ActionEvent actionEvent) {
        dividirhabilitar();
    }

    public void Radioword_pdfOnAction(ActionEvent actionEvent) {
        dividirhabilitar();
    }

    private void dividirhabilitar(){
        filtro.setVisible(false);
        textFiltroHojas.setVisible(false);
    }

}