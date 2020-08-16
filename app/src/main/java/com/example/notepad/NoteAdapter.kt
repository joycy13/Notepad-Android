package com.example.notepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter (val notes: List<Note>,
                   val itemClickListener: View.OnClickListener) :
                    RecyclerView.Adapter<NoteAdapter.viewHolder>() {
    class viewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val cardView = itemview.findViewById<CardView>(R.id.card_view) as CardView
        val titleView = cardView.findViewById<TextView>(R.id.title) as TextView
        val excerptView = cardView.findViewById<TextView>(R.id.excerpt) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return viewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val note = notes[position]
        holder.cardView.setOnClickListener(itemClickListener)
        holder.cardView.tag = position
        holder.titleView.text = note.title
        holder.excerptView.text = note.text
    }

    override fun getItemCount(): Int {
        return notes.size
    }

}

