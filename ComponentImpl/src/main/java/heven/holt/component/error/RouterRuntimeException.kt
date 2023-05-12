package heven.holt.component.error

/**
 * 路由框架的运行时异常, 需要奔溃给用户看, 不能处理的
 */
class RouterRuntimeException(
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)