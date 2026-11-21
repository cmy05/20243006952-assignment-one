package com.example.assignmentone;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * Detail screen of the CampusLife application.
 *
 * <p>The screen is opened from {@link MainActivity} with an explicit Intent
 * that carries the index of the tapped topic. That index is used to look up
 * the matching entry in the three arrays stored in {@code arrays.xml} - the
 * title, the body text and the icon file name - and the widgets are then
 * populated directly from those arrays (Part H).</p>
 *
 * <p>No if statements or switch cases are used anywhere: array indexing does
 * all of the work, so adding a fifth topic would only require new entries in
 * {@code arrays.xml} and a new button, not new branching logic.</p>
 */
public class DetailActivity extends AppCompatActivity {

    /** Intent extra holding the index of the tapped topic (0 - 3). */
    public static final String EXTRA_TOPIC_INDEX =
            "com.example.assignmentone.EXTRA_TOPIC_INDEX";

    /** Intent extra holding the short name shown in the header. */
    public static final String EXTRA_TOPIC_LABEL =
            "com.example.assignmentone.EXTRA_TOPIC_LABEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        applySystemBarInsets();

        Resources resources = getResources();

        // The three arrays described in the assignment: titles, body text and
        // the names of the matching icon drawables.
        String[] titles = resources.getStringArray(R.array.string_array_titles);
        String[] contents = resources.getStringArray(R.array.string_array_content);
        String[] imageNames = resources.getStringArray(R.array.string_array_images);

        // The index sent by MainActivity, normalised into the array bounds so
        // that a missing or out-of-range extra can never crash the screen.
        int requestedIndex = getIntent().getIntExtra(EXTRA_TOPIC_INDEX, 0);
        int topicIndex = Math.floorMod(requestedIndex, titles.length);

        TextView topicName = findViewById(R.id.textViewTopicName);
        TextView detailTitle = findViewById(R.id.textViewDetailTitle);
        TextView detailContent = findViewById(R.id.textViewDetailContent);
        ImageView topicIcon = findViewById(R.id.imageViewTopicIcon);

        // Populate the header: short name and the matching large icon.
        topicName.setText(getIntent().getStringExtra(EXTRA_TOPIC_LABEL));
        topicIcon.setImageResource(findDrawableId(resources, imageNames[topicIndex]));

        // Populate the scrollable body: heading and paragraph text.
        detailTitle.setText(titles[topicIndex]);
        detailContent.setText(contents[topicIndex]);
    }

    /**
     * Pads the window by the navigation bar inset so the end of the body text
     * is never hidden behind it, and keeps the status bar icons light because
     * the header photo is covered by a dark scrim.
     */
    private void applySystemBarInsets() {
        View root = findViewById(R.id.rootLayout);
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, 0, bars.right, bars.bottom);
            return windowInsets;
        });

        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(false);
    }

    /**
     * Resolves a drawable name from arrays.xml to its resource id.
     *
     * @param resources     resources of this application
     * @param drawableName  file name of the icon, as listed in arrays.xml
     * @return the drawable resource id, or 0 if the name does not exist
     */
    private int findDrawableId(Resources resources, String drawableName) {
        // The entries in arrays.xml are padded with spaces, so trim first.
        String cleanedName = drawableName.trim();
        return resources.getIdentifier(cleanedName, "drawable", getPackageName());
    }
}
