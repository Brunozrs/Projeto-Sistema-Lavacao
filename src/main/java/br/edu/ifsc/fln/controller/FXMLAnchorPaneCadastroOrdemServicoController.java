package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ItemOSDAO;
import br.edu.ifsc.fln.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroOrdemServicoController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbAgenda;

    @FXML
    private Label lbDesconto;

    @FXML
    private Label lbNumero;

    @FXML
    private Label lbStatus;

    @FXML
    private Label lbTotal;

    @FXML
    private Label lbVeiculo;

    @FXML
    private TableColumn<OrdemServico, String> tableColumnNumeroDaOrdem;

    @FXML
    private TableColumn<OrdemServico, String> tableColumnTotalDaOrdem;

    @FXML
    private TableColumn<OrdemServico, String> tableColumnVeiculoDaOrdem;

    @FXML
    private TableView<OrdemServico> tableViewOrdemDeServico;

    private List<OrdemServico> listaOrdemServicos;
    private ObservableList<OrdemServico> observableListOrdemServicos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ordemServicoDAO.setConnection(connection);

        carregarTableView();

        tableViewOrdemDeServico.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }

    public void carregarTableView() {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        tableColumnNumeroDaOrdem.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tableColumnVeiculoDaOrdem.setCellValueFactory(new PropertyValueFactory<>("veiculo"));
        tableColumnTotalDaOrdem.setCellValueFactory(new PropertyValueFactory<>("total"));

        listaOrdemServicos = ordemServicoDAO.listar();

        observableListOrdemServicos = FXCollections.observableArrayList(listaOrdemServicos);
        tableViewOrdemDeServico.setItems(observableListOrdemServicos);
    }

    public void selecionarItemTableView(OrdemServico ordemServico) {
        if (ordemServico != null) {
            lbNumero.setText(Long.toString(ordemServico.getNumero()));
            lbAgenda.setText((ordemServico.getAgenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            lbTotal.setText(String.format("%.2f", ordemServico.getTotal()));
            lbDesconto.setText((ordemServico.getDesconto()) + "%");
            lbStatus.setText(ordemServico.getStatus().name());
            lbVeiculo.setText(ordemServico.getVeiculo().getPlaca());
        } else {
            lbNumero.setText("");
            lbAgenda.setText("");
            lbTotal.setText("");
            lbDesconto.setText("");
            lbStatus.setText("");
            lbVeiculo.setText("");
        }
    }

    @FXML
    private void handleBtInserir(ActionEvent event) throws IOException, SQLException {
        OrdemServico ordemServico = new OrdemServico();
        List<ItemOS> itensOS = new ArrayList<>();
        ordemServico.setItemOS(itensOS);
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroOrdemServicoDialog(ordemServico);
        if (buttonConfirmarClicked) {
            ordemServicoDAO.setConnection(connection);
            ordemServicoDAO.inserir(ordemServico);
            carregarTableView();
        }
    }

    @FXML
    private void handleBtAlterar(ActionEvent event) throws IOException {
        OrdemServico ordemServico = tableViewOrdemDeServico.getSelectionModel().getSelectedItem();
        if (ordemServico != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroOrdemServicoDialog(ordemServico);
            if (buttonConfirmarClicked) {
                ordemServicoDAO.alterar(ordemServico);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um ordemServico na Tabela.");
            alert.show();
        }
    }

    @FXML
    private void handleBtExcluir(ActionEvent event) throws SQLException {
        OrdemServico ordemServico = tableViewOrdemDeServico.getSelectionModel().getSelectedItem();
        if (ordemServico != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir a ordem de Servico " + ordemServico.getNumero())) {
                ordemServicoDAO.setConnection(connection);
                ordemServicoDAO.remover(ordemServico);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Por favor, escolha uma ordem de Servico na tabela!");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneCadastroOrdemServicoDialog(OrdemServico ordemServico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroOrdemServicoController.class.getResource(
                "/view/FXMLAnchorPaneCadastroOrdemServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de ordemServicos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o ordemServico ao controller
        FXMLAnchorPaneCadastroOrdemServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setOrdemServico(ordemServico);

        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }


}