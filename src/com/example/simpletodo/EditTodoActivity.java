package com.example.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditTodoActivity extends Activity 
{

	private EditText editedTodoText;
	private int editedTodoIndex; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_todo);
		
		editedTodoText = (EditText) findViewById( R.id.et_editTodo );

		Intent i = getIntent();
		
		//For todo text
		String currentText = i.getExtras().getString("todo");
		editedTodoText.setText( currentText );
		editedTodoText.setSelection(editedTodoText.getText().length());  //put cursor at end of text
		
		//For todo index in main list
		editedTodoIndex = i.getExtras().getInt("todo_list_index");
		
	}
	
	
	//Called when you click Save
	public void editTodoItem( View v)
	{

		Intent mainScreen = new Intent(getApplicationContext(), TodoActivity.class);

        //Sending data to another Activity
        mainScreen.putExtra("todo", editedTodoText.getText().toString());
        mainScreen.putExtra("todo_list_index", editedTodoIndex );
        
        setResult(RESULT_OK, mainScreen);

        finish();
        
        Log.d( "EDIT TODO", "finished editing todo and save pressed ");
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo, menu);
		return true;
	}

}
