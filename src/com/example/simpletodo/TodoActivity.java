package com.example.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class TodoActivity extends Activity 
{

	private final int REQUEST_CODE = 20;

	private ArrayList<String> items;
	private ArrayAdapter<String> itemsAdapter;
	private ListView lvItems;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);

		//Read in previously saved items or create an empty list 
		readItems();

		lvItems = (ListView) findViewById( R.id.lvItems );
		itemsAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter( itemsAdapter );

		//Setup click listeners
		setupListViewClickListener();  //edit todo on regular click
		setupListViewLongClickListener();  //delete todo on long click

	}

	public void addTodoItem( View v)
	{
		EditText etNewItem = (EditText) findViewById( R.id.etNewItem );
		itemsAdapter.add( etNewItem.getText().toString() );
		etNewItem.setText("");

		saveItems(); 
		
	}

	//Regular click allows user to edit todo item
	private void setupListViewClickListener()
	{
		lvItems.setOnItemClickListener( new AdapterView.OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) 
			{
				TextView clickedView = (TextView) view;

				Intent nextScreen = new Intent(getApplicationContext(), EditTodoActivity.class);

				//Sending data to another Activity
				nextScreen.putExtra("todo", clickedView.getText());
				nextScreen.putExtra("todo_list_index", (int) id);

				startActivityForResult(nextScreen, REQUEST_CODE);

			}
		});
	}
	

	//Long click allows user to delete todo item
		private void setupListViewLongClickListener()
		{
			lvItems.setOnItemLongClickListener( new OnItemLongClickListener()
			{
				@Override
				public boolean onItemLongClick( AdapterView<?> aView, View item, int pos, long id )
				{
					items.remove( pos );
					itemsAdapter.notifyDataSetInvalidated();
					saveItems();
					return true;
				}
			});
		}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// REQUEST_CODE is defined above
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) 
		{
	
			// Get updated todo text
			String editedTodo = data.getExtras().getString("todo");
			int editedTodoIndex = data.getExtras().getInt("todo_list_index");

			//Update in data
			items.set( editedTodoIndex, editedTodo);
			
			//Update in display and save to file
			itemsAdapter.notifyDataSetInvalidated();
			
			saveItems();
			
		}

	} 

	
	private void readItems()
	{
		File filesDir = getFilesDir();
		File todoFile = new File( filesDir, "todo.txt" );

		try
		{
			items = new ArrayList<String>( FileUtils.readLines( todoFile ) );
		}
		catch( IOException e)
		{
			items = new ArrayList<String>();
			e.printStackTrace();
		}
	}

	private void saveItems()
	{
		File filesDir = getFilesDir();
		File todoFile = new File( filesDir, "todo.txt");
		try
		{
			FileUtils.writeLines( todoFile, items);
		}
		catch( IOException e)
		{
			e.printStackTrace();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo, menu);
		return true;
	}

}
