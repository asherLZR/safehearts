package edu.monash.smile.dashboard.statusTab.adapterDelegates;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A generalised version of the view holder to provide common features between all status
 * cards.
 */
abstract class BaseCardViewHolder extends RecyclerView.ViewHolder {
    private final TextView cardHeading;
    private final TextView cardSubheading;

    BaseCardViewHolder(@NonNull View itemView, int cardHeadingId, int cardSubheadingId) {
        super(itemView);
        this.cardHeading = itemView.findViewById(cardHeadingId);
        this.cardSubheading = itemView.findViewById(cardSubheadingId);
    }

    void setUpTitle(String heading, String subheading) {
        cardHeading.setText(heading);
        cardSubheading.setText(subheading);
    }
}