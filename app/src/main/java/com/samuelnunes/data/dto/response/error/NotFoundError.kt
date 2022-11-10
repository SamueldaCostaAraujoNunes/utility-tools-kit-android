package com.samuelnunes.data.dto.response.error

import com.samuelnunes.utility_tool_kit.domain.Resource


data class NotFoundError(val message: String) : Resource.Error.ErrorResponse()