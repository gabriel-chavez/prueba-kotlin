package com.github.rubensousa.bottomsheetbuilder;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;

public class BottomSheetMenuDialog extends BottomSheetDialog implements BottomSheetItemClickListener {

    BottomSheetBehavior.BottomSheetCallback mCallback;
    BottomSheetBehavior mBehavior;
    private BottomSheetItemClickListener mClickListener;
    private AppBarLayout mAppBarLayout;
    private boolean mExpandOnStart;
    boolean mRequestedExpand;
    boolean mClicked;
    boolean mRequestCancel;
    boolean mRequestDismiss;
    OnCancelListener mOnCancelListener;

    public BottomSheetMenuDialog(Context context) {
        super(context);
    }

    public BottomSheetMenuDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * Dismiss the BottomSheetDialog while animating the sheet.
     */
    public void dismissWithAnimation() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void setOnCancelListener(OnCancelListener listener) {
        super.setOnCancelListener(listener);
        mOnCancelListener = listener;
    }

    @Override
    public void cancel() {
        mRequestCancel = true;
        super.cancel();
    }

    @Override
    public void dismiss() {
        mRequestDismiss = true;
        if (mRequestCancel) {
            dismissWithAnimation();
        } else {
            super.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FrameLayout sheet = findViewById(R.id.design_bottom_sheet);

        if (sheet != null) {
            mBehavior = BottomSheetBehavior.from(sheet);
            mBehavior.setBottomSheetCallback(mBottomSheetCallback);

            if (getContext().getResources().getBoolean(R.bool.tablet_landscape)) {
                CoordinatorLayout.LayoutParams layoutParams
                        = (CoordinatorLayout.LayoutParams) sheet.getLayoutParams();
                layoutParams.width = getContext().getResources()
                        .getDimensionPixelSize(R.dimen.bottomsheet_width);
                sheet.setLayoutParams(layoutParams);
            }

            // Make sure the sheet doesn't overlap the appbar
            if (mAppBarLayout != null) {
                if (mAppBarLayout.getHeight() == 0) {
                    mAppBarLayout.getViewTreeObserver()
                            .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    applyAppbarMargin(sheet);
                                }
                            });
                } else {
                    applyAppbarMargin(sheet);
                }
            }


            if (mExpandOnStart) {
                sheet.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        if (mBehavior.getState() == BottomSheetBehavior.STATE_SETTLING
                                && mRequestedExpand) {
                            sheet.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        mRequestedExpand = true;
                    }
                });
            } else if (getContext().getResources().getBoolean(R.bool.landscape)) {
                fixLandscapePeekHeight(sheet);
            }
        }
    }

    public void setAppBar(AppBarLayout appBar) {
        mAppBarLayout = appBar;
    }

    public void expandOnStart(boolean expand) {
        mExpandOnStart = expand;
    }

    public void setBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback callback) {
        mCallback = callback;
    }

    public void setBottomSheetItemClickListener(BottomSheetItemClickListener listener) {
        mClickListener = listener;
    }

    public BottomSheetBehavior getBehavior() {
        return mBehavior;
    }

    @Override
    public void onBottomSheetItemClick(MenuItem item) {
        if (!mClicked) {

            if (mBehavior != null) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            if (mClickListener != null) {
                mClickListener.onBottomSheetItemClick(item);
            }

            mClicked = true;
        }
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   @BottomSheetBehavior.State int newState) {

            if (mCallback != null) {
                mCallback.onStateChanged(bottomSheet, newState);
            }

            //noinspection WrongConstant
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                mBehavior.setBottomSheetCallback(null);
                try {
                    BottomSheetMenuDialog.super.dismiss();
                } catch (IllegalArgumentException e) {
                    // Ignore exception handling
                }

                // User dragged the sheet.
                if (!mClicked && !mRequestDismiss && !mRequestCancel && mOnCancelListener != null) {
                    mOnCancelListener.onCancel(BottomSheetMenuDialog.this);
                }
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            if (mCallback != null) {
                mCallback.onSlide(bottomSheet, slideOffset);
            }
        }
    };

    private void fixLandscapePeekHeight(final View sheet) {
        // On landscape, we shouldn't use the 16:9 keyline alignment
        final int peek = sheet.getResources()
                .getDimensionPixelOffset(R.dimen.bottomsheet_landscape_peek);
        if (sheet.getHeight() != 0) {
            mBehavior.setPeekHeight(Math.max(sheet.getHeight() / 2, peek));
        } else {
            sheet.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (sheet.getHeight() > 0) {
                        sheet.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        mBehavior.setPeekHeight(Math.max(sheet.getHeight() / 2, peek));
                    }
                }
            });
        }
    }

    private void applyAppbarMargin(View sheet) {
        CoordinatorLayout.LayoutParams layoutParams
                = (CoordinatorLayout.LayoutParams) sheet.getLayoutParams();
        layoutParams.topMargin = mAppBarLayout.getHeight();
        sheet.setLayoutParams(layoutParams);
    }
}
