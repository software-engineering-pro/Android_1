package simplenotepad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.coursetable.R;
import simplenotepad.db.NotesDB;
import simplenotepad.db.NotesDao;
import simplenotepad.model.Note;

import java.util.Date;


public class EditNoteActivity extends AppCompatActivity {
    private EditText inputNote;
    private NotesDao dao;
    private Note temp;
    public static final String NOTE_EXTRA_Key = "note_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 设置主题
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        int theme = sharedPreferences.getInt(MainActivity.THEME_Key, R.style.AppTheme);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_note);
        Toolbar toolbar = findViewById(R.id.edit_note_activity_toolbar);
        setSupportActionBar(toolbar);

        inputNote = findViewById(R.id.input_note);
        dao = NotesDB.getInstance(this).notesDao();
        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key, 0);
            temp = dao.getNoteById(id);
            inputNote.setText(temp.getNoteText());
        } else inputNote.setFocusable(true);

    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.edite_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note)
            onSaveNote();
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {
        // 保存note
        String text = inputNote.getText().toString();
        if (!text.isEmpty()) {
            long date = new Date().getTime(); // 获取建立时时间
            if (temp == null) {
                temp = new Note(text, date);
                dao.insertNote(temp);
            } else {
                temp.setNoteText(text);
                temp.setNoteDate(date);
                dao.updateNote(temp);
            }

            finish(); // 返回主页面
        }

    }
}
