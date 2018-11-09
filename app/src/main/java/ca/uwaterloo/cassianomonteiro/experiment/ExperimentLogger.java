package ca.uwaterloo.cassianomonteiro.experiment;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExperimentLogger {

    private StringBuffer buffer = new StringBuffer();
    private long startTime;
    private long pausedTime;
    private long pauseDuration;
    private boolean isPaused     = true;

    private String  participantID;
    private String  runID;
    private boolean backgroundCamera;
    private Context context;

    public ExperimentLogger(Context context, String participantID, String runID, boolean backgroundCamera) {
        this.participantID = participantID;
        this.runID = runID;
        this.backgroundCamera = backgroundCamera;
        this.context = context;
        checkFile();
    }

    public void logGameStart() {
        startTime  = System.currentTimeMillis();
        pausedTime = 0;
        pauseDuration = 0;
        isPaused   = false;
        logScore(0);
    }

    public void logScore(int score) {
        long gameDuration = System.currentTimeMillis() - startTime - pauseDuration;
        buffer.append(participantID + "," + runID + "," + backgroundCamera + "," + gameDuration + "," + score + ",");
        buffer.append(System.getProperty("line.separator"));
    }

    public void logGameEnd() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getLogFile(), true));
            writer.write(buffer.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile() {
        File file = getLogFile();

        if (!file.exists()) {
            // Append title to stringbuffer
            buffer.append("participantID,runID,backgroundCamera,gameDuration,score,");
            buffer.append(System.getProperty("line.separator"));
        }
    }

    private File getLogFile() {
        String fileName = "Participant" + String.format("%02d", Integer.parseInt(participantID)) + ".csv";
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
    }


    /**
     * Accumulate the play time between pause/resume cycles.
     * <p><code>pausedTime</code> is an accumulation of the play time
     * between pause/resume cycles.
     */
    public void logGamePause() {
        pausedTime = System.currentTimeMillis();
        if (!isPaused) {
            isPaused = true;
        }
    }

    public void logGameResume() {
        long currentTime = System.currentTimeMillis();
        isPaused = false;
        pauseDuration += currentTime - pausedTime;
        pausedTime = 0;
    }

}