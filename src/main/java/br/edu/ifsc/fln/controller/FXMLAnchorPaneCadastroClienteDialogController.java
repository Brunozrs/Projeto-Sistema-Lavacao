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
    private RadioButton rbPessoaFisica;

    @FXML
    private RadioButton rbPessoaJuridica;

    @FXML
    private TextField tfCPFouCNPJ;

    @FXML
    private TextField tfCelular;

    @FXML
    private DatePicker dpCadastro;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfIEouDataNasc;

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
                tfIEouDataNasc.setDisable(false);
                rbPessoaFisica.setSelected(true);
                tfCPFouCNPJ.setText(((PessoaFisica) this.cliente).getCpf());
                tfIEouDataNasc.setText(((PessoaFisica) this.cliente).getDataNascimento());
            } else {
                rbPessoaJuridica.setSelected(true);
                tfCPFouCNPJ.setText(((PessoaJuridica) this.cliente).getCnpj());
                tfIEouDataNasc.setText(((PessoaJuridica) this.cliente).getInscricaoEstadual());
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
            if (rbPessoaFisica.isSelected()) {
                ((PessoaFisica) cliente).setCpf(tfCPFouCNPJ.getText());
                ((PessoaFisica) cliente).setDataNascimento(tfIEouDataNasc.getText());
            } else {
                ((PessoaJuridica) cliente).setCnpj(tfCPFouCNPJ.getText());
                ((PessoaJuridica) cliente).setInscricaoEstadual(tfIEouDataNasc.getText());
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

        if (rbPessoaFisica.isSelected()) {
            if (this.tfCPFouCNPJ.getText() == null || this.tfCPFouCNPJ.getText().length() == 0) {
                errorMessage += "CPF inválido.\n";
            }

            if (this.tfIEouDataNasc.getText() == null || this.tfIEouDataNasc.getText().length() == 0) {
                errorMessage += "Data de nascimento inválido.\n";
            }
        } else {
            if (this.tfCPFouCNPJ.getText() == null || this.tfCPFouCNPJ.getText().length() == 0) {
                errorMessage += "CNPJ inválido.\n";
            }
            if (this.tfIEouDataNasc.getText() == null || this.tfIEouDataNasc.getText().length() == 0) {
                errorMessage += "Inscrição estadual inválida.\n";
            }
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