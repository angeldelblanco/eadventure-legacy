package es.eucm.eadventure.editor.data.lom;

import java.util.ArrayList;

import es.eucm.eadventure.editor.control.controllers.lom.LOMTextDataControl;

public class LOMEducational {

	//5.1 Vocabulary: active, explosive, mixed
	private Vocabulary interactivityType;
	
	//5.2 Vocabulary: exercise, simulation, questionnaire, diagram, figure, graph, index, slide, table, 
	//narrative text, exam, experiment, problem statement, self assessment, lecture
	private ArrayList<Vocabulary> learningResourceType;
	
	//5.3 Vocabulary: active, explosive, mixed
	private Vocabulary interactivityLevel;
	
	//5.4
	private Vocabulary semanticDensity;
	
	//5.5
	private ArrayList<Vocabulary> intendedEndUserRole;
	
	//5.6
	private ArrayList<Vocabulary> context;
	
	//5.7
	private ArrayList<LangString> typicalAgeRange;
	
	//5.8
	private Vocabulary difficulty;
	
	//5.9 DURATION
	private String typicalLearningTime;
	
	//5.10
	private ArrayList<LangString> description;
	
	//5.11 : NOT SUPPORTED BY EDITOR
	private ArrayList<String> language;
	
	
	
	public LOMEducational (){

		this.typicalLearningTime = null;
		
		this.language = new ArrayList<String>();
		
		this.context = new ArrayList<Vocabulary>();
		this.intendedEndUserRole = new ArrayList<Vocabulary>();
		this.learningResourceType = new ArrayList<Vocabulary>();
		
		this.description = new ArrayList<LangString>();
		this.typicalAgeRange = new ArrayList<LangString>();
		
		this.difficulty = new Vocabulary(Vocabulary.ED_DIFFICULTY_5_8);
		this.interactivityLevel = new Vocabulary(Vocabulary.ED_INTERACTIVITY_LEVEL_5_3);
		this.interactivityType = new Vocabulary(Vocabulary.ED_INTERACTIVITY_TYPE_5_1);
		this.semanticDensity = new Vocabulary(Vocabulary.ED_SEMANTIC_DENSITY_5_4);

	}
	
	
	/***********************************ADD METHODS **************************/

	
	public void addLanguage(String lang){
		language.add( lang );
	}
	
	public void addContext(int index){
		this.context.add( new Vocabulary(Vocabulary.ED_CONTEXT_5_6, index) );
	}

	
	public void addIntendedEndUserRole(int index){
		this.intendedEndUserRole.add( new Vocabulary(Vocabulary.ED_INTENDED_END_USER_ROLE_5_5, index) );
	}
	
	public void addLearningResourceType(int index){
		this.learningResourceType.add( new Vocabulary(Vocabulary.ED_LEARNING_RESOURCE_TYPE_5_2, index) );
	}

	
	public void addDescription(LangString description){
		this.description.add(description);
	}
	
	public void addTypicalAgeRange(LangString typicalAgeRange){
		this.typicalAgeRange.add( typicalAgeRange );
	}



	/*********************************** SETTERS **************************/
	public void setTypicalLearningTime(String typicalLearningTime){
		this.typicalLearningTime = typicalLearningTime; 
	}
	
	public void setLanguage(String language){
		this.language = new ArrayList<String>();
		this.language.add( language );
	}
	
	public void setContext(int index){
		this.context = new ArrayList<Vocabulary>();
		this.context.add( new Vocabulary(Vocabulary.ED_CONTEXT_5_6, index) );
	}

	
	public void setIntendedEndUserRole(int index){
		this.intendedEndUserRole = new ArrayList<Vocabulary>();
		this.intendedEndUserRole.add( new Vocabulary(Vocabulary.ED_INTENDED_END_USER_ROLE_5_5, index) );
	}
	
	public void setSemanticDensity(int index){
		this.semanticDensity = new Vocabulary(Vocabulary.ED_SEMANTIC_DENSITY_5_4, index);
	}
	
	public void setDifficulty(int index){
		this.difficulty = new Vocabulary(Vocabulary.ED_DIFFICULTY_5_8, index);
	}
	
	public void setInteractivityLevel(int index){
		this.interactivityLevel = new Vocabulary(Vocabulary.ED_INTERACTIVITY_LEVEL_5_3, index);
	}
	
	public void setInteractivityType(int index){
		this.interactivityType= new Vocabulary(Vocabulary.ED_INTERACTIVITY_TYPE_5_1, index);
	}
	
	
	
	public void setLearningResourceType(int index){
		this.learningResourceType = new ArrayList<Vocabulary>();
		this.learningResourceType.add( new Vocabulary(Vocabulary.ED_LEARNING_RESOURCE_TYPE_5_2, index) );
	}

	
	public void setDescription(LangString description){
		this.description = new ArrayList<LangString>();
		this.description.add( description );
	}
	
	public void setTypicalAgeRange(LangString typicalAgeRange){
		this.typicalAgeRange = new ArrayList<LangString>();
		this.typicalAgeRange.add( typicalAgeRange );
	}



	/*********************************** GETTERS **************************/
	public String getTypicalLearningTime(){
		return typicalLearningTime; 
	}
	
	public String getLanguage(){
		return language.get( 0 );
	}
	
	public Vocabulary getContext(){
		return context.get( 0 );
	}

	
	public Vocabulary getIntendedEndUserRole(){
		return intendedEndUserRole.get( 0 );
	}
	
	public Vocabulary getLearningResourceType(){
		return learningResourceType.get( 0 );
	}

	
	public LangString getDescription(){
		return description.get( 0 );
	}
	
	public LangString getTypicalAgeRange(){
		return typicalAgeRange.get( 0 );
	}

	public Vocabulary getDifficulty(){
		return difficulty;
	}

	public Vocabulary getInteractivityLevel(){
		return interactivityLevel;
	}

	public Vocabulary getInteractivityType(){
		return interactivityType;
	}


	public Vocabulary getSemanticDensity(){
		return semanticDensity;
	}
	
}
