package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Motor;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastroModeloDialogController implements Initializable {

    @FXML
    private TextField tfDescricao;

    @FXML
    private ChoiceBox<ECategoria> cbCategoria;

    @FXML
    private ChoiceBox<Marca> cbMarca;

    @FXML
    private ChoiceBox<ETipoCombustivel> cbTipoCombustivel;

    @FXML
    private Spinner<Integer> spPotencia;

    @FXML
    private Button btConfirmar;

    @FXML
    private Button btCancelar;

    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Modelo modelo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,3000,600,50);
        spPotencia.setValueFactory(valueFactory);

        marcaDAO.setConnection(connection);

        carregarChoiceBoxMarcas();
        carregarChoiceBoxCategorias();
        carregarChoiceBoxTipoCombustivel();
        setFocusLostHandle();
    }

    private void setFocusLostHandle() {
        tfDescricao.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!newV) { // focus lost
                if (tfDescricao.getText() == null || tfDescricao.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    tfDescricao.requestFocus();
                }
            }
        });
    }

    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas;

    public void carregarChoiceBoxMarcas() {
        listaMarcas = marcaDAO.listar();
        observableListMarcas =
                FXCollections.observableArrayList(listaMarcas);
        cbMarca.setItems(observableListMarcas);
    }


    public void carregarChoiceBoxCategorias() {
        cbCategoria.getItems().setAll(ECategoria.values());
    }

    public void carregarChoiceBoxTipoCombustivel() {
        cbTipoCombustivel.getItems().setAll(ETipoCombustivel.values());
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
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    /**
     * @return the modelo
     */
    public Modelo getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        tfDescricao.setText(modelo.getDescricao());
        cbMarca.getSelectionModel().select(modelo.getMarca());
        cbCategoria.getSelectionModel().select(modelo.getCategoria());
        spPotencia.getValueFactory().setValue(modelo.getMotor().getPotencia());
        cbTipoCombustivel.getSelectionModel().select(modelo.getMotor().getTipoCombustivel());
    }

    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            modelo.setDescricao(tfDescricao.getText());
            modelo.setMarca( cbMarca.getSelectionModel().getSelectedItem());
            modelo.setCategoria( cbCategoria.getSelectionModel().getSelectedItem());
            modelo.getMotor().setPotencia(spPotencia.getValue());
            modelo.getMotor().setTipoCombustivel( cbTipoCombustivel.getSelectionModel().getSelectedItem());

            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }

    //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (tfDescricao.getText() == null || tfDescricao.getText().isEmpty()) {
            errorMessage += "Nome inválido!\n";
        }

        if (cbMarca.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Marca inválida!\n";
        }

        if (cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma categoria!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }


}
