package it.polito.tdp.seriea;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.SeasonForTeam;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> boxSquadra;

    @FXML
    private Button btnSelezionaSquadra;

    @FXML
    private Button btnTrovaAnnataOro;

    @FXML
    private Button btnTrovaCamminoVirtuoso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSelezionaSquadra(ActionEvent event) {
    	txtResult.clear();
    	String team = this.boxSquadra.getValue();
    	if(team == null) {
    		txtResult.setText("Errore: selezionare una squadra per poter procedere con l'analisi.\n");
    		return;
    	}
    	List<SeasonForTeam> seasonsForTeam = this.model.selectedTeam(team);
    	if(seasonsForTeam.isEmpty()) {
    		txtResult.setText("Non esistono stagioni per la squadra selezionata.\n");
    		return;
    	}
    	txtResult.appendText("Risultati della squadra " + team + ":\n\n");
    	for(SeasonForTeam sft : seasonsForTeam) {
    		txtResult.appendText(String.format("Stagione %d - %d punti\n", sft.getSeason(), sft.getPoints()));
    	}
    	this.btnTrovaAnnataOro.setDisable(false);
    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {
    	txtResult.clear();
    	String team = this.boxSquadra.getValue();
    	if(team == null) {
    		txtResult.setText("Errore: selezionare una squadra per poter procedere con l'analisi.\n");
    		return;
    	}
    	if(this.model.getTeamOfCalculatedList() == null || 
    			! this.model.getTeamOfCalculatedList().equals(team)) {
    		txtResult.setText("Errore: per il team selezionato, calcolare prima i risultati di tutte "
    				+ "le stagioni (bottone Seleziona Squadra), e poi l'annata d'oro!\n");
    		return;
    	}
    	
    	this.model.createGraph();
    	this.btnTrovaCamminoVirtuoso.setDisable(false);
    	Map<Integer, Integer> annateDoro = this.model.getAnnataDoro();
    	for(Integer annata : annateDoro.keySet()) {
    		if(annata == null) {
    			txtResult.appendText("Nessuna annata d'oro trovata: è possibile che la squadra selezionata "
    					+ "non abbia disputato stagioni in serie A, o ne abbia disputata solo una.\n");
    		}
    		else {
    	    	txtResult.appendText("L'annata d'oro trovata è "
    	    			+ "(in caso di pari peso, si considera la prima in ordine cronologico):\n\n");
    			txtResult.appendText(annata + " - peso: " + annateDoro.get(annata) + "\n");
    		}
    	}
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {
    	txtResult.clear();
    	String team = this.boxSquadra.getValue();
    	if(team == null) {
    		txtResult.setText("Errore: selezionare una squadra per poter procedere con l'analisi.\n");
    		return;
    	}
    	if(this.model.getTeamOfCalculatedList() == null || 
    			! this.model.getTeamOfCalculatedList().equals(team)) {
    		txtResult.setText("Errore: per il team selezionato, calcolare prima i risultati di tutte "
    				+ "le stagioni (bottone Seleziona Squadra), e poi la miglior serie di stagioni!\n");
    		return;
    	}
    	
    	List<SeasonForTeam> bestPath = this.model.camminoVirtuoso();
    	if(bestPath.isEmpty()) {
    		txtResult.setText("Errore, nessun percorso trovato: è possibile che la squadra selezionata "
    				+ "non abbia disputato nessun campionato in serie A.\n");
    		return;
    	}
    	txtResult.appendText("Miglior successione di stagioni consecutive con miglioramento del punteggio"
    			+ " per la squadra " + team + ": \n\n");
    	for(SeasonForTeam sft : bestPath) {
    		txtResult.appendText("Stagione " + sft.getSeason() + " - " + sft.getPoints() + " punti\n");
    	}
    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxSquadra.getItems().addAll(this.model.getAllTeams());
		this.btnTrovaAnnataOro.setDisable(true);
		this.btnTrovaCamminoVirtuoso.setDisable(true);
	}
}
