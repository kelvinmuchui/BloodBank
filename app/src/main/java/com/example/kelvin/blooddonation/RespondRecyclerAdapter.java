package com.example.kelvin.blooddonation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kelvin.blooddonation.Model.Responds;

import java.util.List;

public class RespondRecyclerAdapter extends RecyclerView.Adapter<RespondRecyclerAdapter.ViewHolder> {

    public List<Responds> respondsList;
    public Context context;

    public RespondRecyclerAdapter(List<Responds> respondsList){

        this.respondsList = respondsList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.respond_list_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        String commentMessage = respondsList.get(position).getMessage();
        holder.setComment_message(commentMessage);

    }


    @Override
    public int getItemCount() {

        if(respondsList != null) {

            return respondsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView comment_message;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setComment_message(String message){

            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }

    }

}
