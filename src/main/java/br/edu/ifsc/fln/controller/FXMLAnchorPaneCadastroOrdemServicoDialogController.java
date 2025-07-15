package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.*;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.*;
        import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroOrdemServicoDialogController implements Initializable {

    @FXML
    private Button btAdicionar;

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private MenuItem contextMenuItemRemoverItem;

    @FXML
    private ContextMenu contextMenuTableView;

    @FXML
    private ComboBox<Servico> cbServico;

    @FXML
    private ChoiceBox<EStatus> cbStatus;

    @FXML
    private ComboBox<Veiculo> cbVeiculo;

    @FXML
    private DatePicker dpData;

    @FXML
    private TableColumn<ItemOS, Servico> tableColumnServico;

    @FXML
    private TableColumn<ItemOS, Double> tableColumnValorServico;

    @FXML
    private TableColumn<ItemOS, String> tableColumnObservacoesServico;

    @FXML
    private TableView<ItemOS> tableViewItemsOS;

    @FXML
    private TextField tfCliente;

    @FXML
    private TextField tfModelo;

    @FXML
    private TextField tfMarca;

    @FXML
    private TextField tfCategoria;

    @FXML
    private TextField tfDesconto;

    @FXML
    private TextField tfValor;

    @FXML
    private TextField tfValorServico;

    @FXML
    private TextField tfObservacoesServico;

    private List<Veiculo> listaVeiculos;
    private List<Servico> listaServicos;

    private ObservableList<Veiculo> observableListVeiculos;
    private ObservableList<Servico> observableListServicos;
    private ObservableList<ItemOS> observableListItemsOS;


    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    private final ItemOSDAO itemOSDAO = new ItemOSDAO();


    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private OrdemServico ordemServico;
    private Veiculo veiculo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);
        servicoDAO.setConnection(connection);
        carregarComboBoxVeiculos();
        carregarComboBoxServicos();
        carregarChoiceBoxSituacao();
        setFocusLostHandle();
        // Procurar no objeto itemOS os valores de servico e valorServico
        tableColumnServico.setCellValueFactory(new PropertyValueFactory<>("servico"));
        tableColumnValorServico.setCellValueFactory(new PropertyValueFactory<>("valorServico"));
        tableColumnObservacoesServico.setCellValueFactory(new PropertyValueFactory<>("Observacoes"));

        contextMenuTableView.getItems().addAll(contextMenuItemRemoverItem);

        // Aplica o ContextMenu à TableView
        tableViewItemsOS.setContextMenu(contextMenuTableView);


        // Permite seleção de linha
        tableViewItemsOS.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    public void carregarComboBoxVeiculos() {
        listaVeiculos = veiculoDAO.listar();
        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        cbVeiculo.setItems(observableListVeiculos);
    }

    public void carregarComboBoxServicos() {
        listaServicos = servicoDAO.listar();
        observableListServicos =
                FXCollections.observableArrayList(listaServicos);
        cbServico.setItems(observableListServicos);
    }

    public void carregarChoiceBoxSituacao() {
        cbStatus.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                    event.consume();
                }
            }
        });
        cbStatus.setItems( FXCollections.observableArrayList( EStatus.values()));
    }

    private void setFocusLostHandle() {
        tfDesconto.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { // focus lost
                if (tfDesconto.getText() != null && !tfDesconto.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    ordemServico.setDesconto(Double.parseDouble(tfDesconto.getText()));
                    tfValor.setText(String.format("%.2f", ordemServico.getTotal()));
                }
            }
        });
    }

    /**
     * @return the dialogStage
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmarClicked
     */
    public boolean isBtConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setBtConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    /**
     * @return the ordemServico
     */
    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    /**
     * @param ordemServico the ordemServico to set
     */
    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
        if (ordemServico.getNumero() != 0) {
            cbVeiculo.getSelectionModel().select(this.ordemServico.getVeiculo());
            tfCliente.setText(this.ordemServico.getVeiculo().getCliente().getNome());
            tfModelo.setText(this.ordemServico.getVeiculo().getModelo().getDescricao());
            tfMarca.setText(this.ordemServico.getVeiculo().getModelo().getMarca().getNome());
            tfCategoria.setText(this.ordemServico.getVeiculo().getModelo().getCategoria().getDescricao());
            dpData.setValue(this.ordemServico.getAgenda());
            observableListItemsOS = FXCollections.observableArrayList(this.ordemServico.getItemOS());
            tableViewItemsOS.setItems(observableListItemsOS);
            tfDesconto.setText(String.format("%.2f", this.ordemServico.getDesconto()));
            tfValor.setText(String.format("%.2f", this.ordemServico.getTotal()));
            cbStatus.getSelectionModel().select(this.ordemServico.getStatus());
        }
    }

    @FXML
    void handleBtPesquisarDados(){
        if(cbVeiculo != null){
            tfCliente.setText(cbVeiculo.getSelectionModel().getSelectedItem().getCliente().getNome());
            tfModelo.setText(cbVeiculo.getSelectionModel().getSelectedItem().getModelo().getDescricao());
            tfMarca.setText(cbVeiculo.getSelectionModel().getSelectedItem().getModelo().getMarca().getNome());
            tfCategoria.setText(cbVeiculo.getSelectionModel().getSelectedItem().getModelo().getCategoria().getDescricao());
        }
    }



    @FXML
    void handleBtAdicionar() {
        itemOSDAO.setConnection(connection);
        ItemOS itemOS = new ItemOS(Double.parseDouble(tfValorServico.getText()),tfObservacoesServico.getText(),cbServico.getSelectionModel().getSelectedItem(),this.ordemServico);
        String sql = "INSERT INTO itemos(valor_do_servico, observacao, id_servico, id_os) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, itemOS.getValorServico());
            stmt.setString(2, itemOS.getObservacoes());
            stmt.setInt(3, itemOS.getServico().getId());
            stmt.setLong(4, itemOS.getOrdemServico().getNumero());
            stmt.execute();

        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);

        }
            List <ItemOS> listaItemOS = this.ordemServico.getItemOS();
            listaItemOS.add(itemOS);
            this.ordemServico.setItemOS(listaItemOS);

            observableListItemsOS.add(itemOS);

            ordemServico.calcularServico();
            tfValor.setText(String.format("%.2f", ordemServico.getTotal()));



    }

    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            ordemServico.setVeiculo(cbVeiculo.getSelectionModel().getSelectedItem());
            ordemServico.setAgenda(dpData.getValue());
            ordemServico.setStatus(cbStatus.getSelectionModel().getSelectedItem());
            ordemServico.setDesconto(Double.parseDouble(tfDesconto.getText()));
            String valorTexto = tfValor.getText().replace(",", ".");
            double valorTotal = Double.parseDouble(valorTexto);
            ordemServico.setTotal(valorTotal);
            buttonConfirmarClicked = true;
            dialogStage.close();
        }

    }

    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }

    @FXML
    void handleTableViewMouseClicked(MouseEvent event) {

        ItemOS itemOS = tableViewItemsOS.getSelectionModel().getSelectedItem();
        if (itemOS == null) {
            contextMenuItemRemoverItem.setDisable(true);
        } else {
            contextMenuItemRemoverItem.setDisable(false);
        }

    }


    @FXML
    void excluirItemSelecionado() {
        ItemOS itemSelecionado = tableViewItemsOS.getSelectionModel().getSelectedItem();

        if (itemSelecionado != null) {
            try {
                // Remove do banco de dados
                String sql = "DELETE FROM itemos WHERE id_servico = ?"; // Pega o id do serviço
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, itemSelecionado.getServico().getId());
                stmt.execute();

                // Remove da TableView e da lista
                observableListItemsOS.remove(itemSelecionado);
                ordemServico.getItemOS().remove(itemSelecionado);


                // Atualiza o total
                ordemServico.calcularServico();
                tfValor.setText(String.format("%.2f", ordemServico.getTotal()));

            } catch (SQLException ex) {
                Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Falha ao excluir item");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum item selecionado");
            alert.setContentText("Selecione um item para excluir.");
            alert.showAndWait();
        }
    }

//    private String inputDialog(int value) {
//        TextInputDialog dialog = new TextInputDialog(Integer.toString(value));
//        dialog.setTitle("Entrada de dados.");
//        dialog.setHeaderText("Atualização da quantidade de produtos.");
//        dialog.setContentText("Quantidade: ");
//
//        // Traditional way to get the response value.
//        Optional<String> result = dialog.showAndWait();
//        return result.get();
//    }

    @FXML
    private void handleContextMenuItemRemoverItem() {
        ItemOS itemOS = tableViewItemsOS.getSelectionModel().getSelectedItem();
        int index = tableViewItemsOS.getSelectionModel().getSelectedIndex();
        try {
            ordemServico.getItemOS().remove(index);
            ordemServico.remove(itemOS);


            ordemServico.calcularServico();
            tfValor.setText(String.format("%.2f", ordemServico.getTotal()));
         }
        catch (Exception ex) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText("Nenhum item selecionado");
        alert.setContentText("Selecione um item para excluir.");
        alert.showAndWait();
        }
    }

    //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (cbVeiculo.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Veiculo inválido!\n";
        }

        if (dpData.getValue() == null) {
            errorMessage += "Data inválida!\n";
        }

        if (observableListItemsOS == null) {
            errorMessage += "Itens da ordem de serviço inválidos!\n";
        }

        DecimalFormat df = new DecimalFormat("0.00");
        try {
            tfDesconto.setText(df.parse(tfDesconto.getText()).toString());
        } catch (ParseException ex) {
            errorMessage += "A taxa de desconto está incorreta! Use \",\" como ponto decimal.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}