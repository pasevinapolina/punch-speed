package com.artioml.practice.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.artioml.practice.R;
import com.artioml.practice.utils.PunchType;
import com.artioml.practice.models.Result;

import java.util.List;

/**
 * Created by Artiom L on 29.01.2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<Result> history; // Поисковые запросы
    private Context context;

    public HistoryAdapter(Context context, List<Result> history) {
        this.history = history;
        this.context = context;
    }

    // Вложенный субкласс RecyclerView.ViewHolder используется для
    // реализации паттерна View-Holder в контексте RecyclerView--
    // логики повторного использования представлений
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView punchTypeTextView;
        private final TextView dateTextView;
        private final TextView speedTextView;
        private final TextView reactionTextView;
        private final TextView accelerationTextView;
        private final TextView weightTextView;

        private final ImageView handsImageView;
        private final ImageView glovesImageView;
        private final ImageView positionImageView;

        // Настройка объекта ViewHolder элемента RecyclerView
        private ViewHolder(View itemView) {
            super(itemView);

            punchTypeTextView = (TextView) itemView.findViewById(R.id.punchTypeHistoryTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateHistoryTextView);
            speedTextView = (TextView) itemView.findViewById(R.id.speedHistoryTextView);
            reactionTextView = (TextView) itemView.findViewById(R.id.reactionHistoryTextView);
            accelerationTextView = (TextView) itemView.findViewById(R.id.accelerationHistoryTextView);
            weightTextView = (TextView) itemView.findViewById(R.id.weightHistoryTextView);

            handsImageView = (ImageView) itemView.findViewById(R.id.handsHistoryImageView);
            glovesImageView = (ImageView) itemView.findViewById(R.id.glovesHistoryImageView);
            positionImageView = (ImageView) itemView.findViewById(R.id.positionHistoryImageView);
        }
    }

    // Создает новый элемент списка и его объект ViewHolder
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Заполнение макета list_item
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.row_history, parent, false);

        // Создание ViewHolder для текущего элемента
        return (new ViewHolder(view));
    }

    // Назначение текста элемента списка для вывода тега запроса
    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {

        Result currentResult = history.get(position);

        holder.punchTypeTextView.setText(context.getResources().getStringArray(
                R.array.punch_type_list)[currentResult.getPunchType()]);
        String date = currentResult.getDate();
        holder.dateTextView.setText(
                date.substring(6, 8) + "." + date.substring(3, 5) + "." + date.substring(0, 2));
        holder.speedTextView.setText(context.getString(R.string.speed_result, currentResult.getSpeed()));
        holder.reactionTextView.setText(
                context.getString(R.string.reaction_result, currentResult.getReaction()));
        holder.accelerationTextView.setText(Html.fromHtml(
                context.getString(R.string.acceleration_result, currentResult.getAcceleration())));
        holder.weightTextView.setText(currentResult.getGlovesWeight());

        String hand = "ic_" + currentResult.getHand() + "_hand";
        holder.handsImageView.setImageDrawable(ContextCompat.getDrawable(context,
                context.getResources().getIdentifier(hand, "drawable", context.getPackageName())));

        String gloves = "ic_gloves_" + currentResult.getGloves();
        holder.glovesImageView.setImageDrawable(ContextCompat.getDrawable(context,
                context.getResources().getIdentifier(gloves, "drawable", context.getPackageName())));

        if (PunchType.getTypeByValue(currentResult.getGloves()) == PunchType.GLOVES_ON)
            holder.weightTextView.setVisibility(View.VISIBLE);
        else holder.weightTextView.setVisibility(View.GONE);

        String pos = "ic_punch_" +  currentResult.getPosition() + "_step";
        holder.positionImageView.setImageDrawable(ContextCompat.getDrawable(context,
                context.getResources().getIdentifier(pos, "drawable", context.getPackageName())));
    }

    @Override
    public int getItemCount() {
        return history.size();
    }
}
