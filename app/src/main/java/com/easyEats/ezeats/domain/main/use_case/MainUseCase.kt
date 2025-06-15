package com.easyEats.ezeats.domain.main.use_case

import com.easyEats.ezeats.domain.main.use_case.active_order.ActiveOrderUseCase
import com.easyEats.ezeats.domain.main.use_case.home_use_case.HomeUseCase
import com.easyEats.ezeats.domain.main.use_case.location_use_case.LocationUseCase
import com.easyEats.ezeats.domain.main.use_case.main_use_case.MainActivityUseCase
import com.easyEats.ezeats.domain.main.use_case.offers_use_case.OffersUserCase
import com.easyEats.ezeats.domain.main.use_case.order_history_use_case.OrderHistoryUseCase
import com.easyEats.ezeats.domain.main.use_case.points_use_case.PointsUseCase
import com.easyEats.ezeats.domain.main.use_case.redeem.RedeemOfferUseCase
import com.easyEats.ezeats.domain.main.use_case.restaurant_use_case.RestaurantUseCase
import com.easyEats.ezeats.domain.main.use_case.search_history.SearchHistoryUseCase
import com.easyEats.ezeats.domain.main.use_case.search_suggestions.SearchSuggestionsUseCase
import com.easyEats.ezeats.domain.main.use_case.search_use_case.SearchUseCase
import com.easyEats.ezeats.domain.main.use_case.user_info.UserInfoUseCase
import com.easyEats.ezeats.domain.main.use_case.voucher_use_case.VoucherUseCase

data class MainUseCase(
    val mainUseCase: MainActivityUseCase,
    val homeUseCase: HomeUseCase,
    val searchSuggestionsUseCase: SearchSuggestionsUseCase,
    val searchHistoryUseCase: SearchHistoryUseCase,
    val locationUseCase: LocationUseCase,
    val searchUseCase: SearchUseCase,
    val offersUserCase: OffersUserCase,
    val restaurantUseCase: RestaurantUseCase,
    val redeemOfferUseCase: RedeemOfferUseCase,
    val userInfoUseCase: UserInfoUseCase,
    val orderHistoryUseCase: OrderHistoryUseCase,
    val pointsUseCase: PointsUseCase,
    val voucherUseCase: VoucherUseCase,
    val trackActiveOrderUseCase: ActiveOrderUseCase
)