package cs.ualberta.ca.tunein;


import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * View
 * TopicListActivity Class:
 * This is part of the view of displaying the topic list
 * of comments. This also includes the creation view of creating
 * topic comments through a dialog box.
 * Dialog code from:
 * http://stackoverflow.com/questions/4279787/how-can-i-pass-values-between-a-dialog-and-an-activity
 */
public class TopicListActivity extends Activity {

	public final static String SORT = "cs.ualberta.ca.tunein.sort";
	public static int SELECT_PICTURE_REQUEST_CODE = 12345;
	
	//comment view adapter
	private CommentViewAdapter viewAdapter;
	//listview
	private ListView listview;
	//discussion thread list
	private ThreadList threadList;
	//thread controller
	private ThreadController threadController;
	//variables for adding topic
	private String title;
	private String comment;
	
	//dialog elements
	private View createView;
	private TextView inputTitle;
	private TextView inputComment;
	private ImageView inputImage;
	
	private TextView textViewSort;
	
	//uri for image upload
	private Uri outputFileUri;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_list_view);
		setupTopicView();
	    threadList = new ThreadList();
		//setup the comment listview
		viewAdapter = new CommentViewAdapter(TopicListActivity.this, threadList.getDiscussionThread());
		threadController = new ThreadController(threadList);
		listview = (ListView) findViewById(R.id.listViewTopics);
		listview.setAdapter(viewAdapter);
		threadList.setAdapter(viewAdapter);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		threadController.getOnlineTopics(TopicListActivity.this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*
	 * Code from:
	 * http://stackoverflow.com/questions/4455558/allow-user-to-select-camera-or-gallery-for-image
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
	    if(resultCode == RESULT_OK)
	    {
	        if(requestCode == SELECT_PICTURE_REQUEST_CODE)
	        {
	            final boolean isCamera;
	            if(data == null)
	            {
	                isCamera = true;
	            }
	            else
	            {
	                final String action = data.getAction();
	                if(action == null)
	                {
	                    isCamera = false;
	                }
	                else
	                {
	                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	                }
	            }

	            Uri selectedImageUri;
	            if(isCamera)
	            {
	                selectedImageUri = outputFileUri;
	            }
	            else
	            {
	                selectedImageUri = data == null ? null : data.getData();
	            }
	            try {
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
					inputImage.setImageBitmap(bitmap);
		    		inputImage.setVisibility(View.VISIBLE);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	    }
	}
	
	/**
	 * Method to setup the topic list view.
	 */
	private void setupTopicView()
	{
	    SharedPreferences prefs = this.getSharedPreferences(
			      "cs.ualberta.ca.tunein", Context.MODE_PRIVATE);
	    String sortType = prefs.getString(SORT, "default");
		//setuptextview
		textViewSort = (TextView)findViewById(R.id.textViewSort);
		textViewSort.setText(sortType);
	}
	
	/**
	 * This method is to open the create topic comment dialog box
	 * and create a comment and add to the topic list.
	 * Bitmap code from 
	 * http://stackoverflow.com/questions/8490474/cant-compress-a-recycled-bitmap
	 * Listview code from
	 * http://stackoverflow.com/questions/8134541/how-to-set-the-focus-on-the-last-element-in-a-list-view-in-android
	 * @param v The view passed in.
	 */
	public void createCommentClick(View v)
	{
		setupDialogs();
		AlertDialog dialog = new AlertDialog.Builder(TopicListActivity.this)
		    .setTitle("Create Comment")
		    .setView(createView)
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) { 
		            title = inputTitle.getText().toString();
		            comment = inputComment.getText().toString();
		            
		            //create comment with image 
		            if (inputImage.getVisibility() == View.VISIBLE) 
		            {
		            	inputImage.buildDrawingCache(true);
		            	Bitmap bitmap = inputImage.getDrawingCache(true).copy(Config.RGB_565, false);
		            	inputImage.destroyDrawingCache();
		            	threadController.createTopicImg(TopicListActivity.this, title, comment, bitmap);
		            } 
		            else 
		            {	                
		            	threadController.createTopic(TopicListActivity.this, title, comment);     		
		            }
		            viewAdapter.updateThreadView(threadList.getDiscussionThread());
		            //move the listview to the bottom to see new item
		            listview.setSelection(listview.getAdapter().getCount()-1);
		        }
		    })
		    .setNegativeButton("Cancel", null).create();
		dialog.show();
	}
	
	/**
	 * Image to upload a image from the gallery or take a picture from the camera.
	 * @param v
	 */
	public void uploadImageBtnClick(View v) {
		ImageController imgCntrl = new ImageController(TopicListActivity.this);
		outputFileUri = imgCntrl.openImageIntent();
	}
	
	/**
	 * This method is for setting up the dialog boxes.
	 */
	private void setupDialogs()
	{
		LayoutInflater inflater = LayoutInflater.from(TopicListActivity.this);
		createView = inflater.inflate(R.layout.create_comment_view, null);

		inputTitle = (EditText) createView.findViewById(R.id.textViewInputTitle);
		inputComment = (EditText) createView.findViewById(R.id.editTextComment);
		inputImage = (ImageView) createView.findViewById(R.id.imageViewUpload);
	}

}
