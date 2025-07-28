/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.PercentageStringConverter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneRelatorioOrdemServicoController implements Initializable {


    @FXML
    private TableView<OrdemServico> tableView;
    @FXML
    private TableColumn<OrdemServico, Integer> tableColumnNumeroOs;
    @FXML
    private TableColumn<OrdemServico, Integer> tableColumnPlacaVeiculo;
    @FXML
    private TableColumn<OrdemServico, Date> tableColumnDataOS;
    @FXML
    private TableColumn<OrdemServico, Double> tableColumnDesconto;
    @FXML
    private TableColumn<OrdemServico, BigDecimal> tableColumnTotal;
    @FXML
    private Button buttonImprimir;

    private List<OrdemServico> listaOrdemServico;
    private ObservableList<OrdemServico> observableListOrdemservico;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final OrdemServicoDAO ordemservicoDAO = new OrdemServicoDAO();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ordemservicoDAO.setConnection(connection);
        carregarTableView();

    }

    public void carregarTableView(){
        tableColumnNumeroOs.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tableColumnPlacaVeiculo.setCellValueFactory(new PropertyValueFactory<>("veiculo"));
        tableColumnDataOS.setCellValueFactory(new PropertyValueFactory<>("agenda"));
        tableColumnDesconto.setCellValueFactory(new PropertyValueFactory<>("desconto"));
        tableColumnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        listaOrdemServico = ordemservicoDAO.listar();

        observableListOrdemservico = FXCollections.observableArrayList(listaOrdemServico);
        tableView.setItems(observableListOrdemservico);

    }



    //@FXML
    public void handleImprimir() throws JRException {
        URL url = getClass().getResource("/report/Relatoriolavacao.jasper");
        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(url);

        //null: caso não existam filtros
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);

        //false: não deixa fechar a aplicação principal
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setVisible(true);
    }

}