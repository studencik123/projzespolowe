package zespolowe.todoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zespolowe.todoapp.dbo.Task;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TaskViewHolder>
{

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public List<Task> tasks;

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView taskName, taskType, taskNotes, taskDate;

        public TaskViewHolder(View view)
        {
            super(view);
            taskName = (TextView) view.findViewById(R.id.taskName);
            taskType = (TextView) view.findViewById(R.id.taskType);
            taskDate = (TextView) view.findViewById(R.id.taskDate);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Data is passed into the constructor
    TasksRecyclerViewAdapter(Context context, List<Task> tasks)
    {
        this.mInflater = LayoutInflater.from(context);
        this.tasks = tasks;
    }

    // Inflates the row layout from xml when needed
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.tasksrecyclerview_row, parent, false);
        return new TaskViewHolder(view);
    }

    // Binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position)
    {
        Task task = tasks.get(position);
        holder.taskName.setText(task.subject);
        holder.taskType.setText("task.getTaskType()");
        holder.taskDate.setText("task.getTaskDate()");
    }

    // Total number of rows
    @Override
    public int getItemCount() {
        return tasks.size();
    }


    // Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView myTextView;

        ViewHolder(View itemView)
        {
            super(itemView);
            myTextView = itemView.findViewById(R.id.taskName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    //String getItem(int id)
    //{
      //  return tasks.get(id);
    //}

    // Allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener)
    {
        this.mClickListener = itemClickListener;
    }

    // Parent activity will implement this method to respond to click events
    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }
}
