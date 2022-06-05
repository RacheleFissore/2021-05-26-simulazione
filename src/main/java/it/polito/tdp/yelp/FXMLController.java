/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	private boolean entrato = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnLocaleMigliore"
    private Button btnLocaleMigliore; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	double soglia;
    	try {
			soglia = Double.parseDouble(txtX.getText());
		} catch (NumberFormatException e) {
			txtResult.appendText("Inserire valore numerico");
			return;
		}
    	
    	if(soglia >= 0 && soglia <= 1 && cmbLocale.getValue() != null && entrato) {
    		List<Business> businesses = model.calcolaPercorso(cmbLocale.getValue(), soglia);
    		if(businesses == null) {
    			txtResult.setText("Non è presente un percorso");
    		}
    		else {
    			for(Business bus : businesses) {
        			txtResult.appendText(bus + "\n");
        		}
    		}
    		
    	}
    	else {
    		txtResult.setText("Inserire combo e rispettare la soglia numerica tra 0 e 1");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	entrato = true;
    	
    	if(cmbCitta.getValue() != null && cmbAnno.getValue() != null) {
    		model.creaGrafo(cmbCitta.getValue(), cmbAnno.getValue());
    		cmbLocale.getItems().addAll(model.getBusinesses());
    		txtResult.clear();
    		txtResult.setText("GRAFO CREATO\n# vertici: " + model.numVertici() + "\n# archi: " + model.numArchi());
    	}
    	else {
    		txtResult.setText("Inserire entrambi i valori delle combobox");
    	}
    	
    }

    @FXML
    void doLocaleMigliore(ActionEvent event) {
    	txtResult.clear();
    	if(entrato) {
    		String s = model.calcolaMigliore();
        	txtResult.clear();
        	txtResult.setText("LOCALE MIGLIORE: " + s);
    	}
    	else {
    		txtResult.setText("Creare prima il grafo!");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnLocaleMigliore != null : "fx:id=\"btnLocaleMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	cmbCitta.getItems().addAll(model.getCitta());
    	cmbAnno.getItems().addAll(model.getAnno());
    }
}
