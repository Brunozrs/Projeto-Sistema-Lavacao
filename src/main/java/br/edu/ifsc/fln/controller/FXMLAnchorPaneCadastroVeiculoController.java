package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Veiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastroVeiculoController implements Initializable {
    @FXML
    private Label lbId;

    @FXML
    private Label lbPlaca;

    @FXML
    private Label lbObservacoes;

    @FXML
    private Label lbCor;

    @FXML
    private Label lbModelo;

    @FXML
    private Label lbCliente;

    @FXML
    private Label lbTipo;

    @FXML
    private Button btInserir;

    @FXML
    private Button btAlterar;

    @FXML
    private Button btRemover;

    @FXML
    private TableView<Veiculo> tableViewVeiculos;

    @FXML
    private TableColumn<Veiculo, String> TableColumnPlaca;


    private List<Veiculo> listaVeiculos;
    private ObservableList<Veiculo> observableListVeiculos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);

        carregarTableViewVeiculo();

        tableViewVeiculos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));

    }

    public void carregarTableViewVeiculo() {
        TableColumnPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));


        listaVeiculos = veiculoDAO.listar();

        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        tableViewVeiculos.setItems(observableListVeiculos);
    }

    public void selecionarItemTableView(Veiculo veiculo) {
        if (veiculo != null) {
            lbId.setText(String.valueOf(veiculo.getId()));
            lbPlaca.setText(veiculo.getPlaca());
            lbObservacoes.setText(veiculo.getObservacoes());
            lbCor.setText(veiculo.getCor().getNome());
            lbModelo.setText(veiculo.getModelo().getDescricao());
            lbCliente.setText(veiculo.getCliente().getNome());
        } else {
            lbId.setText("");
            lbPlaca.setText("");
            lbObservacoes.setText("");
            lbCor.setText("");
            lbModelo.setText("");
            lbCliente.setText("");
        }
    }


    @FXML
    public void handleBtInserir() throws IOException {
        Veiculo veiculo = new Veiculo();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosVeiculosDialog(veiculo);
        if (buttonConfirmarClicked) {
            veiculo.getCliente().add(veiculo);
            veiculoDAO.inserir(veiculo);
            carregarTableViewVeiculo();
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Veiculo veiculo = tableViewVeiculos.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosVeiculosDialog(veiculo);
            if (buttonConfirmarClicked) {
                veiculoDAO.alterar(veiculo);
                carregarTableViewVeiculo();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um veiculo na Tabela.");
            alert.show();
        }
    }

    @FXML
    public void handleBtRemover() throws IOException {
        Veiculo veiculo = tableViewVeiculos.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            veiculo.getCliente().remove(veiculo);
            veiculoDAO.remover(veiculo);
            carregarTableViewVeiculo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um veiculo na Tabela.");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneCadastrosVeiculosDialog(Veiculo veiculo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroVeiculoController.class.getResource("/view/FXMLAnchorPaneCadastroVeiculoDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de veiculos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o veiculo ao controller
        FXMLAnchorPaneCadastroVeiculoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVeiculo(veiculo);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }


}
