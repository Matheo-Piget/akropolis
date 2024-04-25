package util;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a timeline that executes actions at specified intervals.
 * The timeline can be repeated a specified number of times.
 * The timeline is controlled by a timer, so it can be executed asynchronously.
 */
public class Timeline {
    private final Timer timer;
    private final List<KeyFrame> keyFrames;
    private ActionListener onFinished = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };
    private int currentKeyFrameIndex;
    private int repeatCount;

    /**
     * Creates a new timeline with the specified repeat count.
     *
     * @param repeatCount the number of times to repeat the timeline (-1 for
     *                    infinite)
     * @throws IllegalArgumentException if the repeat count is negative
     */
    public Timeline(int repeatCount) {
        // Validate the repeat count
        if (repeatCount < -1) {
            throw new IllegalArgumentException("Repeat count must be non-negative");
        }
        keyFrames = new ArrayList<>();
        currentKeyFrameIndex = 0;
        this.repeatCount = repeatCount;

        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!keyFrames.isEmpty()) {
                    KeyFrame currentKeyFrame = keyFrames.get(currentKeyFrameIndex);
                    currentKeyFrame.action().actionPerformed(e);
                    currentKeyFrame.currentRepeat--;
                    if (currentKeyFrame.currentRepeat == 0) {
                        currentKeyFrameIndex = (currentKeyFrameIndex + 1) % keyFrames.size();
                        if (currentKeyFrameIndex == 0) {
                            if (Timeline.this.repeatCount == 0) {
                                System.out.println("Timeline finished");
                                onFinished.actionPerformed(e);
                                timer.stop();
                                return;
                            }
                            if (Timeline.this.repeatCount > 0) {
                                Timeline.this.repeatCount--;
                            }
                        }
                        if (currentKeyFrameIndex < keyFrames.size()) {
                            timer.stop();
                            timer.setInitialDelay(keyFrames.get(currentKeyFrameIndex).delay());
                            timer.restart();
                        }
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
            timer.setDelay(keyFrame.delay());
        }
    }

    /**
     * Sets the action to execute when the timeline finishes.
     *
     * @param onFinished
     */
    public void setOnFinished(ActionListener onFinished) {
        this.onFinished = onFinished;
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
        // Reset each key frame
        for (KeyFrame keyFrame : keyFrames) {
            keyFrame.reset();
        }
        timer.restart();
    }

    /**
     * Represents a key frame in the timeline.
     * A key frame consists of an action and a delay.
     * The action is executed when the key frame is reached.
     */
    public static class KeyFrame {
        private final int delay;
        private final int repeat;
        int currentRepeat;
        private final ActionListener action;

        /**
         * Creates a new key frame with the specified delay and action.
         *
         * @param delay  the delay before the action is executed
         * @param repeat the number of times to repeat the action
         * @param action the action to execute
         * @throws IllegalArgumentException if the delay is negative
         * @throws IllegalArgumentException if the repeat is negative
         */
        public KeyFrame(int delay, int repeat, ActionListener action) {
            if (delay < 0) {
                throw new IllegalArgumentException("Delay must be non-negative");
            }
            if (repeat < 0) {
                throw new IllegalArgumentException("Repeat must be non-negative");
            }
            this.delay = delay;
            this.repeat = repeat;
            this.action = action;
            currentRepeat = repeat;
        }

        /**
         * Creates a new key frame with the specified delay and action.
         * The action is executed only once.
         *
         * @param delay  the delay before the action is executed
         * @param action the action to execute
         * @throws IllegalArgumentException if the delay is negative
         */
        public KeyFrame(int delay, ActionListener action) {
            this(delay, 1, action);
        }

        /**
         * Resets the key frame.
         */
        public void reset() {
            currentRepeat = repeat;
        }

        /**
         * Gets the action of this key frame.
         *
         * @return the action of this key frame
         */
        public ActionListener action() {
            return action;
        }

        /**
         * Gets the delay of this key frame.
         *
         * @return the delay of this key frame
         */
        public int delay() {
            return delay;
        }
    }
}
