package cs.ualberta.ca.tunein;

import java.util.ArrayList;

import android.view.View;

public class ThreadController implements ThreadControllerInterface {

	private Thread discussionThread;
	
	public ThreadController(Thread threadList) {
	
		discussionThread = threadList;
	}

	@Override
	public void sortByLocation(GeoLocation loc) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sortBySetLocation(GeoLocation loc) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sortByPicture() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sortByDate() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createTopic(Comment aComment) 
	{
		ArrayList<Comment> list = discussionThread.getDiscussionThread();
		list.add(aComment);
	}
	
}
