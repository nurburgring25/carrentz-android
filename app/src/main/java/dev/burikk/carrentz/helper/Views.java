package dev.burikk.carrentz.helper;

import android.view.MenuItem;
import android.view.View;

/**
 * @author Muhammad Irfan
 * @since 28/08/2019 13.20
 */
public class Views {
    public static void enable(View... mViews) {
        enable(true, mViews);
    }

    public static void enable(boolean mCondition, View... mViews) {
        if (mViews != null) {
            if (mCondition) {
                for (View mView : mViews) {
                    if (mView != null) {
                        mView.setEnabled(true);
                    }
                }
            }
        }
    }

    public static void disable(View... mViews) {
        disable(true, mViews);
    }

    public static void disable(boolean mCondition, View... mViews) {
        if (mViews != null) {
            if (mCondition) {
                for (View mView : mViews) {
                    if (mView != null) {
                        mView.setEnabled(false);
                    }
                }
            }
        }
    }

    public static void determineEnabled(boolean enabled, View... views) {
        if (enabled) {
            enable(views);
        } else {
            disable(views);
        }
    }

    public static void gone(View... mViews) {
        gone(true, mViews);
    }

    public static void gone(boolean mCondition, View... mViews) {
        if (mViews != null) {
            if (mCondition) {
                for (View mView : mViews) {
                    if (mView != null) {
                        mView.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    public static void visible(View... mViews) {
        visible(true, mViews);
    }

    public static void visible(boolean mCondition, View... mViews) {
        if (mViews != null) {
            if (mCondition) {
                for (View mView : mViews) {
                    if (mView != null) {
                        mView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    public static void invisible(View... mViews) {
        invisible(true, mViews);
    }

    public static void invisible(boolean mCondition, View... mViews) {
        if (mViews != null) {
            if (mCondition) {
                for (View mView : mViews) {
                    if (mView != null) {
                        mView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    public static void toggle(View... views) {
        toggle(true, views);
    }

    public static void toggle(boolean condition, View... views) {
        if (views != null) {
            if (condition) {
                for (View view : views) {
                    if (view != null) {
                        if (view.getVisibility() == View.VISIBLE) {
                            view.setVisibility(View.GONE);
                        } else {
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

    public static void gone(MenuItem... mMenuItems) {
        if (mMenuItems != null) {
            for (MenuItem mMenuItem : mMenuItems) {
                if (mMenuItem != null) {
                    mMenuItem.setVisible(false);
                    mMenuItem.setEnabled(false);
                }
            }
        }
    }

    public static boolean isVisible(View mView) {
        return mView.getVisibility() == View.VISIBLE;
    }

    public static boolean isVisibleAny(View... views) {
        boolean result = false;

        if (views != null) {
            for (View view : views) {
                if (isVisible(view)) {
                    result = true;

                    break;
                }
            }
        }

        return result;
    }
}