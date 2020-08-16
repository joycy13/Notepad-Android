package com.example.notepad

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.utils.deleteNote
import com.example.notepad.utils.loadNote
import com.example.notepad.utils.persistNote
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var adapter: NoteAdapter
    lateinit var notes: MutableList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<FloatingActionButton>(R.id.create_note_fab).setOnClickListener(this)

        notes = loadNote(this)
        adapter = NoteAdapter(notes, this)
        /*notes = mutableListOf()*/
        /*notes.add(Note("Note 1", "Toto"))
        notes.add(Note("Note 2", "Titi"))
        notes.add(Note("Note 3", "Tata"))
        notes.add(Note("Note 4", "Tutu"))*/

        coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinator_layout) as CoordinatorLayout
        findViewById<FloatingActionButton>(R.id.create_note_fab).setOnClickListener(this)

        val recyclerView = findViewById<RecyclerView>(R.id.note_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        when (requestCode) {
            NoteDetailActivity.REQUEST_EDIT_NOTE -> processEditNoteResult(data)
        }
    }

    private fun processEditNoteResult(data: Intent) {
        val noteIndex = data.getIntExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, -1)
        when(data.action) {
            NoteDetailActivity.ACTION_SAVE_NOTE -> {
                val note = data.getParcelableExtra<Note>(NoteDetailActivity.EXTRA_NOTE)
                saveNote(note, noteIndex)
            }
            NoteDetailActivity.ACTION_DELETE_NOTE -> {
            }
        }
    }

    override fun onClick(view: View) {
        if (view.tag != null) {
            startDetailActivity(view.tag as Int)
        } else {
            when (view.id) {
                R.id.create_note_fab -> createNewNote()
            }
        }
    }

    fun createNewNote() {
        startDetailActivity(-1)
    }

    fun saveNote(note: Note, noteIndex: Int) {
        persistNote(this, note)
        if (noteIndex < 0) {
            notes.add(0, note)
        } else {
            notes[noteIndex] = note
        }
        adapter.notifyDataSetChanged()
    }


    fun deleteNote(noteIndex: Int) {
        if (noteIndex < 0) {
            return
        }
        val note = notes.removeAt(noteIndex)
        deleteNote(this, note)
        adapter.notifyDataSetChanged()
        Snackbar.make(coordinatorLayout, "${note.title} Supprimée", Snackbar.LENGTH_SHORT).show()
    }

    fun startDetailActivity(noteIndex: Int) {
        val note = if (noteIndex < 0) Note() else notes[noteIndex]

        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE, note as Parcelable)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, noteIndex)
        startActivityForResult(intent, NoteDetailActivity.REQUEST_EDIT_NOTE)
    }
}
