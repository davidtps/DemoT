package com.hisense.storemode.manager.play.strategy;

/**
 * Created by tianpengsheng on 2019年04月22日 19时31分.
 * strategy  context
 */
public class StrategyContext {

    private IPlayStrategy mIplayStrategy;

    public StrategyContext() {
    }

    public StrategyContext(IPlayStrategy playStrategy) {
        mIplayStrategy = playStrategy;
    }

    public void contextMethod() {

        mIplayStrategy.playNext();
    }

}
