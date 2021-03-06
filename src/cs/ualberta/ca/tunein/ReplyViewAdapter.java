package cs.ualberta.ca.tunein;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * View
 * ReplyViewAdapter Class:
 * This is part of the view class for comments. This class is used to 
 * display the replies to a comment using an expandable list view.
 * This class is not complete yet.
 * Dialog code from:
 * http://stackoverflow.com/questions/4279787/how-can-i-pass-values-between-a-dialog-and-an-activity
 * Bitmap code from:
 * http://stackoverflow.com/questions/4715044/android-how-to-convert-whole-imageview-to-bitmap
 * expandable listview code from:
 * http://androidtrainningcenter.blogspot.in/2012/07/android-expandable-listview-simple.html
 * http://www.dreamincode.net/forums/topic/270612-how-to-get-started-with-expandablelistview/
 * Intent code from:
 * http://stackoverflow.com/questions/2736389/how-to-pass-object-from-one-activity-to-another-in-android
 * Underline text code from:
 * http://stackoverflow.com/questions/8033316/to-draw-an-underline-below-the-textview-in-android
 */
public class ReplyViewAdapter extends BaseExpandableListAdapter{
	
	//public string that tags the extra of the comment that is passed to CommentPageActivity
	public final static String EXTRA_COMMENT = "cs.ualberta.ca.tunein.comment";
	//public string that tags the extra of the topic comment that is passed to CommentPageActivity
	public final static String EXTRA_TOPIC_COMMENT = "cs.ualberta.ca.tunein.topicComment";
	//public string that tags the extra of the comment to be edited that is passed to EditPageActivity
	public final static String EXTRA_USERID = "cs.ualberta.ca.tunein.userid";
	
	private Context context;
	//holder for elements in the row
	private ViewHolder holder;
	private ArrayList<Comment> replies;
	//comment controller
	private CommentController cntrl;
	
	//dialog elements
	private View createView;
	private TextView inputTitle;
	private TextView inputComment;
	private ImageView inputImage;
	
	/**
	 * View holder that holds the elements of a
	 * custom row that improves scrolling.
	 * Code from http://developer.android.com/training/improving-layouts/smooth-scrolling.html
	 */
	public static class ViewHolder
	{
		TextView textViewReply;
		TextView textViewReplyRowCount;
		TextView textViewReplyUser;
		TextView textViewReplyDate;
		
		Button buttonRowReply;
		Button buttonReplyView;
	}
	
	/**
	 * Constructor that constructs a ReplyViewAdapter
	 * @param context The context of the activity that constructs this adapter.
	 * @param replies The array list of replies to be displayed.
	 */
	public ReplyViewAdapter(Context context, ArrayList<Comment> replies)
	{
		this.context = context;
		this.replies = replies;
	}


	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<Comment> list = replies.get(groupPosition).getReplies();
		return list.get(childPosition);
	}


	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}


	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		holder = new ViewHolder();
		
		//create inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//get rowView from inflater
		View rowView = null;
		rowView = inflater.inflate(R.layout.reply_view_row, parent, false);
		
		//set all the elements in the custom comment row
		holder.textViewReply = (TextView) rowView.findViewById(R.id.textViewReply);
		holder.textViewReplyRowCount = (TextView) rowView.findViewById(R.id.textViewReplyRowCount);
		holder.textViewReplyUser = (TextView) rowView.findViewById(R.id.textViewReplyUser);
		holder.textViewReplyDate = (TextView) rowView.findViewById(R.id.textViewReplyDate);
		holder.buttonRowReply = (Button) rowView.findViewById(R.id.buttonRowReply);
		holder.buttonReplyView = (Button) rowView.findViewById(R.id.buttonReplyView);
		
		//set text of textviews
		holder.textViewReply.setText(replies.get(groupPosition).getReplies().get(childPosition).getComment());
		holder.textViewReplyRowCount.setText("Replies: " + Integer.toString(replies.get(groupPosition).getReplies().get(childPosition).getReplyCount()));
		holder.textViewReplyUser.setPaintFlags(holder.textViewReplyUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		holder.textViewReplyUser.setText(replies.get(groupPosition).getReplies().get(childPosition).getCommenter().getName());
		holder.textViewReplyDate.setText(replies.get(groupPosition).getReplies().get(childPosition).dateDisplay());
		
		//set onclick listeners for buttons and the tag for position
		//int array to send indexes
		int arr[] = {groupPosition, childPosition};
		holder.buttonRowReply.setOnClickListener(replyChildBtnClick);
		holder.buttonRowReply.setTag(arr);
		holder.buttonReplyView.setOnClickListener(viewChildBtnClick);
		holder.buttonReplyView.setTag(arr);
		holder.textViewReplyUser.setOnClickListener(profileChildBtnClick);
		holder.textViewReplyUser.setTag(arr);
		
		//set the holder
        rowView.setTag(holder);
		
		return rowView;
	}


	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<Comment> list = replies.get(groupPosition).getReplies();
		return list.size();
	}


	@Override
	public Object getGroup(int groupPosition) {
		return replies.get(groupPosition);
	}


	@Override
	public int getGroupCount() {
		return replies.size();
	}


	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}


	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
	
		//declare a new holder
		holder = new ViewHolder();
				
		//create inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
		//get rowView from inflater
		View rowView = null;
		rowView = inflater.inflate(R.layout.reply_view_row, parent, false);
		
		//set all the elements in the custom comment row
		holder.textViewReply = (TextView) rowView.findViewById(R.id.textViewReply);
		holder.textViewReplyRowCount = (TextView) rowView.findViewById(R.id.textViewReplyRowCount);
		holder.textViewReplyUser = (TextView) rowView.findViewById(R.id.textViewReplyUser);
		holder.textViewReplyDate = (TextView) rowView.findViewById(R.id.textViewReplyDate);
		holder.buttonRowReply = (Button) rowView.findViewById(R.id.buttonRowReply);
		holder.buttonReplyView = (Button) rowView.findViewById(R.id.buttonReplyView);
		
		//set text of textviews
		holder.textViewReply.setText(replies.get(groupPosition).getComment());
		holder.textViewReplyRowCount.setText("Replies: " + Integer.toString(replies.get(groupPosition).getReplyCount()));
		holder.textViewReplyUser.setPaintFlags(holder.textViewReplyUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		holder.textViewReplyUser.setText(replies.get(groupPosition).getCommenter().getName());
		holder.textViewReplyDate.setText(replies.get(groupPosition).dateDisplay());
		
		//set onclick listeners for buttons and the tag for position
		holder.buttonRowReply.setOnClickListener(replyParentBtnClick);
		holder.buttonRowReply.setTag(groupPosition);
		holder.buttonReplyView.setOnClickListener(viewParentBtnClick);
		holder.buttonReplyView.setTag(groupPosition);
		holder.textViewReplyUser.setOnClickListener(profileParentBtnClick);
		holder.textViewReplyUser.setTag(groupPosition);
		
		//set the holder
        rowView.setTag(holder);
		
		return rowView;
	}


	@Override
	public boolean hasStableIds() {
		return true;
	}


	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	
	/**
	 * This click listener will send user to CommentViewPage of the comment
	 * that they clicked view on.
	 */
	private OnClickListener viewChildBtnClick = new OnClickListener() 
	{
	    public void onClick(View v)
	    {
	    	int index[] = (int[])v.getTag();
	    	Comment aComment = replies.get(index[0]).getReplies().get(index[1]);
	    	Intent intent = new Intent(context, CommentPageActivity.class);
	    	intent.putExtra(EXTRA_COMMENT, aComment);
	    	intent.putExtra("isReplyReply", true);
	    	context.startActivity(intent);
	    }
	};
	
	/**
	 * This click listener will send user a comment creation dialog box
	 * so that they can reply to a comment that they clicked reply on.
	 * Bitmap code from:
	 * http://stackoverflow.com/questions/8490474/cant-compress-a-recycled-bitmap
	 */
	private OnClickListener replyChildBtnClick = new OnClickListener() 
	{
	    public void onClick(View v)
	    {
	    	final int index[] = (int[])v.getTag();
	    	setupDialogs();
			
			AlertDialog dialog = new AlertDialog.Builder(context)
			    .setTitle("Create Comment")
			    .setView(createView)
			    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            String title = inputTitle.getText().toString();
			            String text = inputComment.getText().toString();
			            
		        		Comment currentComment = replies.get(index[0]).getReplies().get(index[1]);
		        		cntrl = new CommentController(currentComment);
			            //create comment with image else one with no image
			            if (inputImage.getVisibility() == View.VISIBLE) 
			            {
			            	inputImage.buildDrawingCache(true);
			            	Bitmap bitmap = inputImage.getDrawingCache(true).copy(Config.RGB_565, false);
			            	inputImage.destroyDrawingCache();      
			        		cntrl.addReplyImg(currentComment.getElasticID(), (Activity) context, title, text, bitmap);
			            } 
			            else 
			            {	                
			        		cntrl.addReply(currentComment.getElasticID(), (Activity) context, title, text);
			            }
		        		updateReplyView(replies);
			        }
			    })
			    .setNegativeButton("Cancel", null).create();
			dialog.show();
	    }
	};
	
	/**
	 * This click listener will go to the profile page.
	 */
	private OnClickListener profileChildBtnClick = new OnClickListener() {
		public void onClick(View v) {
			int index[] = (int[])v.getTag();
			String userid = replies.get(index[0]).getReplies().get(index[1]).getCommenter().getUniqueID();
			Intent intent = new Intent(context,
					ProfileViewActivity.class);
			intent.putExtra(EXTRA_USERID, userid);
			context.startActivity(intent);
		}
	};
	
	/**
	 * This click listener will send user to CommentViewPage of the comment
	 * that they clicked view on.
	 */
	private OnClickListener viewParentBtnClick = new OnClickListener() 
	{
	    public void onClick(View v)
	    {
	    	int index = (Integer) v.getTag();
	    	Comment aComment = replies.get(index);
	    	Intent intent = new Intent(context, CommentPageActivity.class);
	    	intent.putExtra(EXTRA_COMMENT, aComment);
	    	intent.putExtra("isReplyReply", true);
	    	context.startActivity(intent);
	    }
	};
	
	/**
	 * This click listener will send user a comment creation dialog box
	 * so that they can reply to a comment that they clicked reply on.
	 * Bitmap code from:
	 * http://stackoverflow.com/questions/8490474/cant-compress-a-recycled-bitmap
	 */
	private OnClickListener replyParentBtnClick = new OnClickListener() 
	{
	    public void onClick(View v)
	    {
	    	final int i = (Integer) v.getTag();
	    	setupDialogs();
	    	
			AlertDialog dialog = new AlertDialog.Builder(context)
			    .setTitle("Create Comment")
			    .setView(createView)
			    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            String title = inputTitle.getText().toString();
			            String text = inputComment.getText().toString();
			            
			          //current comment that is replied to using tag and get parent position
		        		Comment currentComment = replies.get(i);
		        		cntrl = new CommentController(currentComment);
			            //create comment with image else one with no image
			            if (inputImage.getVisibility() == View.VISIBLE) 
			            {
			            	inputImage.buildDrawingCache(true);
			            	Bitmap bitmap = inputImage.getDrawingCache(true).copy(Config.RGB_565, false);
			            	inputImage.destroyDrawingCache(); 
			        		cntrl.addReplyImg(currentComment.getElasticID(), (Activity) context, title, text, bitmap);
			            } 
			            else 
			            {	                
			        		cntrl.addReply(currentComment.getElasticID(), (Activity) context, title, text);
			            }
			            updateReplyView(replies);
			        }
			    })
			    .setNegativeButton("Cancel", null).create();
			dialog.show();
	    }
	};
	
	/**
	 * This click listener will go to the profile page.
	 */
	private OnClickListener profileParentBtnClick = new OnClickListener() {
		public void onClick(View v) {
			int i = (Integer) v.getTag();
			String userid = replies.get(i).getCommenter().getUniqueID();
			Intent intent = new Intent(context,
					ProfileViewActivity.class);
			intent.putExtra(EXTRA_USERID, userid);
			context.startActivity(intent);
		}
	};

	/**
	 * Method to refresh the view and change the reference in
	 * this adapter to the one passed in.
	 * @param replies The new replies list.
	 */
	public void updateReplyView(ArrayList<Comment> replies) {
		this.replies = replies;
		notifyDataSetChanged();
	}
	
	/**
	 * This method is for setting up the dialog boxes.
	 */
	private void setupDialogs()
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		createView = inflater.inflate(R.layout.create_comment_view, null);

		inputTitle = (EditText) createView.findViewById(R.id.textViewInputTitle);
		inputComment = (EditText) createView.findViewById(R.id.editTextComment);
		inputImage = (ImageView) createView.findViewById(R.id.imageViewUpload);
	}


	/**
	 * Method for collapsing all parent items in the listview. Code from: http://stackoverflow.com/questions/2848091/expandablelistview-collapsing-all-parent-items
	 * @param listview  The listview to be collapsed.
	 */
	public void collapseAll(ExpandableListView listview) {
		int count = getGroupCount();
		for (int i = 0; i < count; i++) {
			listview.collapseGroup(i);
		}
	}
	
}
