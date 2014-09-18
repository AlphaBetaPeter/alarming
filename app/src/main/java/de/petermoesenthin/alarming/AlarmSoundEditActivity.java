/*
 * Copyright (C) 2014 Peter Mösenthin <peter.moesenthin@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.petermoesenthin.alarming;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import de.petermoesenthin.alarming.util.PrefUtil;

public class AlarmSoundEditActivity extends Activity{

    //================================================================================
    // Members
    //================================================================================

    public static final String DEBUG_TAG = "AlarmSoundEditActivity";
    private static final boolean D = true;

    private String alarmUri;

    //================================================================================
    // Lifecycle
    //================================================================================

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmsoundedit);
        Intent intent = getIntent();
        int audioId = intent.getIntExtra("audio_id",-1);
        if (audioId == -1) {
            if (D) {Log.d(DEBUG_TAG, "Preferences changed");}
            return;
        } else {
            alarmUri = PrefUtil.getAlarmSoundUris(this)[audioId];
            Toast.makeText(this,alarmUri,Toast.LENGTH_SHORT).show();
        }
    }


    private void loadAudioMetaData(){

    }

}
