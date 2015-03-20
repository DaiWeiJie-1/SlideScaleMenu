package com.example.slidescalemenu;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class SlideScaleMenu extends FrameLayout{
	private float mScaleValue = 0.65f;
	private float mShadowAddScaleValue = 0.08f;
	
	private RelativeLayout mTopLayout;
	private View mShadowView;
	
	private boolean mIsMenuOpened = false;
	private boolean mIsAnimating = false;

	public SlideScaleMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews();
		// TODO Auto-generated constructor stub
	}

	public SlideScaleMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
		// TODO Auto-generated constructor stub
	}

	public SlideScaleMenu(Context context) {
		super(context);
		initViews();
	}
	
	private void initViews(){
		//����backgroudlayout����layout��
		View backgroundLayout = LayoutInflater.from(getContext()).inflate(R.layout.backgroud_layout, this);
		mShadowView = backgroundLayout.findViewById(R.id.shadow_view);
		mTopLayout = new RelativeLayout(getContext());
		mTopLayout.setBackgroundColor(Color.WHITE);
		addView(mTopLayout);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		if(action == MotionEvent.ACTION_DOWN){
			if(isMenuOpened()){
				closeMenu();
			}else{
				openMenu();
			}
		}
		return super.dispatchTouchEvent(ev);
	}
	
	private void scaleTargetView(View targetView,float scaleValue){
		targetView.setPivotX(targetView.getWidth() * 1.5f);
		targetView.setPivotY(targetView.getHeight()/2);
		
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(ObjectAnimator.ofFloat(targetView, "scaleX", scaleValue));
		animatorSet.playTogether(ObjectAnimator.ofFloat(targetView, "scaleY", scaleValue));
		animatorSet.setDuration(400);
		animatorSet.addListener(mAnimatorListener);
		animatorSet.start();
	}
	
	private void openMenu(){
		if(mIsAnimating){
			return;
		}
		
		if(!isMenuOpened()){
			scaleTargetView(mTopLayout, mScaleValue);
			scaleTargetView(mShadowView, mScaleValue + mShadowAddScaleValue);
			setMenuOpenState(true);
		}
	}
	
	private void closeMenu(){
		if(mIsAnimating){
			return;
		}
		
		if(isMenuOpened()){
			scaleTargetView(mTopLayout, 1f);
			scaleTargetView(mShadowView, 1f);
			setMenuOpenState(false);
		}
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
			// TODO Auto-generated method stub
			
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