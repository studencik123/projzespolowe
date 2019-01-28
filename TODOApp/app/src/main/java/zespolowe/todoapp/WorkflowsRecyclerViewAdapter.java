package zespolowe.todoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zespolowe.todoapp.dbo.Workflow;

public class WorkflowsRecyclerViewAdapter extends RecyclerView.Adapter<WorkflowsRecyclerViewAdapter.WorkflowViewHolder>
{
    private LayoutInflater mInflater;
    private WorkflowsRecyclerViewAdapter.ItemClickListener mClickListener;

    public List<Workflow> workflows;

    public class WorkflowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView workflowName, workflowText;

        public WorkflowViewHolder(View view)
        {
            super(view);
            workflowName = (TextView) view.findViewById(R.id.workflowName);
            //workflowText = (TextView) view.findViewById(R.id.taskName);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Data is passed into the constructor
    WorkflowsRecyclerViewAdapter(Context context, List<Workflow> workflows)
    {
        this.mInflater = LayoutInflater.from(context);
        this.workflows = workflows;
    }

    // Inflates the row layout from xml when needed
    @Override
    public WorkflowsRecyclerViewAdapter.WorkflowViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.workflowsrecyclerview_row, parent, false);
        return new WorkflowsRecyclerViewAdapter.WorkflowViewHolder(view);
    }

    // Binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(WorkflowsRecyclerViewAdapter.WorkflowViewHolder holder, int position)
    {
        Workflow workflow = workflows.get(position);
        holder.workflowName.setText(workflow.name);
        //holder.workflowText.setText(workflow.workflow);
    }

    // Total number of rows
    @Override
    public int getItemCount() {
        return workflows.size();
    }


    // Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView myTextView;

        ViewHolder(View itemView)
        {
            super(itemView);
            myTextView = itemView.findViewById(R.id.workflowName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Allows clicks events to be caught
    void setClickListener(WorkflowsRecyclerViewAdapter.ItemClickListener itemClickListener)
    {
        this.mClickListener = itemClickListener;
    }

    // Parent activity will implement this method to respond to click events
    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }
}
