/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
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

/**
 * FXML Controller class
 *
 * @author mpisc
 */
public class FXMLAnchorPaneCadastroClienteController implements Initializable {

    
    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbClienteId;

    @FXML
    private Label lbClienteDocumento;

    @FXML
    private Label lbClienteDataCadastro;

    @FXML
    private Label lbClienteNome;

    @FXML
    private Label lbClienteTelefone;

    @FXML
    private Label lbClienteEmail;

    @FXML
    private Label lbPontosCliente;

    @FXML
    private Label lbTipoCliente;
    
    @FXML
    private TableColumn<Cliente, String> tableColumnClienteTipo;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteNome;

    @FXML
    private TableView<Cliente> tableViewClientes;

    
    private List<Cliente> listaClientes;
    private ObservableList<Cliente> observableListClientes;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        carregarTableViewCliente();

        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));
    }
    
    public void carregarTableViewCliente() {
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnClienteTipo.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        listaClientes = clienteDAO.listar();
        
        observableListClientes = FXCollections.observableArrayList(listaClientes);
        tableViewClientes.setItems(observableListClientes);
    }
    
    public void selecionarItemTableViewClientes(Cliente cliente) {
        if (cliente != null) {
            lbClienteId.setText(String.valueOf(cliente.getId())); 
            lbClienteNome.setText(cliente.getNome());
            lbClienteTelefone.setText(cliente.getCelular());
            lbClienteEmail.setText(cliente.getEmail());
            lbClienteDataCadastro.setText(cliente.getDataCadastro().toString());
            lbPontosCliente.setText(String.valueOf(cliente.getPontuacao()));
            if(cliente instanceof PessoaFisica) {
                lbClienteDocumento.setText(((PessoaFisica) cliente).getCpf());
                lbTipoCliente.setText(" Pessoa Fisica");
            }
            else {
                lbClienteDocumento.setText(((PessoaJuridica) cliente).getCnpj());
                lbTipoCliente.setText(" Pessoa Juridica");
            }
        } else {
            lbClienteId.setText(""); 
            lbClienteNome.setText("");
            lbClienteDocumento.setText("");
            lbClienteTelefone.setText("");
            lbClienteEmail.setText("");
            lbClienteDataCadastro.setText("");
            lbPontosCliente.setText("");
        }
        
    }


    @FXML
    public void handleBtInserir() throws IOException {
      Cliente cliente = getTipoCliente();
      if (cliente != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
        if (btConfirmarClicked) {
            try {
                clienteDAO.inserir(cliente);
            }catch (DAOException e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Operação inválida");
                alert.setHeaderText("Não foi possivel inserir novo cliente");
                alert.setContentText("Ocorreu um erro ao inserir novo cliente");
                alert.showAndWait();
            }
            carregarTableViewCliente();
        }
      }
    }


    private Cliente getTipoCliente() {
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Pessoa Fisica");
        opcoes.add("Pessoa Juridica");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Cliente", opcoes);
        dialog.setTitle("Dialogo de Opções");
        dialog.setHeaderText("Escolha o tipo de Cliente");
        dialog.setContentText("Tipo de cliente: ");
        Optional<String> escolha = dialog.showAndWait();
        if (escolha.isPresent()) {
            if (escolha.get().equalsIgnoreCase("Pessoa Fisica"))
                return new PessoaFisica();
            else
                return new PessoaJuridica();
        } else {
            return null;
        }
    }


    @FXML 
    public void handleBtAlterar() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                try {
                    clienteDAO.alterar(cliente);
                }catch (DAOException e){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Operação inválida");
                    alert.setHeaderText("Não foi possivel alterar  cliente");
                    alert.setContentText("Ocorreu um erro ao alterar cliente");
                    alert.showAndWait();
                }
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Cliente na tabela ao lado");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtExcluir() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            try {
                clienteDAO.remover(cliente);
            }catch (DAOException e){
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.setTitle("Operação inválida");
               alert.setHeaderText("Não foi possivel remover  cliente");
               alert.setContentText("Ocorreu um erro ao remover cliente");
               alert.showAndWait();
            }
            carregarTableViewCliente();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cliente na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroClienteDialog(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        //  verifica se cliente é pessoa fisica (check ternário )
        String tfDialog = cliente instanceof PessoaFisica ? "/view/FXMLAnchorPaneCadastroClientePFDialog.fxml": "/view/FXMLAnchorPaneCadastroClientePJDialog.fxml";
        // insere o conteudo selecionado no loader
        loader.setLocation(FXMLAnchorPaneCadastroClienteController.class.getResource(tfDialog));
        AnchorPane page = (AnchorPane) loader.load();


        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cliente");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o objeto cliente para o controller
        FXMLAnchorPaneCadastroClienteDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();



    }
}
