package de.xzise.bukkit.util.callback;

/**
 * <p><strong>Do not use it externally!</strong></p>
 * <p>This wrapper will be removed with the {@link de.xzise.Callback} interface.</p>
 * @deprecated You may should use {@link Callback}!
 */
@Deprecated
public class OldCallbackWrapper<R, P> implements Callback<R, P> {
    
    private final de.xzise.Callback<R, ? super P> c;
    
    public OldCallbackWrapper(final de.xzise.Callback<R, ? super P> c) {
        this.c = c;
    }

    @Override
    public R call(P parameter) {
        return this.c.call(parameter);
    }

}
