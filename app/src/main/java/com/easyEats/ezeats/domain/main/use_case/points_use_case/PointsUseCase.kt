package com.easyEats.ezeats.domain.main.use_case.points_use_case

import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.points.PointsRequest
import com.easyEats.ezeats.domain.main.model.response.points.PointsResponse
import com.easyEats.ezeats.domain.main.repository.MainRepository
import retrofit2.http.Body
import javax.inject.Inject

class PointsUseCase @Inject constructor(val mainRepository: MainRepository) {
    suspend fun getUserPoints(pointsRequest: PointsRequest): BaseResponse<PointsResponse> {
        return mainRepository.getUserPoints(pointsRequest)
    }

    fun getToken() = mainRepository.getToken()


}