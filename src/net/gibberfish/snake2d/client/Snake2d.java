package net.gibberfish.snake2d.client;

import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Snake2d implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	
	Canvas canvas;
    Context2d ctx;
    static final int canvasHeight = 300;
    static final int canvasWidth = 400;
    
    Canvas canvas_menue;
    Context2d ctx_m;
    static final int canvas_menueHeight = 300;
    static final int canvas_menueWidth = 140;
    
    int gridSize = 10;
    int snakeLength = 3;
    int foodItems = 0;
    boolean paused = true;
    boolean allowPressKeys = false;
    int maxFoodItems = 1;
    int score = 0;
    String direction = "";
    int[] currentPosition;
    int[] suggestedPoint;
    ArrayList<int[]> snakeBody;// = new ArrayList<int[]>();
    
    Timer timer;
    
    myMenueButton buttonPause = new myMenueButton(20, 50, 100, 25);
    myMenueButton buttonRestart = new myMenueButton(20, 80, 100, 25);
    myMenueButton buttonResume = new myMenueButton(20, 110, 100, 25);
    
    
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		canvas = Canvas.createIfSupported();
		canvas_menue = Canvas.createIfSupported();
		 
        if (canvas == null) {
              RootPanel.get().add(new Label("Sorry, your browser doesn't support the HTML5 Canvas element"));
              return;
        }
 
        canvas.setStyleName("mainCanvas");
        canvas.setWidth(canvasWidth + "px");
        canvas.setCoordinateSpaceWidth(canvasWidth);
 
        canvas.setHeight(canvasHeight + "px");
        canvas.setCoordinateSpaceHeight(canvasHeight);
 
        RootPanel.get().add(canvas);
        ctx = canvas.getContext2d();
        
        canvas_menue.setStyleName("menueCanvas");
        canvas_menue.setWidth(canvas_menueWidth + "px");
        canvas_menue.setCoordinateSpaceWidth(canvas_menueWidth);
 
        canvas_menue.setHeight(canvas_menueHeight + "px");
        canvas_menue.setCoordinateSpaceHeight(canvas_menueHeight);
 
        RootPanel.get().add(canvas_menue);
        ctx_m = canvas_menue.getContext2d();
        
        canvas_menue.addClickHandler(new ClickHandler() {
		
        	@Override
			public void onClick(ClickEvent event) {
				
	            checkButtonClicked(event);	
			}

		});   
        
        canvas.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				keyPressed(event);	
			}
			
		});
        
        timer = new Timer() {
            @Override
            public void run() {
                snakeLoop();
            }
        };
        timer.scheduleRepeating(100);
        start();
       
	}
	
	private void keyPressed(KeyDownEvent event) {
		if (!allowPressKeys){
	        return;
	    }
		
    	 if (event.isLeftArrow()){
            // action when pressing left key.
            if ( !direction.equals("right"))
                {
                    direction = "left";
                }
    	 }
         // up 
    	 if (event.isUpArrow()){
            // action when pressing up key
            if ( !direction.equals("down"))
                {
                    direction = "up";	       	
                }
    	 }
         // right 
    	 if (event.isRightArrow()){
            // action when pressing right key
            if ( !direction.equals("left"))
                {
                    direction = "right";
                }
            }
         // down
    	 if (event.isDownArrow()){
            // action when pressing down key
            if ( !direction.equals("up"))
                {
                    direction = "down";
                }
    	 }	 	
	}
	
	private void checkButtonClicked(ClickEvent event) {
		int mouse_x = event.getClientX() - canvas_menue.getAbsoluteLeft();
        int mouse_y = event.getClientY() - canvas.getAbsoluteTop();
        
		if (paused){	 
            //Button Restart clicked?
            if ((mouse_x > buttonRestart.getX()) && (mouse_x < (buttonRestart.getX() + buttonRestart.getWidth()))){
                //within horizon, test vertical
                if ((mouse_y > buttonRestart.getY()) && (mouse_y < (buttonRestart.getY() + buttonRestart.getHeight()))){
                    restart();
                }
            }
            
            //Button Resume clicked?
            if ((mouse_x > buttonResume.getX()) && (mouse_x < (buttonResume.getX() + buttonResume.getWidth()))){
                //within horizon, test vertical
                if ((mouse_y > buttonResume.getY()) && (mouse_y < (buttonResume.getY() + buttonResume.getHeight()))){
                    play();
                }
            }
            
        }
        else{
            //Button pause clicked?
            if ((mouse_x > buttonPause.getX()) && (mouse_x < (buttonPause.getX() + buttonPause.getWidth()))){
                //within horizon, test vertical
                if ((mouse_y > buttonPause.getY()) && (mouse_y < (buttonPause.getY() + buttonPause.getHeight()))){
                    pause();
                }
            }
        }
		
	}
	
	private void restart() {
		pause();
		start();
	}

	public void start(){
		ctx.clearRect(0,0, canvasWidth, canvasHeight);
		snakeBody = new ArrayList<int[]>();
	   
	    snakeLength = 3;
	    foodItems = 0;
	    maxFoodItems = 1;
	    // The current position of the Snake's head, as xy coordinates
	    currentPosition = new int[]{50, 50}; 
	    direction = "right";
	    play(); 
	}
	
	public void play(){
		 updateScore();
		 paused = false;
		 allowPressKeys = true;
	}
	
	public void updateScore(){
		   score = (snakeLength - 3)*10;
		}
	
	public void gameOver()
	{
	    updateScore();
	    pause();
	    foodItems = 0;
	    snakeBody.clear();
	    snakeLength = 3;
	    Window.alert("Game Over. Your score was "+ score);
	    ctx.clearRect(0,0, canvasWidth, canvasHeight);

	}
	
	public void pause(){
	    allowPressKeys = false;
	    paused = true;
	}
	
	public void snakeLoop() {
		 
		drawMenue();   
		if (!paused){
	       updateScore();
	       if (foodItems < maxFoodItems)
	       {
	           makeFoodItem();
	       }

	       if(direction.equals("up")){
	            moveUp();
	       }else if (direction.equals("down")){
	            moveDown();    
	       }else if (direction.equals("left")){
	            moveLeft();
	       }else if(direction.equals("right")){
	           moveRight();
	       }//End if
		}
	}
   
	
	public void makeFoodItem()
	{
	   suggestedPoint = new int[]{(int) (Math.floor(Math.random()*(canvasWidth/gridSize))*gridSize), (int) (Math.floor(Math.random()*(canvasHeight/gridSize))*gridSize)};
	   if (hasEaten(suggestedPoint)) {
	     makeFoodItem();
	   } else {
	     ctx.setFillStyle("rgb(10,100,0)");
	     ctx.fillRect(suggestedPoint[0], suggestedPoint[1], gridSize, gridSize);
	     foodItems = 1;
	   }
	}
	
	public void moveUp(){
	    if ((currentPosition[1] - gridSize) >= 0) {
	     executeMove("up", 1, (currentPosition[1] - gridSize));
	   } else {
	     whichWayToGo(0);
	   }
	 }
	 
	public void moveDown(){
	   if ((currentPosition[1] + gridSize) < canvasHeight) {
	     executeMove("down", 1, (currentPosition[1] + gridSize));    
	   } else {
	     whichWayToGo(0);
	   }
	 }
	 
	public void moveLeft(){
	   if ((currentPosition[0] - gridSize) >= 0) {
	     executeMove("left", 0, (currentPosition[0] - gridSize));
	   } else {
	     whichWayToGo(1);
	   }
	 }
	 
	public void moveRight(){
	   if ((currentPosition[0] + gridSize) < canvasWidth) {
	     executeMove("right", 0, (currentPosition[0] + gridSize));
	   } else {
	     whichWayToGo(1);
	   }
	}
	
	public void executeMove(String dirValue, int axisType, int axisValue) {
		   direction = dirValue;
		   currentPosition[axisType] = axisValue;
		   drawSnake();
		}
	
	public void whichWayToGo(int axisType){  
		  if (axisType==0) {
		    if(currentPosition[0] > canvasWidth / 2){
		    	moveLeft();
		    }else{
		    	moveRight();
		    }
		  } else {
		   if(currentPosition[1] > canvasHeight / 2){
			   moveUp();
		   }else{
			   moveDown();
		   }
		  }
		}

	private boolean hasEaten(int[] point){
		for (int[] i : snakeBody){
			if (i[0] == point[0] && i[1] == point[1] ) 
	        {
	            return true;
	        }
		}
		return false;
	}
	
	private void drawSnake() {
		if ( hasEaten(currentPosition))
        {
            gameOver();
            return;
        }
		
		snakeBody.add(new int[]{currentPosition[0],currentPosition[1]});
		ctx.fillRect(currentPosition[0], currentPosition[1], gridSize, gridSize);
		if (snakeBody.size() > snakeLength) {
			int[] itemToRemove = snakeBody.get(0);
			snakeBody.remove(0);
			ctx.clearRect(itemToRemove[0], itemToRemove[1], gridSize, gridSize);
			
		}
        if (currentPosition[0] == suggestedPoint[0] && currentPosition[1] == suggestedPoint[1]) {
            makeFoodItem();
            snakeLength += 1;
        }  
		
	}

	public void drawMenue()
	{
	    ctx_m.setFont("18pt Arial"); 
	    ctx_m.clearRect(0,0, canvas_menueWidth, canvas_menueHeight);
	    
	    ctx_m.setFillStyle("rgb(150,29,28)");
	    ctx_m.fillText("Score: " + score, 20, 230); 
	    
	    //Draw Buttons
	    ctx_m.setFillStyle("rgb(150,29,28)");
	    if (!paused) {
	        ctx_m.fillRect (buttonPause.getX(), buttonPause.getY(), buttonPause.getWidth(), buttonPause.getHeight());
	    }
	    else
	    {
	        ctx_m.fillRect (buttonResume.getX(), buttonResume.getY(), buttonResume.getWidth(), buttonResume.getHeight());
	        ctx_m.fillRect (buttonRestart.getX(), buttonRestart.getY(), buttonRestart.getWidth(), buttonRestart.getHeight());
	    }
	    
	    ctx_m.setFillStyle("rgb(255,255,255)");
	    if (!paused) {
	        ctx_m.fillText("Pause" , buttonPause.getX() + 5, buttonPause.getY() + 20);
	    }
	    else
	    {
	        ctx_m.fillText("Resume" , buttonResume.getX() + 5, buttonResume.getY() + 20);
	        ctx_m.fillText("Restart" , buttonRestart.getX() + 5, buttonRestart.getY() + 20);
	    }
	    
	}
}
