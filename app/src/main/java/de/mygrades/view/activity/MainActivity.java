package de.mygrades.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.greenrobot.event.EventBus;
import de.mygrades.R;
import de.mygrades.database.dao.GradeEntry;
import de.mygrades.main.MainServiceHelper;
import de.mygrades.main.events.ErrorEvent;
import de.mygrades.main.events.GradesEvent;
import de.mygrades.util.Constants;
import de.mygrades.view.adapter.GradesRecyclerViewAdapter;
import de.mygrades.view.adapter.model.GradeItem;
import de.mygrades.view.decoration.GradesDividerItemDecoration;

/**
 * Activity to show the overview of grades.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView rvGrades;
    private GradesRecyclerViewAdapter adapter;

    private MainServiceHelper mainServiceHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static final String EXTRA_INITIAL_LOADING = "initial_loading"; // initial loading after login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkLogin()) {
            goToUniversitySelection();
            return;
        }

        setContentView(R.layout.activity_main);
        mainServiceHelper = new MainServiceHelper(this);

        // register event bus
        EventBus.getDefault().register(this);

        // init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // init recycler view
        initGradesRecyclerView();

        // init swipe to refresh layout
        initSwipeToRefresh();

        boolean initialLoading = getIntent().getBooleanExtra(EXTRA_INITIAL_LOADING, false);
        if (initialLoading) {
            getIntent().removeExtra(EXTRA_INITIAL_LOADING);
            swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        } else {
            mainServiceHelper.getGradesFromDatabase();
        }
    }

    /**
     * Initialize the SwipeRefreshLayout.
     */
    private void initSwipeToRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainServiceHelper.scrapeForGrades();
            }
        });
    }

    /**
     * Initialize recycler view and add dummy items.
     */
    private void initGradesRecyclerView() {
        rvGrades = (RecyclerView) findViewById(R.id.rv_grades);
        rvGrades.setLayoutManager(new LinearLayoutManager(rvGrades.getContext()));
        rvGrades.addItemDecoration(new GradesDividerItemDecoration(this, R.drawable.grade_divider, R.drawable.semester_divider));
        rvGrades.setItemAnimator(new DefaultItemAnimator());

        // set adapter
        adapter = new GradesRecyclerViewAdapter(this);
        rvGrades.setAdapter(adapter);
    }

    /**
     * Receive an GradesEvent and add all grades to the adapter.
     *
     * @param gradesEvent - grades event
     */
    public void onEventMainThread(GradesEvent gradesEvent) {
        if (adapter != null) {
            for(GradeEntry gradeEntry : gradesEvent.getGrades()) {
                GradeItem item = new GradeItem();
                item.setName(gradeEntry.getName());
                item.setHash(gradeEntry.getHash());

                Double creditPoints = gradeEntry.getCreditPoints();
                item.setCreditPoints(creditPoints == null ? null : creditPoints.floatValue());

                Double grade = gradeEntry.getGrade();
                item.setGrade(grade == null ? null : grade.floatValue());

                adapter.addGradeForSemester(item, gradeEntry.getSemesterNumber(), gradeEntry.getSemester());
            }

            adapter.updateSummary();
        }

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Receive an Error Event and displays it to the user.
     *
     * @param errorEvent - error event
     */
    public void onEventMainThread(ErrorEvent errorEvent) {

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }

        String title;
        String text;

        switch (errorEvent.getType()) {
            case TIMEOUT:
                title = "Zeitüberschreitung bei der Anfrage";
                text = "Es hat zu lange gedauert mit dem Server deiner Hochschule zu kommunizieren. \n" +
                        "Das kann verschiedene Gründe haben: \n" +
                        "Deine Verbindung zum Internet ist nicht optimal. \n" +
                        "Das Notensystem deiner Hochschule ist überlastet.";
                break;
            case NO_NETWORK:
                title = "Keine Internetverbindung";
                text = "Du hast zur Zeit keine Verbindung zum Internet. Bitte versuche es erneut!";
                break;
            case GENERAL:
                title = "Fehler beim Abrufen der Noten";
                text = "Deine Noten konnten nicht abgerufen werden. \n" +
                        "Das kann verschiedene Gründe haben: \n" +
                        "1. Deine Zugangsdaten sind falsch. \n" +
                        "2. Probleme mit der Internetverbindung oder dem Server der Hochschule. \n" +
                        "3. Die Linkstruktur innerhalb deines Notensystems könnte sich geändert haben" +
                        " und so ist es zur Zeit nicht möglich deine Noten abzurufen.";
                break;
            default:
                title = "Fehler beim Abrufen der Noten";
                text = "Deine Noten konnten nicht abgerufen werden. \n" +
                        "Das kann verschiedene Gründe haben: \n" +
                        "1. Deine Zugangsdaten sind falsch. \n" +
                        "2. Probleme mit der Internetverbindung oder dem Server der Hochschule. \n" +
                        "3. Die Linkstruktur innerhalb deines Notensystems könnte sich geändert haben" +
                        " und so ist es zur Zeit nicht möglich deine Noten abzurufen.";
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    /**
     * Checks if user is already logged in.
     *
     * @return true if user is logged in.
     */
    private boolean checkLogin() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean(Constants.PREF_KEY_LOGGED_IN, false);
    }

    /**
     * Starts the SelectUniversityActivity.
     */
    private void goToUniversitySelection() {
        Intent intent = new Intent(this, SelectUniversityActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                mainServiceHelper.logout();

                Intent intent = new Intent(this, SelectUniversityActivity.class);
                intent.putExtra(MainActivity.EXTRA_INITIAL_LOADING, true);
                // set flags, so the user won't be able to go back to the main activity
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
