package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Modelo;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroModeloController implements Initializable {
    @FXML
    private Label lbModeloId;

    @FXML
    private Label lbModeloDesc;

    @FXML
    private Label lbModeloCat;

    @FXML
    private Label lbModeloMarca;

    @FXML
    private Label lbMotorPot;

    @FXML
    private Label lbMotorTC;

    @FXML
    private Button btInserir;

    @FXML
    private Button btAlterar;

    @FXML
    private Button btRemover;

    @FXML
    private TableView<Modelo> tableViewModelos;

    @FXML
    private TableColumn<Modelo, String> tableColumnDescricao;


    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modeloDAO.setConnection(connection);

        carregarTableViewModelo();

        tableViewModelos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));

    }

    public void carregarTableViewModelo() {
        tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));


        listaModelos = modeloDAO.listar();

        observableListModelos = FXCollections.observableArrayList(listaModelos);
        tableViewModelos.setItems(observableListModelos);
    }

    public void selecionarItemTableView(Modelo modelo) {
        if (modelo != null) {
            lbModeloId.setText(String.valueOf(modelo.getId()));
            lbModeloDesc.setText(modelo.getDescricao());
            lbModeloCat.setText(modelo.getCategoria().getDescricao());
            lbModeloMarca.setText(modelo.getMarca().getNome());
            lbMotorPot.setText(String.valueOf(modelo.getMotor().getPotencia()));
            lbMotorTC.setText(modelo.getMotor().getTipoCombustivel().getDescricao());
        } else {
            lbModeloId.setText("");
            lbModeloDesc.setText("");
            lbModeloCat.setText("");
            lbModeloMarca.setText("");
        }
    }


    @FXML
    public void handleBtInserir() throws IOException {
        Modelo modelo = new Modelo();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosModelosDialog(modelo);
        if (buttonConfirmarClicked) {
            modeloDAO.inserir(modelo);
            carregarTableViewModelo();
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Modelo modelo = tableViewModelos.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosModelosDialog(modelo);
            if (buttonConfirmarClicked) {
                modeloDAO.alterar(modelo);
                carregarTableViewModelo();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um modelo na Tabela.");
            alert.show();
        }
    }

    @FXML
    public void handleBtRemover() throws IOException {
        Modelo modelo = tableViewModelos.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            modeloDAO.remover(modelo);
            carregarTableViewModelo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um modelo na Tabela.");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneCadastrosModelosDialog(Modelo modelo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroModeloController.class.getResource("/view/FXMLAnchorPaneCadastroModeloDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de modelos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o modelo ao controller
        FXMLAnchorPaneCadastroModeloDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setModelo(modelo);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }


}
