package meta;

/**
 *
 * @author Walter Grönholm
 */
public class Pair<F, S> {

    public final F first;
    public final S second;

    private Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
    
    public static <F1, S1> Pair<F1, S1> of(F1 first, S1 second) {
        return new Pair(first, second);
    }
}
