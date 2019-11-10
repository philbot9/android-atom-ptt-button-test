package ca.klostermann.pttbuttontest;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class Sound {
  public static final String TAG = Sound.class.getCanonicalName();

  Context mContext;
  int mResource;
  MediaPlayer mMediaPlayer;

  public Sound(Context context, final int resource) {
    mContext = context;
    mResource = resource;
  }

  protected MediaPlayer createMediaPlayer(final int resource) {
    MediaPlayer mediaPlayer = new MediaPlayer();
    try {
      AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(resource);
      mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
      afd.close();
    } catch (IOException e) {
      Log.e(TAG, "Exception", e);
      return null;
    }
    return mediaPlayer;

  }

  public void prepare () {
    mMediaPlayer = createMediaPlayer(mResource);

    Integer category = AudioManager.STREAM_VOICE_CALL;

    mMediaPlayer.setVolume(1.0f, 1.0f);
    mMediaPlayer.setAudioStreamType(category);

    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public synchronized void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "Sound prepared");
      }

    });

    mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
      @Override
      public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "Sound error " + what + " " + extra);
        return true;
      }
    });

    try {
      mMediaPlayer.prepare();
    } catch (Exception ignored) {
      // When loading files from a file, we useMediaPlayer.create, which actually
      // prepares the audio for us already. So we catch and ignore this error
      Log.e(TAG, "Exception", ignored);
    }

  }

  public void play (float volume) {
    if (mMediaPlayer == null) {
      Log.w(TAG, "Player not prepared. Cannot play.");
      return;
    }

    if (mMediaPlayer.isPlaying()) {
      Log.w(TAG, "Player is already playing.");
      return;
    }

    mMediaPlayer.setVolume(volume, volume);

    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public synchronized void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "Playing Sound completed");
      }
    });

    mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
      @Override
      public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "Playing Sound failed " + what + " " + extra);
        return true;
      }
    });

    mMediaPlayer.start();
  }

  public void reset () {
    if (mMediaPlayer != null) {
      mMediaPlayer.reset();
    }
  }

}
