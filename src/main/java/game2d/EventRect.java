package game2d;

import java.awt.Rectangle;

public class EventRect extends Rectangle {

	private static final long serialVersionUID = 2138182177701204028L;

	private int eventRectDefaultX;
	private int eventRectDefaultY;
	private boolean eventDone;
	
	public EventRect() {
		eventDone = false;
	}
	
	public int getEventRectDefaultX() {
		return eventRectDefaultX;
	}
	
	public void setEventRectDefaultX(int eventRectDefaultX) {
		this.eventRectDefaultX = eventRectDefaultX;
	}
	
	public int getEventRectDefaultY() {
		return eventRectDefaultY;
	}
	
	public void setEventRectDefaultY(int eventRectDefaultY) {
		this.eventRectDefaultY = eventRectDefaultY;
	}
	
	public boolean isEventDone() {
		return eventDone;
	}
	
	public void setEventDone(boolean eventDone) {
		this.eventDone = eventDone;
	}
}
