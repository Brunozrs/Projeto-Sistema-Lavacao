package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Servico;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.text.DecimalFormat;
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


/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroServicoController implements Initializable {

    @FXML
    private TableColumn<Servico, String> tableColumnServicoDescricao;

    @FXML
    private TableView<Servico> tableViewServicos;

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbCategoria;

    @FXML
    private Label lbPontos;

    @FXML
    private Label lbServicoDescricao;

    @FXML
    private Label lbServicoId;

    @FXML
    private Label lbValor;

    private List<Servico> listaServicos;
    private ObservableList<Servico> observableListServicos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicoDAO.setConnection(connection);

        carregarTableView();

        tableViewServicos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));

    }

    public void carregarTableView() {
        tableColumnServicoDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        listaServicos = servicoDAO.listar();

        observableListServicos = FXCollections.observableArrayList(listaServicos);
        tableViewServicos.setItems(observableListServicos);
    }

    public void selecionarItemTableView(Servico servico) {
        if (servico != null) {
            servico = servicoDAO.buscar(servico);
            lbServicoId.setText(Integer.toString(servico.getId()));
            lbServicoDescricao.setText(servico.getDescricao());
            lbValor.setText(Double.toString(servico.getValor()));
            lbPontos.setText(Integer.toString(servico.getPontos()));
            lbCategoria.setText(servico.getCategoria().name());
        } else {
            lbServicoId.setText("");
            lbServicoDescricao.setText("");
            lbValor.setText("");
            lbPontos.setText("");
            lbCategoria.setText("");
        }
    }


    @FXML
    public void handleBtInserir() throws IOException {
        Servico servico = new Servico();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosServicosDialog(servico);
        if (buttonConfirmarClicked) {
            servicoDAO.inserir(servico);
            carregarTableView();
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Servico servico = tableViewServicos.getSelectionModel().getSelectedItem();
        servico = servicoDAO.buscar(servico);
        if (servico != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosServicosDialog(servico);
            if (buttonConfirmarClicked) {
                servicoDAO.alterar(servico);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um servico na Tabela.");
            alert.show();
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException {
        Servico servico = tableViewServicos.getSelectionModel().getSelectedItem();
        if (servico != null) {
            servicoDAO.remover(servico);
            carregarTableView();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um servico na Tabela.");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneCadastrosServicosDialog(Servico servico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroServicoDialogController.class.getResource(
                "/view/FXMLAnchorPaneCadastroServicoDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de servicos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o servico ao controller
        FXMLAnchorPaneCadastroServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setServico(servico);

        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }


}