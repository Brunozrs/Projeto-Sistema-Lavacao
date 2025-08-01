package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroClienteDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private Group gbTipo;


    @FXML
    private TextField tfCPF;

    @FXML
    private TextField tfCNPJ;

    @FXML
    private TextField tfCelular;

    @FXML
    private DatePicker dpCadastro;

    @FXML
    private DatePicker dpDataNasc;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfIE;

    @FXML
    private TextField tfNome;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Cliente cliente;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Cliente getCliente() {
        return cliente;
    }

//    public void trocarInputs(){
//
//    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente.getId() != 0) {
            this.tfNome.setText(this.cliente.getNome());
            this.tfCelular.setText(this.cliente.getCelular());
            this.tfEmail.setText(this.cliente.getEmail());
            this.dpCadastro.setValue(this.cliente.getDataCadastro());
            this.gbTipo.setDisable(true);
            if (cliente instanceof PessoaFisica) {
                dpDataNasc.setDisable(false);
                tfCPF.setText(((PessoaFisica) this.cliente).getCpf());
                Date date = Date.valueOf(((PessoaFisica) this.cliente).getDataNascimento());
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dpDataNasc.setValue(localDate);
            } else {
                tfCNPJ.setText(((PessoaJuridica) this.cliente).getCnpj());
                tfIE.setText(((PessoaJuridica) this.cliente).getInscricaoEstadual());
            }
        }
        this.tfNome.requestFocus();
    }

    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            cliente.setNome(tfNome.getText());
            cliente.setCelular(tfCelular.getText());
            cliente.setEmail(tfEmail.getText());
            cliente.setDataCadastro(dpCadastro.getValue());
            if (cliente instanceof PessoaFisica) {
                ((PessoaFisica) cliente).setCpf(tfCPF.getText());
                Date date = Date.valueOf(((PessoaFisica) this.cliente).getDataNascimento());
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dpDataNasc.setValue(localDate);
            } else {
                ((PessoaJuridica) cliente).setCnpj(tfCNPJ.getText());
                ((PessoaJuridica) cliente).setInscricaoEstadual(tfIE.getText());
            }
            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }

    @FXML
    public void handleRbPessoaFisica() {
//        this.tfPais.setText("BRASIL");
//        this.tfPais.setDisable(true);
    }

    @FXML
    public void handleRbPessoaJuridica() {
//        this.tfPais.setText("");
//        this.tfPais.setDisable(false);
    }

    //método para validar a entrada de dados
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfNome.getText() == null || this.tfNome.getText().length() == 0) {
            errorMessage += "Nome inválido.\n";
        }

        if (this.tfCelular.getText() == null || this.tfCelular.getText().length() == 0) {
            errorMessage += "Telefone inválido.\n";
        }

        if (this.tfEmail.getText() == null || this.tfEmail.getText().length() == 0 || !this.tfEmail.getText().contains("@")) {
            errorMessage += "Email inválido.\n";
        }

        if (this.dpCadastro.getValue() == null) {
            errorMessage += "Data de cadastro inválido.\n";
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            //exibindo uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}