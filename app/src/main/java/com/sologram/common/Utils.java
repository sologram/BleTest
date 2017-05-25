package com.sologram.common;

import android.content.Context;
import android.media.AudioManager;
import android.view.SoundEffectConstants;

/**
 * Created by hans on 2015/12/16.
 */

public class Utils {
	private static AudioManager ado;

	public static void deinit() {
	}

	public static void init(Context context) {
		ado = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	public static void soundClick() {
		ado.playSoundEffect(SoundEffectConstants.CLICK);
	}
}
