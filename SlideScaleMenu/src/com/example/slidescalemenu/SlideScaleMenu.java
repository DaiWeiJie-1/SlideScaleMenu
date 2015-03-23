package com.example.slidescalemenu;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class SlideScaleMenu extends FrameLayout{
	private float mScaleValue = 0.65f;
	private float mShadowAddScaleValue = 0.08f;
	
	private RelativeLayout mTopLayout;
	private View mShadowView;
	
	private boolean mIsMenuOpened = false;
	private boolean mIsAnimating = false;
	
	private boolean mIsPressDown = false;
	private float mPressDownX = 0;
	private float mTotalCanMoveWidth;
	
	private float mCurrentScaleValue;
	private float mCurrentShadowAddScaleValue;

	public SlideScaleMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews();
		replaceContentView((Activity)context);
	}

	public SlideScaleMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
		replaceContentView((Activity)context);
	}

	public SlideScaleMenu(Context context) {
		super(context);
		initViews();
		replaceContentView((Activity)context);
	}
	
	@Override
    protected boolean fitSystemWindows(Rect insets) {
        this.setPadding(insets.left,insets.top,insets.right,insets.bottom);
        insets.left = insets.top = insets.right = insets.bottom = 0;
        return true;
    }
	
	private void initViews(){
		View backgroundLayout = LayoutInflater.from(getContext()).inflate(R.layout.backgroud_layout, this);
		mShadowView = backgroundLayout.findViewById(R.id.shadow_view);
		mTopLayout = new RelativeLayout(getContext());
		mTopLayout.setBackgroundColor(Color.WHITE);
		addView(mTopLayout);
		
		int width = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		
		//topLayout最后能移动的距离
		mTotalCanMoveWidth = (float)((width - width *  mScaleValue)/2 + (1.5- 0.5) * width/2);
	}
	
	public void replaceContentView(Activity activity){
		
		//get root viewGroup
		ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
		
		//get activity setcontent view
		View contentView = decorView.getChildAt(0);
		
		decorView.removeViewAt(0);
		mTopLayout.addView(contentView);
		decorView.addView(this, 0);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		Log.d("dispatch", ev.getAction() + "");
		if(action == MotionEvent.ACTION_DOWN){
			mIsPressDown = true;
			mPressDownX = ev.getX(); 
		}else if(action == MotionEvent.ACTION_MOVE){
			if(mIsPressDown){
				float distance = ev.getX() - mPressDownX;
				scaleByTouchDistance(distance);
			}
		}else if(action == MotionEvent.ACTION_UP){
//			if(mIsPressDown){
//				float currentScaleValue = getCurrentScaleValue(mTopLayout);
//				if(isMenuOpened()){
//					if(currentScaleValue > mScaleValue + 0.02){
//						closeMenu();
//					}else{
//						openMenu();
//					}
//				}else{
//					if(currentScaleValue < 0.98){
//						openMenu();
//					}else{
//						closeMenu();
//					}
//				}
//				mIsPressDown = false;
//			}
		}
		
		return super.dispatchTouchEvent(ev);
	}
	
	private void scaleByTouchDistance(float distance){
		mTopLayout.setPivotX(mTopLayout.getWidth() * 1.5f);
		mTopLayout.setPivotY(mTopLayout.getHeight()/2);
		
		mShadowView.setPivotX(mTopLayout.getWidth() * 1.5f);
		mShadowView.setPivotY(mTopLayout.getHeight()/2);
		if(isMenuOpened()){
			if(distance >= 0){
				return;
			}
			distance = Math.abs(distance);
			if(distance >= mTotalCanMoveWidth){
				distance = mTotalCanMoveWidth;
			}
			float mCurrentScaleValue = mScaleValue + (1 - mScaleValue) * (distance/mTotalCanMoveWidth);
			float mCurrentShadowAddScaleValue = mShadowAddScaleValue - (distance/mTotalCanMoveWidth);
			mTopLayout.setScaleX(mCurrentScaleValue);
			mTopLayout.setScaleY(mCurrentScaleValue);
			mShadowView.setScaleX(mCurrentShadowAddScaleValue);
			mShadowView.setScaleY(mCurrentShadowAddScaleValue);
			if(mCurrentScaleValue == 1.0){
				setMenuOpenState(false);
			}
		}else{
			if(distance <= 0){
				return;
			}else if(distance >= mTotalCanMoveWidth){
				distance = mTotalCanMoveWidth;
			}
			float mCurrentScaleValue =1- distance/mTotalCanMoveWidth *(1 - mScaleValue);
			float mCurrentShadowAddScaleValue = distance/mTotalCanMoveWidth * mShadowAddScaleValue;
			mTopLayout.setScaleX(mCurrentScaleValue);
			mTopLayout.setScaleY(mCurrentScaleValue);
			mShadowView.setScaleX(mCurrentScaleValue + mCurrentShadowAddScaleValue);
			mShadowView.setScaleY(mCurrentScaleValue + mCurrentShadowAddScaleValue);
			if(mCurrentScaleValue == mScaleValue){
				setMenuOpenState(true);
			}
		}
	}
	
	private float getCurrentScaleValue(View target){
		return target.getScaleX();
	}
	
	private void scaleTargetView(View targetView,float scaleValue,int duration){
		targetView.setPivotX(targetView.getWidth() * 1.5f);
		targetView.setPivotY(targetView.getHeight()/2);
		
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(ObjectAnimator.ofFloat(targetView, "scaleX", scaleValue));
		animatorSet.playTogether(ObjectAnimator.ofFloat(targetView, "scaleY", scaleValue));
		animatorSet.setDuration(duration);
		if(duration == 0){
			
		}else{
			animatorSet.addListener(mAnimatorListener);
		}
		animatorSet.start();
	}
	
	private void openMenu(){
		scaleTargetView(mTopLayout, mScaleValue,400);
		scaleTargetView(mShadowView, mScaleValue + mShadowAddScaleValue,400);
		setMenuOpenState(true);
	}
	
	private void closeMenu(){
		scaleTargetView(mTopLayout, 1f,400);
		scaleTargetView(mShadowView, 1f,400);
		setMenuOpenState(false);
	}
	
	private boolean isMenuOpened(){
		return mIsMenuOpened;
	}
	
	private void setMenuOpenState(boolean isOpen){
		mIsMenuOpened = isOpen;
	}
	
	
	
	AnimatorListener mAnimatorListener = new AnimatorListener() {
		
		@Override
		public void onAnimationStart(Animator animation) {
			mIsAnimating = true;
			
		}
		
		@Override
		public void onAnimationRepeat(Animator animation) {
			
		}
		
		@Override
		public void onAnimationEnd(Animator animation) {
			mIsAnimating = false;
			
		}
		
		@Override
		public void onAnimationCancel(Animator animation) {
			mIsAnimating = false;
			
		}
	};
	
}
