package ca.klostermann.pttbuttontest;

import android.content.Context;

public class Sounds {
  Sound pttPressedSound;
  Sound pttReleasedSound;

  public Sounds (Context context) {
    pttPressedSound = new Sound(context, R.raw.ptt_pressed);
    pttPressedSound.prepare();

    pttReleasedSound = new Sound(context, R.raw.ptt_released);
    pttReleasedSound.prepare();
  }

  public void playPttPressed() {
      pttPressedSound.play(1.0f);
  }

  public void playPttReleased() {
      pttReleasedSound.play(1.0f);
  }
}
