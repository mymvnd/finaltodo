package minh.tdtu.todolistapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.autofill.ImageTransformation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private ArrayList<Note> NoteList;
    private Context context;
    Database database;
    private int colorInt;

    private static final String TABLE_NOTE = "NOTES";
    private static final String COLUMN_STATUS = "NOTE_STATUS";

    public NoteAdapter(ArrayList<Note> noteList, MainActivity context) {
        this.NoteList = noteList;
        this.context = context;
    }

    public void setNoteList(ArrayList<Note> noteList) {
        NoteList = noteList;
    }

    public Note getNote(int position){
        return NoteList.get(position);
    }

    public ArrayList<Note> getNoteList() {
        return NoteList;
    }

    public void removeNote(Note note){
        NoteList.remove(note);
        notifyItemRemoved(NoteList.indexOf(note));
        notifyItemRangeChanged(NoteList.indexOf(note), NoteList.size());
    }
    public void removeNote(int position){
        Note note = NoteList.get(position);
        NoteList.remove(note);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, NoteList.size());
    }

    public void addNote(Note note){
        NoteList.add(note);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_list,parent,false);
        return new NoteViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = NoteList.get(position);
        if(note == null){
            return;
        }

        holder.title.setText(note.getTitle());
        holder.date.setText(note.getDate());
        //holder.time.setText(note.getTime());
        holder.status.setText(note.getStatus());
        ///holder.content.setText(note.getContent());

        //holder.itemView.setBackgroundColor(holder.NoteLinearLayout.getResources().getColor(getRandomColor(),null));

        if(note.getStatus().equals("Late")){
            holder.itemView.setBackgroundResource(R.drawable.rounded_rectangle_late);

            //holder.itemView.setBackgroundColor(holder.NoteLinearLayout.getResources().getColor(R.color.LateNote,null));
        }
        if(note.getStatus().equals("Wait")){
            holder.itemView.setBackgroundResource(R.drawable.rounded_rectangle_wait);

            //holder.itemView.setBackgroundColor(holder.NoteLinearLayout.getResources().getColor(R.color.WaitingNote,null));
        }
        if(note.getStatus().equals("Done")){
            //holder.itemView.setBackgroundColor(holder.NoteLinearLayout.getResources().getColor(R.color.teal_200,null));
            holder.itemView.setBackgroundResource(R.drawable.rounded_rectangle);

        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorBGr = getRandomColor();
                Database db = new Database(context);
                //holder.itemView.setBackgroundColor(holder.NoteLinearLayout.getResources().getColor(colorBGr,null));
                holder.itemView.setBackgroundResource(colorBGr);
                if(colorBGr == R.drawable.rounded_rectangle_late){
                    note.setStatus("Late");
                }
                if(colorBGr == R.drawable.rounded_rectangle_wait ){
                    note.setStatus("Wait");
                }
                if(colorBGr == R.drawable.rounded_rectangle){
                    note.setStatus("Done");
                }
                db.updateNote(note);
                notifyDataSetChanged();
                //db.QueryData(" UPDATE "+ TABLE_NOTE +" SET  "+ COLUMN_STATUS + " = " + note.getStatus()+ " WHERE ID = " + (note.getId()+ "")+";" );

            }
        });

        holder.title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                onClickGoToDetails(note);
                return false;
            }
        });

    }
    public int getRandomColor(){
        List<Integer> color = new ArrayList<>();
        color.add(R.drawable.rounded_rectangle_late);
        color.add(R.drawable.rounded_rectangle_wait);
        color.add(R.drawable.rounded_rectangle);

        int pickedColor = (color.size()-1) - colorInt;
        colorInt++;

        if(colorInt == color.size()){ colorInt = 0; }
        return color.get(pickedColor);
    }

    private void onClickGoToDetails(Note note){
        Intent intent = new Intent(context, Activity_Edit.class);
        Bundle bundle = new Bundle();
        int REQUEST_CODE = 9;
        bundle.putSerializable("NoteDetails", note);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
    @Override
    public int getItemCount() {
        if(NoteList != null){
            return NoteList.size();
        }
        else return 0;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        TextView time;
        TextView status;
        TextView content;
        LinearLayout NoteLinearLayout;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            title =itemView.findViewById(R.id.tv_name);
            date = itemView.findViewById(R.id.tv_date);
            //time = itemView.findViewById(R.id.tv_time);
            status = itemView.findViewById(R.id.tv_status);
            //content = itemView.findViewById(R.id.tv_content);
            NoteLinearLayout = itemView.findViewById(R.id.NoteLinearLayout);

        }
        public void setNote(Note note) {
            title.setText(note.getTitle());
            date.setText(note.getDate());
            status.setText(note.getStatus());
        }
    }

}
