package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.domain.ECategoria;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroServicoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ChoiceBox<ECategoria> cbCategoria;

    @FXML
    private TextField tfDescricao;

    @FXML
    private TextField tfPontos;

    @FXML
    private TextField tfValor;

    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Servico servico;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarChoiceBoxCategoria();
    }

    public void carregarChoiceBoxCategoria() {
        cbCategoria.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                    event.consume();
                }
            }
        });
        cbCategoria.setItems( FXCollections.observableArrayList( ECategoria.values()));
        //cbCategoria.getSelectionModel().select(-1);
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

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        this.tfDescricao.setText(this.servico.getDescricao());
        this.tfValor.setText(Double.toString(this.servico.getValor()));
        this.tfPontos.setText(Integer.toString(this.servico.getPontos()));
        this.cbCategoria.getSelectionModel().select(this.servico.getCategoria());
    }


    @FXML
    public void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            servico.setDescricao(tfDescricao.getText());
            servico.setValor(Double.parseDouble(tfValor.getText()));
            servico.setPontos(Integer.parseInt(tfPontos.getText()));
            servico.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());

            btConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleBtCancelar() {
        dialogStage.close();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfDescricao.getText() == null || this.tfDescricao.getText().length() == 0) {
            errorMessage += "Descricao inválido.\n";
        }

        if (this.tfValor.getText() == null || this.tfValor.getText().length() == 0) {
            errorMessage += "Valor inválido.\n";
        }

        if (this.tfPontos.getText() == null || this.tfPontos.getText().length() == 0) {
            errorMessage += "Pontos inválido.\n";
        }

        if (cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma categoria para o servico!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}