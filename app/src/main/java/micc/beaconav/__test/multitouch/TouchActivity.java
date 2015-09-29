//package micc.beaconav.test.multitouch;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.graphics.PointF;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.view.Display;
//import android.view.MotionEvent;
//import android.view.ScaleGestureDetector;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.widget.ImageView;
//
//import micc.beaconav.R;
//import micc.beaconav.indoorEngine.IndoorMap;
//import micc.beaconav.indoorEngine.building.building;
//import micc.beaconav.indoorEngine.building.Floor;
//import micc.beaconav.test.multitouch.gesturedetectors.MoveGestureDetector;
//import micc.beaconav.test.multitouch.gesturedetectors.RotateGestureDetector;
//
///**
//* Test activity for testing the different GestureDetectors.
//*
//* @author Almer Thie (code.almeros.com)
//* Copyright (c) 2013, Almer Thie (code.almeros.com)
//*
//* All rights reserved.
//*
//* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
//*
//*  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
//*  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
//*  in the documentation and/or other materials provided with the distribution.
//*
//* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//* INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
//* IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
//* OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
//* OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
//* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
//* OF SUCH DAMAGE.
//*/
//public class TouchActivity extends Activity implements OnTouchListener
//{
//
//	private Matrix mMatrix = new Matrix();
//    private float mScaleFactor = .4f;
//    private float mRotationDegrees = 0.f;
//    private float mFocusX = 0.f;
//    private float mFocusY = 0.f;
//    private int mAlpha = 255;
//    private int mImageHeight, mImageWidth;
//
//    private float zoomTranslationX = 0.f;
//    private float zoomTranslationY = 0.f;
//
//    private ScaleGestureDetector mScaleDetector;
//    private RotateGestureDetector mRotateDetector;
//    private MoveGestureDetector mMoveDetector;
//
//	@SuppressWarnings("deprecation")
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//    {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.fragment_indoor_map);
//
//		// Determine the center of the screen to center 'earth'
//		Display display = getWindowManager().getDefaultDisplay();
//		mFocusX = display.getWidth()/2f;
//		mFocusY = display.getHeight()/2f;
//
//		// Set this class as touchListener to the ImageView
//        ImageView imgView = (ImageView) findViewById(R.id.imageView);
//        imgView.setOnTouchListener(this);
//
////        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.indoor_map);
//
//        imgView.setOnTouchListener(this);
//
//
//
//        Bitmap floor0_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.indoor_map);
//        building building = new building(500,500);
//        Floor floor0 = new Floor();
//        Floor floor1 = new Floor();
//
//        building.add(floor0);
//        building.add(floor1);
//
//
//        IndoorMap indoorMap = new IndoorMap(building);
//        Bitmap frameBmp = indoorMap.drawMapBmp();
//        imgView.setImageDrawable(new BitmapDrawable(getResources(), frameBmp));
//
//
//        //Canvas canvas = new Canvas(bm.copy(Bitmap.Config.ARGB_8888, true));
//
////        Paint paint = new Paint();
////        paint.setStyle(Paint.Style.FILL);
////        paint.setColor(Color.RED);
////        //canvas.drawPaint(paint);
//
//
//
//    //Create a new image bitmap and setManager a brand new canvas to it
//
//    /*    building building = new building(500, 220);
//        Bitmap floor0 = BitmapFactory.decodeResource(getResources(), R.drawable.indoor_map);
//        building.addFloor(floor0, new Position(0,0));
//        IndoorMap indoorMapHere = new IndoorMap(building);
//
//        Bitmap frameBmp = indoorMapHere.drawMapBmp();
//*/
//
////
////    //Draw everything else you want into the canvas, in this example a rectangle with rounded edges
////        tempCanvas.drawRoundRect(new RectF(30,30,300,500), 2, 2, paint);
//
//    //Attach the canvas to the ImageView
///*
//        imgView.setImageDrawable(new BitmapDrawable(getResources(), frameBmp));
//*/
//
//
//
//
//
//
//        mImageWidth = (int)building.getWidth();
//        mImageHeight = (int) building.getHeight();
//
//
//	//	View is scaled and translated by matrix, so scale and translate initially
//        float scaledImageCenterX = (mImageWidth*mScaleFactor)/2;
//        float scaledImageCenterY = (mImageHeight*mScaleFactor)/2;
//
//		mMatrix.postScale(mScaleFactor, mScaleFactor);
//		mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);
//		imgView.setImageMatrix(mMatrix);
//
//		// Setup Gesture Detectors
//		mScaleDetector 	= new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
//		mRotateDetector = new RotateGestureDetector(getApplicationContext(), new RotateListener());
//		mMoveDetector 	= new MoveGestureDetector(getApplicationContext(), new MoveListener());
//	}
//
//
//
//    private void generateFrame()
//    {
//        setContentView(R.layout.fragment_indoor_map);
//        ImageView imgView = (ImageView) findViewById(R.id.imageView);
//        imgView.setOnTouchListener(this);
//
//
//
//        Bitmap floor0_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.indoor_map);
//        building building = new building(500,500);
//        Floor floor0 = new Floor( );
//        Floor floor1 = new Floor( );
//
//        building.add(0, floor0);
//        building.add(1, floor1);
//
//
//        IndoorMap indoorMap = new IndoorMap(building);
//        Bitmap frameBmp = indoorMap.drawMapBmp();
//        imgView.setImageDrawable(new BitmapDrawable(getResources(), frameBmp));
//
//    }
//
//
//
//
//
//
//	@SuppressWarnings("deprecation")
//	public boolean onTouch(View v, MotionEvent event)
//    {
//        mScaleDetector.onTouchEvent(event);
//        mRotateDetector.onTouchEvent(event);
//        mMoveDetector.onTouchEvent(event);
//
//
//        //event.getY();
//        float scaledImageCenterX = (mImageWidth*mScaleFactor)/2;
//        float scaledImageCenterY = (mImageHeight*mScaleFactor)/2;
//
//        mMatrix.reset();
//        mMatrix.postScale(mScaleFactor, mScaleFactor, zoomTranslationX, zoomTranslationY);
//        //mMatrix.postRotate(mRotationDegrees,  scaledImageCenterX, scaledImageCenterY);
//       // mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);
//
//		ImageView view = (ImageView) v;
//		view.setImageMatrix(mMatrix);
//		//view.setAlpha(mAlpha);
//
//		return true; // indicate event was handled
//	}
//
//
//
//
//
//	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//		@Override
//		public boolean onScale(ScaleGestureDetector detector)
//        {
//            float oldScaleFactor = mScaleFactor;
//			mScaleFactor *= detector.getScaleFactor(); // scale change since previous event
//			// Don't let the object get too small or too large.
//			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
//
//            float scaleDifference = mScaleFactor / oldScaleFactor;
//            float zoomTranslationX = (1 - scaleDifference) * detector.getFocusX() / mScaleFactor;
//            float zoomTranslationY = (1 - scaleDifference) * detector.getFocusY() / mScaleFactor;
//
//
//
//			return true;
//		}
//	}
//
//	private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
//		@Override
//		public boolean onRotate(RotateGestureDetector detector) {
//			mRotationDegrees -= detector.getRotationDegreesDelta();
//			return true;
//		}
//	}
//
//	private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
//		@Override
//		public boolean onMove(MoveGestureDetector detector) {
//			PointF d = detector.getFocusDelta();
//			mFocusX += d.x;
//			mFocusY += d.y;
//
////			mFocusX = detector.getFocusX();
////			mFocusY =  detector.getFocusY();
//			return true;
//		}
//	}
//
//
//
//
//}