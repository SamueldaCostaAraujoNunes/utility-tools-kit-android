package com.samuelnunes.data.dto.response.error

import com.samuelnunes.utility_tool_kit.domain.Result


data class NotFoundError(val message: String) : Result.Error.ErrorResponse()