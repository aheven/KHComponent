package heven.holt.component.anno.router

/**
 * userInfo 表示 Android 中的 Uri 的 UserInfo
 * host 表示 Android 中的 Uri 的 host
 * userInfo + @ + host = Android 中的 Uri 的 authority
 * 比如 root@hevenholt.com
 * userInfo = "root"
 * host = "hevenholt.com"
 * authority = "root@hevenholt.com"
 *
 * @see SchemeAnno
 * @see UserInfoAnno
 * @see HostAnno
 * @see PathAnno
 * @see HostAndPathAnno
 */
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.BINARY)
annotation class UserInfoAnno(val value: String)