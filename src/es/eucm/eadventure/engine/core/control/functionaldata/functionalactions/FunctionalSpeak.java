package es.eucm.eadventure.engine.core.control.functionaldata.functionalactions;

import java.util.Timer;
import java.util.TimerTask;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.editor.control.controllers.AssetsController;
import es.eucm.eadventure.engine.core.control.ActionManager;
import es.eucm.eadventure.engine.core.control.Game;
import es.eucm.eadventure.engine.core.control.Options;
import es.eucm.eadventure.engine.core.control.animations.Animation;
import es.eucm.eadventure.engine.core.control.animations.AnimationState;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalElement;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalPlayer;
import es.eucm.eadventure.engine.core.gui.GUI;
import es.eucm.eadventure.engine.multimedia.MultimediaManager;

/**
 * The action to speak a line
 * 
 * @author Eugenio Marchiori
 *
 */
public class FunctionalSpeak extends FunctionalAction {

	/**
	 * The text to be spoken
	 */
	private String[] text;

    /**
     * The id of the spoken audio
     */
    private long audioId=-1;
    
    /**
     * This is an Voice object of FreeTTS, that is used to synthesize the sound of a 
     * conversation line.
     */
    private Voice voice;
    
    /**
     * The speech must be launched in another thread
     */
    private TTask task;
    
    /**
     * Check if tts synthesizer is been used
     */
    private boolean ttsInUse;

    /**
     * Time spent in this state
     */
    private long totalTime;
    
    /**
     * The time the character will be talking
     */
    private int timeTalking;

	/**
	 * Constructor with the original action and the text
	 * to speak
	 * 
	 * @param action The original action
	 * @param text The text to speak
	 */
	public FunctionalSpeak(Action action, String text) {
		super(action);
		type = ActionManager.ACTION_TALK;
		setText(text);
		ttsInUse=false;
	}

	/**
	 * Constructor with the original action, the text
	 * to speak and the audio to use
	 * 
	 * @param action The original action
	 * @param text The text to speak
	 * @param audioPath The path of the audio
	 */
	public FunctionalSpeak(Action action, String text, String audioPath) {
		super(action);
		type = ActionManager.ACTION_TALK;
		setText(text);
		setAudio(audioPath);
		ttsInUse=false;
	}

	@Override
	public void start(FunctionalPlayer functionalPlayer) {
		this.functionalPlayer = functionalPlayer;
		totalTime = 0;
		
        Resources resources = functionalPlayer.getResources( );
        MultimediaManager multimedia = MultimediaManager.getInstance( );
        Animation[] animations = new Animation[4];
        
        animations[AnimationState.EAST] = multimedia.loadAnimation( resources.getAssetPath( Player.RESOURCE_TYPE_SPEAK_RIGHT ), false, MultimediaManager.IMAGE_PLAYER );
        if (resources.getAssetPath(Player.RESOURCE_TYPE_SPEAK_LEFT) != null && !resources.getAssetPath(Player.RESOURCE_TYPE_SPEAK_LEFT).equals(AssetsController.ASSET_EMPTY_ANIMATION))
        	animations[AnimationState.WEST] = multimedia.loadAnimation( resources.getAssetPath( Player.RESOURCE_TYPE_SPEAK_LEFT), false, MultimediaManager.IMAGE_PLAYER);
        else
        	animations[AnimationState.WEST] = multimedia.loadAnimation( resources.getAssetPath( Player.RESOURCE_TYPE_SPEAK_RIGHT ), true, MultimediaManager.IMAGE_PLAYER );
        animations[AnimationState.NORTH] = multimedia.loadAnimation( resources.getAssetPath( Player.RESOURCE_TYPE_SPEAK_UP ), false, MultimediaManager.IMAGE_PLAYER );
        animations[AnimationState.SOUTH] = multimedia.loadAnimation( resources.getAssetPath( Player.RESOURCE_TYPE_SPEAK_DOWN ), false, MultimediaManager.IMAGE_PLAYER );
        
        functionalPlayer.setAnimation(animations, -1);
   	}

	@Override
	public void update(long elapsedTime) {
		totalTime += elapsedTime;
		
        if( totalTime > timeTalking &&(audioId==-1 || !MultimediaManager.getInstance( ).isPlaying( audioId ))&&(!ttsInUse) ) {
        	finished = true;
        	functionalPlayer.popAnimation();
        	stopTTSTalking();
        }
	}

    /**
     * Set the text to be displayed. This is what the player is saying
     * @param text the text to be displayed
     */
    public void setText( String text2 ) {
    	String text = Game.getInstance().processText(text2);

        this.text = GUI.getInstance( ).splitText( text );
        
        float multiplier = 1;
        if( Game.getInstance( ).getOptions( ).getTextSpeed( ) == Options.TEXT_SLOW ) multiplier = 1.5f;
        if( Game.getInstance( ).getOptions( ).getTextSpeed( ) == Options.TEXT_FAST ) multiplier = 0.8f;
        
        timeTalking = (int)( 300 * text.split( " " ).length * multiplier );
        if( timeTalking < (int)( 1400 * multiplier ) ) timeTalking = (int)( 1400 * multiplier );
    }
    
    /**
     * Set the audio used by the action
     * 
     * @param audioPath The path of the audio
     */
    public void setAudio( String audioPath ) {
        if (audioPath==null){
            if (audioId!=-1){
                MultimediaManager.getInstance( ).stopPlayingInmediately( audioId );
                while(MultimediaManager.getInstance( ).isPlaying( audioId )){
                try {
                    Thread.sleep( 1 );
                } catch( InterruptedException e ) {
                }}
                audioId = -1;
            }
        }
        else{
        if (audioId!=-1){
            MultimediaManager.getInstance( ).stopPlayingInmediately( audioId );
            while(MultimediaManager.getInstance( ).isPlaying( audioId )){
            try {
                Thread.sleep( 1 );
            } catch( InterruptedException e ) {
            }}
        }
        
        //Gap between audios: 0.5s
        try {
            Thread.sleep( 500 );
        } catch( InterruptedException e ) {
        }

        
        audioId = MultimediaManager.getInstance( ).loadSound( audioPath, false );
        MultimediaManager.getInstance( ).startPlaying( audioId );
        while(!MultimediaManager.getInstance( ).isPlaying( audioId )){
            try {
                Thread.sleep( 1 );
            } catch( InterruptedException e ) {
            }
            }
        }
    }

    /**
     * Set the parameters to speak with freeTTS.
     * 
     * @param text2 The text that must be said
     * @param voice The voice of the player
     */
    public void setSpeakFreeTTS(String text2, String voice){
    	String text = Game.getInstance().processText(text2);
    	
    	task = new TTask(voice, text);
    	Timer timer = new Timer () ;
    	ttsInUse = true;
    	timer.schedule(task, 0);
   }
    
    /**
     * Stop the freetts speech
     */
    public void stopTTSTalking(){
    	if (task != null){
    		task.cancel();
    		ttsInUse = false;
    	}
    }

    /**
     * The timertask that plays the freetts speech in the background
     */
    public class TTask extends TimerTask{

    	private String text;
    	private boolean deallocate;
    	
    	public TTask ( String voiceText, String text ){
    		this.text = text;
    		this.deallocate = false;
    		 VoiceManager voiceManager = VoiceManager.getInstance();
	         voice = voiceManager.getVoice(voiceText);
	         voice.allocate();
	        
	         
    	}
    	
		@Override
		public void run() {
	    	try{
	    	
			 voice.speak(text);
			 ttsInUse=false;
	        
	    	}catch(IllegalStateException e){
				System.out.println("TTS found one word which can not be processated.");
			}
	        
		}
		
		@Override
		public boolean cancel(){
			 if (!deallocate) {
				voice.deallocate();
				deallocate = true;
			}
			 return true;
			
		}
    }

	@Override
	public void drawAditionalElements() {
        if( text != null && (text.length > 1 || !text[0].equals(""))) {
        	int posX;
        	int posY;
        	if (functionalPlayer != null && !functionalPlayer.isTransparent()) {
	        	posX = (int) functionalPlayer.getX() - Game.getInstance( ).getFunctionalScene( ).getOffsetX( );
	            posY = (int) (functionalPlayer.getY() - functionalPlayer.getHeight( ) * functionalPlayer.getScale());
        	} else {
        		posX = Math.round( GUI.WINDOW_WIDTH/2.0f+Game.getInstance().getFunctionalScene().getOffsetX( ) );
        		posY = Math.round( GUI.WINDOW_HEIGHT*1.0f/6.0f+ (functionalPlayer!=null?functionalPlayer.getHeight():0) );
        	}
        	if (functionalPlayer.getShowsSpeechBubbles()) {
        		GUI.getInstance().addTextToDraw( text, posX , posY - 15, functionalPlayer.getTextFrontColor( ), functionalPlayer.getTextBorderColor( ), functionalPlayer.getBubbleBkgColor(), functionalPlayer.getBubbleBorderColor());
        	} else
        		GUI.getInstance().addTextToDraw( text, posX , posY, functionalPlayer.getTextFrontColor( ), functionalPlayer.getTextBorderColor( ));
        }
	}

	@Override
	public void stop() {
		if (this.isStarted())
			stopTTSTalking();
	}

	@Override
	public void setAnotherElement(FunctionalElement element) {
	}

}