package com.artioml.practice.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artioml.practice.R;
import com.artioml.practice.models.Result;

import java.util.List;

/**
 * Created by user on 07.02.2017.
 */

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

    private final List<Result> communityResults;
    private Context context;

    public CommunityAdapter(Context context, List<Result> communityResults) {
        this.communityResults = communityResults;
        this.context = context;
    }

    @Override
    public CommunityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.row_community, parent, false);

        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(CommunityAdapter.ViewHolder holder, int position) {
        Result currentResult = communityResults.get(position);

        holder.topNumberTextView.setText(position + 1 + "");
        holder.loginTextView.setText(currentResult.getUser());
        holder.speedTextView.setText(context.getString(R.string.speed_result, currentResult.getSpeed()));
        holder.reactionTextView.setText(
                context.getString(R.string.reaction_result, currentResult.getReaction()));
        holder.accelerationTextView.setText(Html.fromHtml(
                context.getString(R.string.acceleration_result, currentResult.getAcceleration())));
    }

    @Override
    public int getItemCount() {
        return communityResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView topNumberTextView;
        private final TextView loginTextView;
        private final TextView speedTextView;
        private final TextView reactionTextView;
        private final TextView accelerationTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.accelerationTextView = (TextView)itemView.findViewById(R.id.accelerationCommunityTextView);
            this.reactionTextView = (TextView)itemView.findViewById(R.id.reactionCommunityTextView);
            this.speedTextView = (TextView)itemView.findViewById(R.id.speedCommunityTextView);
            this.loginTextView = (TextView)itemView.findViewById(R.id.loginCommunityTextView);
            this.topNumberTextView = (TextView)itemView.findViewById(R.id.topNumberTextView);
        }
    }
}
