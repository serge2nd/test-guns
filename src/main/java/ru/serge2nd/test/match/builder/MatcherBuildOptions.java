package ru.serge2nd.test.match.builder;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * A mixin for {@link Matcher} builders supporting the certain building options.
 * @param <Builder> the matcher builder actual type
 */
public interface MatcherBuildOptions<Builder extends MatcherBuildOptions<? super Builder>> {

    /** Do not append the next matcher to description. */
    int END_DESC   = 1 << 4;

    /** Do not call {@link Matcher#matches(Object) matches()} on the next matcher. */
    int END_MATCH  = 1 << 7;

    /** Call {@link Matcher#describeMismatch(Object, Description) describeMismatch()} on the next matcher even when the mismatch was detected by the <i>current</i> matcher.
      * Ignored if {@link #END_ALERT} is set. */
    int FALL_ALERT = 1 << 10;

    /** Do not call {@link Matcher#describeMismatch(Object, Description) describeMismatch()} on the next matcher. */
    int END_ALERT  = 1 << 13;

    /** Always call the current matcher's {@link Matcher#describeMismatch(Object, Description) describeMismatch()}. */
    int FORCE_ALERT = 1 << 16;

    int     options();
    Builder set(int options);

    default boolean is(int options) { return (options() & options) == options; }

    /** Sets {@link #END_DESC}. */
    default Builder endDesc()    { return set(END_DESC); }
    /** Sets {@link #END_MATCH}. */
    default Builder endMatch()   { return set(END_MATCH); }
    /** Sets {@link #END_MATCH} | {@link #FALL_ALERT}. */
    default Builder stopMatch()  { return set(END_MATCH | FALL_ALERT); }
    /** Sets {@link #FALL_ALERT}. */
    default Builder fallAlert()  { return set(FALL_ALERT); }
    /** Sets {@link #FALL_ALERT} | {@link #FORCE_ALERT}. */
    default Builder longAlert()  { return set(FALL_ALERT | FORCE_ALERT); }
    /** Sets {@link #END_ALERT}. */
    default Builder endAlert()   { return set(END_ALERT); }
    /** Sets {@link #FORCE_ALERT}. */
    default Builder forceAlert() { return set(FORCE_ALERT); }
}
