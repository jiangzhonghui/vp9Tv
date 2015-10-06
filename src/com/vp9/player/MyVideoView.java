package com.vp9.player;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class MyVideoView extends VideoView
{
  public MyVideoView(Context paramContext)
  {
    super(paramContext);
  }

  public MyVideoView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public MyVideoView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public boolean canPause()
  {
    return true;
  }

  public boolean canSeekBackward()
  {
    return true;
  }

  public boolean canSeekForward()
  {
    return true;
  }
}
