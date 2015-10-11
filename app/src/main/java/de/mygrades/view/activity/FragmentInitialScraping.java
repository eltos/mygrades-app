package de.mygrades.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;

import de.greenrobot.event.EventBus;
import de.mygrades.R;
import de.mygrades.main.MainServiceHelper;
import de.mygrades.main.events.ErrorEvent;
import de.mygrades.main.events.ScrapeProgressEvent;
import de.mygrades.view.ProgressImageViewOverlay;

/**
 * Fragment to show initial loading screen.
 */
public class FragmentInitialScraping extends Fragment {
    private static final String PROGRESS_STATE = "progress_state";
    private static final String NEXT_PROGRESS_STATE = "next_progress_state";
    private static final String ERROR_TYPE_STATE = "error_type";

    private ErrorEvent.ErrorType receivedErrorType;
    private static final int ANIMATION_DURATION = 500;

    private MainServiceHelper mainServiceHelper;

    private ProgressImageViewOverlay progressImageViewOverlay;
    private LinearLayout llContentWrapper;
    private LinearLayout llStatusWrapper;
    private LinearLayout llErrorWrapper;
    private LinearLayout llProgressWrapper;

    private View inflatedErrorGeneral;
    private View inflatedErrorTimeout;
    private View inflatedErrorNoNetwork;

    private Button btnTryAgain;
    private Button btnBackToLogin;

    private Handler handler = new Handler();
    private Runnable progressAnimation = new Runnable() {
        @Override
        public void run() {
            progressImageViewOverlay.increaseProgress(0.001f);
            handler.postDelayed(progressAnimation, 20);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_initial_scraping, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainServiceHelper = new MainServiceHelper(getContext());

        // initialize all views
        progressImageViewOverlay = (ProgressImageViewOverlay) view.findViewById(R.id.iv_progress_overlay);
        llContentWrapper = (LinearLayout) view.findViewById(R.id.ll_content_wrapper);
        llStatusWrapper = (LinearLayout) view.findViewById(R.id.ll_status_wrapper);
        llErrorWrapper = (LinearLayout) view.findViewById(R.id.ll_error_wrapper);
        llProgressWrapper = (LinearLayout) view.findViewById(R.id.ll_progress_wrapper);
        btnTryAgain = (Button) view.findViewById(R.id.btn_try_again);
        btnBackToLogin = (Button) view.findViewById(R.id.btn_back_to_login);

        // init buttons
        initButtons();

        // restore instance state
        if (savedInstanceState != null) {
            // set progress
            float progress = savedInstanceState.getFloat(PROGRESS_STATE);
            float nextProgress = savedInstanceState.getFloat(NEXT_PROGRESS_STATE);
            progressImageViewOverlay.setProgress(progress, nextProgress);

            // check if the error type is set
            String receivedErrorTypeAsString = savedInstanceState.getString(ERROR_TYPE_STATE, null);
            receivedErrorType = receivedErrorTypeAsString == null ? null : ErrorEvent.ErrorType.valueOf(receivedErrorTypeAsString);

            if (receivedErrorType == null) {
                progressAnimation.run();
                hideErrorWrapper();
            } else {
                showError(receivedErrorType, 0);
            }
        } else {
            // start scraping
            mainServiceHelper.scrapeForGrades(true);
            hideErrorWrapper();
        }

        // register to events
        EventBus.getDefault().register(this);

        // (always) move status wrapper to center immediately
        moveStatusWrapperToCenter(0);
    }

    /**
     * Hides the error wrapper. Only used in onCreateView.
     */
    private void hideErrorWrapper() {
        llErrorWrapper.setVisibility(View.GONE);
        llErrorWrapper.setAlpha(0f);
    }

    /**
     * Initializes the try-again button and back-to-login button.
     */
    private void initButtons() {
        btnTryAgain.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // disable button so it cannot be clicked again
                btnTryAgain.setEnabled(false);

                // reset progress
                progressImageViewOverlay.setProgress(0, 0);

                // scrape again
                mainServiceHelper.scrapeForGrades(true);

                // fade out error wrapper
                llErrorWrapper.animate().alpha(0f).setDuration(ANIMATION_DURATION);
                receivedErrorType = null;

                // move progress wrapper to center
                moveProgressWrapperToCenter(ANIMATION_DURATION);
            }
        });

        btnBackToLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back to login TODO: SelectUniversityActivity -> LoginActivity
                Intent intent = new Intent(getContext(), SelectUniversityActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    /**
     * Moves the status wrapper to the center (animated).
     * The status wrapper contains the progressWrapper and errorWrapper.
     *
     * @param duration duration of the animation in milliseconds.
     */
    private void moveStatusWrapperToCenter(final int duration) {
        llStatusWrapper.post(new Runnable() {
            @Override
            public void run() {
                int translateY = (int) ((llContentWrapper.getHeight() - llStatusWrapper.getHeight()) / 2f - llStatusWrapper.getTranslationY());
                llStatusWrapper.animate().translationYBy(translateY).setDuration(duration);
            }
        });
    }

    /**
     * Moves the status wrapper, so the progressWrapper is in the center.
     * This is used for the animation, when the errorWrapper is fading out.
     *
     * @param duration duration of the animation in milliseconds.
     */
    private void moveProgressWrapperToCenter(final int duration) {
        llStatusWrapper.post(new Runnable() {
            @Override
            public void run() {
                int translateY = (int) ((llStatusWrapper.getHeight() - llProgressWrapper.getHeight()) / 2f - llProgressWrapper.getTranslationY());
                llStatusWrapper.animate().translationYBy(translateY).setDuration(duration);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat(PROGRESS_STATE, progressImageViewOverlay.getProgress());
        outState.putFloat(NEXT_PROGRESS_STATE, progressImageViewOverlay.getNextProgress());

        if (receivedErrorType != null) {
            outState.putString(ERROR_TYPE_STATE, receivedErrorType.name());
        }

        stopProgressAnimation();
    }

    /**
     * Receive event about scraping progress and set the current progress.
     * If the current step is the first one, an ongoing animation runnable will be started.
     *
     * @param scrapeProgressEvent ScrapeProgressEvent
     */
    public void onEventMainThread(ScrapeProgressEvent scrapeProgressEvent) {
        if (llStatusWrapper != null) {
            int currentStep = scrapeProgressEvent.getCurrentStep();
            int stepCount = scrapeProgressEvent.getStepCount();

            float progress = ((float) currentStep) / stepCount;
            float nextProgress = ((float) currentStep + 1) / stepCount;

            // set current progress
            progressImageViewOverlay.setProgress(progress, nextProgress);

            if (currentStep == 0) {
                // start animation at first step, it runs continuously
                progressAnimation.run();
            } else if (currentStep == stepCount) {
                stopProgressAnimation();
            }
        }
    }

    /**
     * Receive an ErrorEvent and show an error message depending on the ErrorType.
     *
     * @param errorEvent ErrorEvent
     */
    public void onEventMainThread(ErrorEvent errorEvent) {
        if (llErrorWrapper != null) {
            showError(errorEvent.getType(), ANIMATION_DURATION);
            moveStatusWrapperToCenter(ANIMATION_DURATION);
        }
    }

    /**
     * Shows an error by the given ErrorType.
     * If the duration > 0, an fade-in animation is shown.
     *
     * @param errorType ErrorType
     * @param duration duration of fade-in animation
     */
    private void showError(ErrorEvent.ErrorType errorType, int duration) {
        // set receivedErrorType (used to store in instanceState)
        receivedErrorType = errorType;

        // stop the progress animation
        stopProgressAnimation();

        // enable try again button
        btnTryAgain.setEnabled(true);

        // hide error wrapper if it was shown previously
        hideInflatedErrors();

        switch (errorType) {
            case NO_NETWORK:
                inflatedErrorNoNetwork = inflateViewStub(inflatedErrorNoNetwork, R.id.stub_no_network_error);
                btnBackToLogin.setVisibility(View.GONE);
                break;
            case TIMEOUT:
                inflatedErrorTimeout = inflateViewStub(inflatedErrorTimeout, R.id.stub_timeout_error);
                btnBackToLogin.setVisibility(View.GONE);
                break;
            case GENERAL:
                inflatedErrorGeneral = inflateViewStub(inflatedErrorGeneral, R.id.stub_general_error);
                btnBackToLogin.setVisibility(View.VISIBLE);
                break;
        }

        // show error wrapper
        llErrorWrapper.setVisibility(View.VISIBLE);
        llErrorWrapper.animate().alpha(1.0f).setDuration(duration);
    }

    /**
     * Inflates a ViewStub if necessary.
     * If it was already inflated, the visibility is set to VISIBLE.
     *
     * @param inflatedError - the view which will be inflated (if necessary)
     * @param stubResId - StubView id
     * @return the inflated view
     */
    private View inflateViewStub(View inflatedError, int stubResId) {
        if (inflatedError == null) {
            inflatedError = ((ViewStub) getView().findViewById(stubResId)).inflate();
        }
        inflatedError.setVisibility(View.VISIBLE);
        return inflatedError;
    }

    /**
     * Hides all inflated error views.
     */
    private void hideInflatedErrors() {
        if (inflatedErrorGeneral != null) {
            inflatedErrorGeneral.setVisibility(View.GONE);
        }
        if (inflatedErrorNoNetwork != null) {
            inflatedErrorNoNetwork.setVisibility(View.GONE);
        }
        if (inflatedErrorTimeout != null) {
            inflatedErrorTimeout.setVisibility(View.GONE);
        }
    }

    /**
     * Stops the progress animation.
     */
    private void stopProgressAnimation() {
        // stop animation, if last step is reached
        handler.removeCallbacks(progressAnimation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
