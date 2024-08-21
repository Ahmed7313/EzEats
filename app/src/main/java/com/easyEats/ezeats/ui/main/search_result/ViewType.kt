package com.easyEats.ezeats.ui.main.search_result


/**
 * Enum class representing the different view types in the search results.
 * This is used to distinguish between restaurant and offer views in the UI.
 *
 * @property viewType The string representation of the view type.
 */
enum class ViewType(val viewType: String) {
    /**
     * Represents the restaurant view type.
     */
    Restaurant("restaurant"),

    /**
     * Represents the offer view type.
     */
    Offer("offers");
}
