package controller;

import view.View;
import model.Model;

public class Controller {

	Model model;
	View view;
	
	public Controller(Model model, View view){
		this.model = model;
		this.view = view;
	}
}
