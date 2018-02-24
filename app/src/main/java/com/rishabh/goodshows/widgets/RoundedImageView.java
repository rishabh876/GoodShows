package com.rishabh.goodshows.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.rishabh.goodshows.R;

import java.util.EnumSet;

public class RoundedImageView extends AppCompatImageView {

    private final int ALL_ROUNDED_CORNERS_VALUE = 15; //boolean 1111

    public enum Corner {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;

        public static final EnumSet<Corner> ALL = EnumSet.allOf(Corner.class);
        public static final EnumSet<Corner> TOP = EnumSet.of(TOP_LEFT, TOP_RIGHT);
    }

    private Paint paint;
    private Path path;
    private int pathWidth, pathHeight, cornerRadius = 0;

    private boolean roundedTopLeft, roundedBottomLeft, roundedTopRight, roundedBottomRight;

    public RoundedImageView(Context context) {
        super(context);
        init();
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RoundedImageView, 0, 0);
        int cornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_cornerRadius, 0);
        int roundedCorners = a.getInt(R.styleable.RoundedImageView_roundedCorners, ALL_ROUNDED_CORNERS_VALUE);
        a.recycle();
        init();
        setCornerRadius(cornerRadius);
        setRoundedCorners(roundedCorners);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyleAttr, 0);
        int cornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_cornerRadius, 0);
        int roundedCorners = a.getInt(R.styleable.RoundedImageView_roundedCorners, ALL_ROUNDED_CORNERS_VALUE);
        a.recycle();
        init();
        setCornerRadius(cornerRadius);
        setRoundedCorners(roundedCorners);
    }

    private void setRoundedCorners(int roundedCorners) {
        int topLeft = 8; //1000 boolean
        int topRight = 4; //0100 boolean
        int bottomLeft = 2; //0010 boolean
        int bottomRight = 1; //0001 boolean

        roundedTopLeft = topLeft == (roundedCorners & topLeft);
        roundedTopRight = topRight == (roundedCorners & topRight);
        roundedBottomLeft = bottomLeft == (roundedCorners & bottomLeft);
        roundedBottomRight = bottomRight == (roundedCorners & bottomRight);
    }

    private void init() {
        paint = new Paint();
        path = new Path();
        setupPaint();
    }

    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        if (pathWidth != newWidth || pathHeight != newHeight) {
            pathWidth = newWidth;
            pathHeight = newHeight;
            setupPath();
        }
    }

    /**
     * @param cornerRadius in pixels, default is 0
     */
    public void setCornerRadius(int cornerRadius) {
        if (this.cornerRadius != cornerRadius) {
            this.cornerRadius = cornerRadius;
            setupPath();
        }
    }

    /**
     * @param corners, default is All rounded corners
     */
    public void setRoundCorners(EnumSet<Corner> corners) {
        if (roundedBottomLeft != corners.contains(Corner.BOTTOM_LEFT)
                || roundedBottomRight != corners.contains(Corner.BOTTOM_RIGHT)
                || roundedTopLeft != corners.contains(Corner.TOP_LEFT)
                || roundedTopRight != corners.contains(Corner.TOP_RIGHT)) {
            roundedBottomLeft = corners.contains(Corner.BOTTOM_LEFT);
            roundedBottomRight = corners.contains(Corner.BOTTOM_RIGHT);
            roundedTopLeft = corners.contains(Corner.TOP_LEFT);
            roundedTopRight = corners.contains(Corner.TOP_RIGHT);
            setupPath();
        }
    }

    private Paint setupPaint() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.TRANSPARENT);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        return paint;

    }

    private void setupPath() {
        path = roundedRect(path, 0, 0, pathWidth, pathHeight, cornerRadius, cornerRadius,
                           roundedTopLeft, roundedTopRight, roundedBottomRight, roundedBottomLeft);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInEditMode()) {
            int saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
            super.onDraw(canvas);
            canvas.drawPath(path, paint);
            canvas.restoreToCount(saveCount);
        } else {
            super.onDraw(canvas);
        }
    }

    public static Path roundedRect(Path path,
                                   float left, float top, float right, float bottom,
                                   float rx, float ry,
                                   boolean tl, boolean tr, boolean br, boolean bl) {
        path.reset();
        if (rx < 0) {
            rx = 0;
        }
        if (ry < 0) {
            ry = 0;
        }
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) {
            rx = width / 2;
        }
        if (ry > height / 2) {
            ry = height / 2;
        }
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        if (tr) {
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        } else {
            path.rLineTo(0, -ry);
            path.rLineTo(-rx, 0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tl) {
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        } else {
            path.rLineTo(-rx, 0);
            path.rLineTo(0, ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bl) {
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        } else {
            path.rLineTo(0, ry);
            path.rLineTo(rx, 0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (br) {
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        } else {
            path.rLineTo(rx, 0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        return path;
    }
}