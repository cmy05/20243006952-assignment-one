package com.example.assignmentone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * Main screen of the CampusLife application.
 *
 * <p>Shows the campus banner and the four student service areas
 * (Facilities, Events, Clubs and Support). Tapping one of the four
 * {@link ImageButton}s shows a confirmation Toast (Part E) and then opens
 * {@link DetailActivity} through an explicit Intent, passing the index of
 * the tapped topic so the detail screen knows which content to display
 * (Part G).</p>
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The four ImageButtons in the order used by arrays.xml: Facilities,
     * Events, Clubs and Support. Keeping the ids in an array means the click
     * listeners can be attached in a loop instead of repeating the same four
     * blocks of code.
     */
    private static final int[] TOPIC_BUTTON_IDS = {
            R.id.imageButtonFacilities,
            R.id.imageButtonEvents,
            R.id.imageButtonClubs,
            R.id.imageButtonSupport
    };

    /** Short names of the four topics, in the same order as the buttons. */
    private static final int[] TOPIC_LABEL_IDS = {
            R.string.label_facilities,
            R.string.label_events,
            R.string.label_clubs,
            R.string.label_support
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applySystemBarInsets();
        setUpTopicButtons();
    }

    /**
     * The banner is drawn edge to edge behind the status bar, so the bottom of
     * the window is padded by the height of the navigation bar. Without this
     * the topic labels would be partly hidden behind the gesture bar on
     * modern devices.
     */
    private void applySystemBarInsets() {
        View root = findViewById(R.id.rootLayout);
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, 0, bars.right, bars.bottom);
            return windowInsets;
        });

        // The photo behind the status bar is bright, so dark system icons are
        // easier to read here.
        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(true);
    }

    /**
     * Attaches the same click listener to all four ImageButtons. The index of
     * the button is captured in the lambda, so a single method can handle
     * every topic without any if statements or switch cases.
     */
    private void setUpTopicButtons() {
        for (int index = 0; index < TOPIC_BUTTON_IDS.length; index++) {
            final int topicIndex = index;
            ImageButton button = findViewById(TOPIC_BUTTON_IDS[index]);
            button.setOnClickListener(view -> openTopic(topicIndex));
        }
    }

    /**
     * Handles a tap on one of the topic buttons.
     *
     * @param topicIndex index of the topic (0 = Facilities ... 3 = Support)
     */
    private void openTopic(int topicIndex) {
        // Part E: quick feedback that the correct button was tapped.
        String label = getString(TOPIC_LABEL_IDS[topicIndex]);
        Toast.makeText(this, getString(R.string.toast_topic_clicked, label),
                Toast.LENGTH_SHORT).show();

        // Part G: explicit Intent to the detail screen. The index is the
        // unique identifier used by DetailActivity to look up its content,
        // and the label is the short heading displayed in its header.
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_TOPIC_INDEX, topicIndex);
        intent.putExtra(DetailActivity.EXTRA_TOPIC_LABEL, label);
        startActivity(intent);
    }
}
