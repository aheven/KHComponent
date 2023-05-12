package heven.holt.component.anno.router

/**
 * 给 Intent 添加一些 category
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS,
)
@Retention(AnnotationRetention.BINARY)
annotation class CategoryAnno(
    vararg val value: String
)
