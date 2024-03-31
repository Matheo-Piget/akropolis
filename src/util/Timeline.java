package util;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a timeline that executes actions at specified intervals.
 * The timeline can be repeated a specified number of times.
 * The timeline is controlled by a timer so it can be executed asynchronously.
 */
public class Timeline {
    private Timer timer;
    private List<KeyFrame> keyFrames;
    private int currentKeyFrameIndex;
    private int repeatCount;

    /**
     * Creates a new timeline with the specified repeat count.
     *
     * @param repeatCount the number of times to repeat the timeline (0 for infinite)
     * @throws IllegalArgumentException if the repeat count is negative
     */
    public Timeline(int repeatCount) {
        // Validate the repeat count
        if (repeatCount < 0) {
            throw new IllegalArgumentException("Repeat count must be non-negative");
        }
        keyFrames = new ArrayList<>();
        currentKeyFrameIndex = 0;
        this.repeatCount = repeatCount;

        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (keyFrames.size() > 0) {
                    KeyFrame currentKeyFrame = keyFrames.get(currentKeyFrameIndex);
                    currentKeyFrame.getAction().actionPerformed(e);
                    currentKeyFrameIndex = (currentKeyFrameIndex + 1) % keyFrames.size();
                    if (currentKeyFrameIndex == 0) {
                        Timeline.this.repeatCount--;
                        if (Timeline.this.repeatCount == 0) {
                            timer.stop();
                        }
                    }
                    if (currentKeyFrameIndex < keyFrames.size()) {
                        timer.setDelay(keyFrames.get(currentKeyFrameIndex).getDelay());
                    }
                }
            }
        });
    }

    /**
     * Adds a key frame to the timeline.
     *
     * @param keyFrame the key frame to add
     */
    public void addKeyFrame(KeyFrame keyFrame) {
        keyFrames.add(keyFrame);
        if (keyFrames.size() == 1) {
            timer.setDelay(keyFrame.getDelay());
        }
    }

    /**
     * Starts the timeline.
     */
    public void start() {
        timer.start();
    }

    /**
     * Stops the timeline.
     */
    public void stop() {
        timer.stop();
    }

    /**
     * Resets the timeline.
     */
    public void reset() {
        currentKeyFrameIndex = 0;
        timer.restart();
    }

    /**
     * Represents a key frame in the timeline.
     * A key frame consists of an action and a delay.
     * The action is executed when the key frame is reached.
     */
    public static class KeyFrame {
        private ActionListener action;
        private int delay;

        /**
         * Creates a new key frame with the specified delay and action.
         *
         * @param delay the delay before the action is executed
         * @param action the action to execute
         */
        public KeyFrame(int delay, ActionListener action) {
            this.action = action;
            this.delay = delay;
        }

        /**
         * Gets the action of this key frame.
         *
         * @return the action of this key frame
         */
        public ActionListener getAction() {
            return action;
        }

        /**
         * Gets the delay of this key frame.
         *
         * @return the delay of this key frame
         */
        public int getDelay() {
            return delay;
        }
    }
}
