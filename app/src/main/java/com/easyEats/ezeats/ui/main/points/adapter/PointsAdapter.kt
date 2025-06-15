package com.easyEats.ezeats.ui.main.points.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.PointsItemBinding
import com.easyEats.ezeats.domain.main.model.response.loyality_center.LoyaltyPoint
import javax.inject.Inject
/**
 * Adapter class for displaying a list of loyalty points in a RecyclerView.
 * It extends `ListAdapter` to efficiently manage list updates with DiffUtil.
 *
 * @constructor Injected using dependency injection to ensure modularity and testability.
 */
class PointsAdapter @Inject constructor() :
    ListAdapter<LoyaltyPoint, PointsAdapter.PointsViewHolder>(PointDiffUtil()) {

    /**
     * Called when a new ViewHolder needs to be created.
     *
     * @param parent The parent ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return A new PointsViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = PointsItemBinding.inflate(inflater, parent, false) // Inflate the layout using View Binding
        return PointsViewHolder(view)
    }

    /**
     * Called to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item
     *               at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: PointsViewHolder, position: Int) {
        val point = getItem(position) // Get the loyalty point item at the given position
        holder.binding.restaurantPoint = point // Bind the item to the layout
        holder.binding.executePendingBindings() // Execute any pending bindings to update the UI
    }

    /**
     * ViewHolder class to hold and bind the views for each item in the RecyclerView.
     *
     * @param binding The binding object for the PointsItem layout.
     */
    inner class PointsViewHolder(val binding: PointsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * DiffUtil callback class for efficiently calculating the difference between two lists of LoyaltyPoint objects.
     */
    class PointDiffUtil : DiffUtil.ItemCallback<LoyaltyPoint>() {
        /**
         * Called to check whether two items have the same ID.
         *
         * @param oldItem The old item in the list.
         * @param newItem The new item in the list.
         * @return True if the two items have the same ID, false otherwise.
         */
        override fun areItemsTheSame(oldItem: LoyaltyPoint, newItem: LoyaltyPoint): Boolean {
            return oldItem.resturantId == newItem.resturantId
        }

        /**
         * Called to check whether two items have the same contents.
         *
         * @param oldItem The old item in the list.
         * @param newItem The new item in the list.
         * @return True if the contents of the items are the same, false otherwise.
         */
        override fun areContentsTheSame(oldItem: LoyaltyPoint, newItem: LoyaltyPoint): Boolean {
            return oldItem == newItem
        }
    }
}
