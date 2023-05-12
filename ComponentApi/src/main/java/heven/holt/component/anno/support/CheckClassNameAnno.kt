package heven.holt.component.anno.support

/**
 * 表示一个类需要注意它的类名改变!
 * [heven.holt.component.ComponentConstants] 这个类中的每一个全类名都应该被关注
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class CheckClassNameAnno(val value: String = "")