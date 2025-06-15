package com.easyEats.ezeats.domain.main.use_case.voucher_use_case

import com.easyEats.ezeats.domain.main.model.request.edit_user_info.UserInfoRequest
import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class VoucherUseCase @Inject constructor(val mainRepository: MainRepository) {

    suspend fun getUserRewardsMobile() = mainRepository.getUserRewardsMobile()

}